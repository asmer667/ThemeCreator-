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
        shapes3D: Set<String> = emptySet(),
        backgroundUri: Uri? = null,
        outputFile: File,
    ): File {
        outputFile.parentFile?.mkdirs()

        val shapes3DBytes = mutableMapOf<String, ByteArray>()
        shapes3D.forEach { fileName ->
            Shapes3DLoader.readBytes(context, fileName)?.let {
                shapes3DBytes[fileName] = it
            }
        }

        var backgroundBytes: ByteArray? = null
        var backgroundName: String? = null
        if (backgroundUri != null) {
            try {
                context.contentResolver.openInputStream(backgroundUri)?.use {
                    backgroundBytes = it.readBytes()
                }
                backgroundName = "background.png"
            } catch (e: Exception) { e.printStackTrace() }
        }

        ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
            zos.setLevel(9)

            zos.putNextEntry(ZipEntry(VERSION_FILE))
            zos.write(ByteBuffer.allocate(9).apply {
                order(ByteOrder.LITTLE_ENDIAN); put(CURRENT_VERSION); putLong(Date().time)
            }.array())
            zos.closeEntry()

            zos.putNextEntry(ZipEntry(TOML_FILE))
            zos.write(TomlGenerator.generate(theme, icons, shapes3DBytes.keys, backgroundName).toByteArray())
            zos.closeEntry()

            icons.forEach { (name, uri) ->
                try {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        zos.putNextEntry(ZipEntry(name))
                        input.copyTo(zos); zos.closeEntry()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }

            shapes3DBytes.forEach { (name, bytes) ->
                zos.putNextEntry(ZipEntry(name))
                zos.write(bytes); zos.closeEntry()
            }

            if (backgroundBytes != null && backgroundName != null) {
                zos.putNextEntry(ZipEntry(backgroundName))
                zos.write(backgroundBytes!!); zos.closeEntry()
            }
        }
        return outputFile
    }
}
