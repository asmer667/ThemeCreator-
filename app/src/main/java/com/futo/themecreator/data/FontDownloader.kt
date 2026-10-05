package com.futo.themecreator.data

import android.content.Context
import android.net.Uri
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.net.HttpURLConnection
import java.net.URL

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

    suspend fun downloadWithProgress(
        context: Context,
        font: FontInfo,
        onProgress: (Long, Long) -> Unit,
    ): Boolean = withContext(Dispatchers.IO) {
        try {
            val target = fontFile(context, font.fileName)
            if (target.exists() && target.length() > 1000) {
                onProgress(1, 1)
                return@withContext true
            }

            val conn = URL(font.url).openConnection() as HttpURLConnection
            conn.connectTimeout = TIMEOUT_MS
            conn.readTimeout = TIMEOUT_MS
            conn.instanceFollowRedirects = true
            conn.connect()

            if (conn.responseCode != 200) {
                conn.disconnect()
                return@withContext false
            }

            val total = conn.contentLengthLong
            val temp = File(target.parentFile, "${font.fileName}.tmp")

            conn.inputStream.use { input ->
                temp.outputStream().use { output ->
                    val buf = ByteArray(8192)
                    var read: Int
                    var sum = 0L
                    while (input.read(buf).also { read = it } > 0) {
                        output.write(buf, 0, read)
                        sum += read
                        onProgress(sum, total)
                    }
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

    suspend fun download(context: Context, font: FontInfo): Boolean =
        downloadWithProgress(context, font) { _, _ -> }

    fun delete(context: Context, fileName: String): Boolean =
        fontFile(context, fileName).delete()

    fun listDownloaded(context: Context): List<String> =
        fontsDir(context).listFiles()
            ?.filter { it.extension.lowercase() in setOf("ttf", "otf") }
            ?.map { it.name }
            ?: emptyList()

    fun loadFontBytes(context: Context, fileName: String): ByteArray? {
        val file = fontFile(context, fileName)
        if (file.exists() && file.length() > 1000) {
            try { return file.readBytes() } catch (e: Exception) { e.printStackTrace() }
        }
        return try {
            context.assets.open("fonts/$fileName").use { it.readBytes() }
        } catch (e: Exception) {
            try {
                context.assets.open(fileName).use { it.readBytes() }
            } catch (e2: Exception) { null }
        }
    }

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

    suspend fun importFromUri(context: Context, uri: Uri): String? =
        withContext(Dispatchers.IO) {
            try {
                val cursor = context.contentResolver.query(uri, null, null, null, null)
                var displayName: String? = null
                cursor?.use {
                    if (it.moveToFirst()) {
                        val idx = it.getColumnIndex(android.provider.OpenableColumns.DISPLAY_NAME)
                        if (idx >= 0) displayName = it.getString(idx)
                    }
                }
                var name = displayName?.substringAfterLast('/') ?: "imported_${System.currentTimeMillis()}.ttf"
                if (!name.endsWith(".ttf", true) && !name.endsWith(".otf", true)) {
                    name += ".ttf"
                }
                name = name.replace(Regex("[^a-zA-Z0-9._-]"), "_")
                val target = fontFile(context, name)

                context.contentResolver.openInputStream(uri)?.use { input ->
                    target.outputStream().use { output ->
                        input.copyTo(output)
                    }
                }

                if (!target.exists() || target.length() < 1000) {
                    target.delete()
                    null
                } else name
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }
}
