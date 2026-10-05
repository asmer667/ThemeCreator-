package com.futo.themecreator.data

import android.content.Context
import android.net.Uri
import java.io.File
import java.io.FileOutputStream
import java.nio.ByteBuffer
import java.nio.ByteOrder
import java.util.Date
import java.util.zip.ZipEntry
import java.util.zip.ZipOutputStream

object ZipPacker {

    private const val VERSION_FILE = "FUTOKeyboardTheme_Version"
    private const val TOML_FILE = "theme.txt"
    private const val CURRENT_VERSION: Byte = 1

    fun pack(
        context: Context,
        theme: ThemeData,
        icons: Map<String, Uri>,
        outputFile: File,
    ): File {
        outputFile.parentFile?.mkdirs()

        ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
            zos.setLevel(9)

            // 1. ملف الإصدار
            putVersionFile(zos)

            // 2. theme.txt (مع الأيقونات)
            putTomlFile(zos, theme, icons)

            // 3. الأيقونات PNG
            icons.forEach { (name, uri) ->
                putIconFile(zos, context, name, uri)
            }
        }

        return outputFile
    }

    private fun putVersionFile(zos: ZipOutputStream) {
        zos.putNextEntry(ZipEntry(VERSION_FILE))
        val bytes = ByteBuffer.allocate(9).apply {
            order(ByteOrder.LITTLE_ENDIAN)
            put(CURRENT_VERSION)
            putLong(Date().time)
        }.array()
        zos.write(bytes)
        zos.closeEntry()
    }

    private fun putTomlFile(
        zos: ZipOutputStream,
        theme: ThemeData,
        icons: Map<String, Uri>,
    ) {
        zos.putNextEntry(ZipEntry(TOML_FILE))
        val content = TomlGenerator.generate(theme, icons)
        zos.write(content.toByteArray(Charsets.UTF_8))
        zos.closeEntry()
    }

    private fun putIconFile(
        zos: ZipOutputStream,
        context: Context,
        name: String,
        uri: Uri,
    ) {
        try {
            context.contentResolver.openInputStream(uri)?.use { input ->
                zos.putNextEntry(ZipEntry(name))
                input.copyTo(zos)
                zos.closeEntry()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
