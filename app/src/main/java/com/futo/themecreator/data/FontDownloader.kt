package com.futo.themecreator.data

import android.content.Context
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

/**
 * يحمّل الخطوط من Google Fonts ويحفظها محليًا.
 * الخطوط تُحفظ في: context.filesDir/fonts/<fileName>
 */
object FontDownloader {

    private const val TIMEOUT_MS = 30_000

    fun fontsDir(context: Context): File {
        val dir = File(context.filesDir, "fonts")
        if (!dir.exists()) dir.mkdirs()
        return dir
    }

    fun fontFile(context: Context, fileName: String): File =
        File(fontsDir(context), fileName)

    fun isDownloaded(context: Context, fileName: String): Boolean =
        fontFile(context, fileName).exists() &&
        fontFile(context, fileName).length() > 1000

    /**
     * يحمّل خطًا واحدًا. يعيد `true` عند النجاح.
     */
    suspend fun download(context: Context, font: FontInfo): Boolean =
        withContext(Dispatchers.IO) {
            try {
                val target = fontFile(context, font.fileName)
                if (target.exists() && target.length() > 1000) return@withContext true

                val url = URL(font.url)
                val conn = url.openConnection() as HttpURLConnection
                conn.connectTimeout = TIMEOUT_MS
                conn.readTimeout = TIMEOUT_MS
                conn.instanceFollowRedirects = true
                conn.connect()

                if (conn.responseCode != 200) {
                    conn.disconnect()
                    return@withContext false
                }

                val temp = File(target.parentFile, "${font.fileName}.tmp")
                conn.inputStream.use { input ->
                    temp.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                conn.disconnect()

                if (temp.length() < 1000) {
                    temp.delete()
                    return@withContext false
                }
                temp.renameTo(target)
                true
            } catch (e: Exception) {
                e.printStackTrace()
                false
            }
        }

    fun delete(context: Context, fileName: String): Boolean =
        fontFile(context, fileName).delete()

    fun listDownloaded(context: Context): List<String> =
        fontsDir(context).listFiles()
            ?.filter { it.extension.lowercase() in setOf("ttf", "otf") }
            ?.map { it.name }
            ?: emptyList()

    /**
     * يقرأ بايتات الخط من المكانين:
     *  1. filesDir/fonts/ (المحمَّلة)
     *  2. assets/fonts/ (المضمّنة في APK)
     * يُرجع null إن لم يُوجد.
     */
    fun loadFontBytes(context: Context, fileName: String): ByteArray? {
        // 1. من filesDir
        val file = fontFile(context, fileName)
        if (file.exists() && file.length() > 1000) {
            try {
                return file.readBytes()
            } catch (e: Exception) { e.printStackTrace() }
        }
        // 2. من assets
        return try {
            context.assets.open("fonts/$fileName").use { it.readBytes() }
        } catch (e: Exception) {
            try {
                context.assets.open(fileName).use { it.readBytes() }
            } catch (e2: Exception) { null }
        }
    }

    /**
     * يتحقق إن كان الخط موجودًا في أي من المكانين.
     */
    fun fontExists(context: Context, fileName: String): Boolean {
        if (isDownloaded(context, fileName)) return true
        return try {
            context.assets.open("fonts/$fileName").use { true }
        } catch (e: Exception) {
            try {
                context.assets.open(fileName).use { true }
            } catch (e2: Exception) { false }
        }
    }
}
