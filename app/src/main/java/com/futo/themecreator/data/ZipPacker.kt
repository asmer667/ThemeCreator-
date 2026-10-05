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
        outputFile: File,
    ): File {
        outputFile.parentFile?.mkdirs()

        // قراءة الأشكال 3D المختارة
        val shapes3DBytes = mutableMapOf<String, ByteArray>()
        shapes3D.forEach { fileName ->
            Shapes3DLoader.readBytes(context, fileName)?.let {
                shapes3DBytes[fileName] = it
            }
        }

        ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
            zos.setLevel(9)

            // 1. Version
            zos.putNextEntry(ZipEntry(VERSION_FILE))
            zos.write(ByteBuffer.allocate(9).apply {
                order(ByteOrder.LITTLE_ENDIAN); put(CURRENT_VERSION); putLong(Date().time)
            }.array())
            zos.closeEntry()

            // 2. TOML
            zos.putNextEntry(ZipEntry(TOML_FILE))
            zos.write(TomlGenerator.generate(theme, icons, shapes3DBytes.keys).toByteArray())
            zos.closeEntry()

            // 3. Icons (uploaded)
            icons.forEach { (name, uri) ->
                try {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        zos.putNextEntry(ZipEntry(name))
                        input.copyTo(zos); zos.closeEntry()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }

            // 4. Shapes 3D
            shapes3DBytes.forEach { (name, bytes) ->
                zos.putNextEntry(ZipEntry(name))
                zos.write(bytes); zos.closeEntry()
            }
        }
        return outputFile
    }
}
