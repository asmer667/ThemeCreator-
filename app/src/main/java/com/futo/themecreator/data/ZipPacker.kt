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

/**
 * يحزم الثيم في ZIP بصيغة FUTO Keyboard الرسمية.
 *
 * البنية الناتجة:
 *   theme.txt                       ← الإعدادات (INI)
 *   <font>.ttf                      ← الخط (في جذر ZIP، بحسب صيغة FUTO)
 *   <background>.png                ← صورة الخلفية
 *   Button-default.png
 *   Button-default-press.png
 *   Button-default-dark.png
 *   ... (14 زر + 5 أيقونات رسمية)
 *   Icon-backspace.png
 *   ...
 *   shapes_3d/<name>.png            ← الأشكال الإضافية (اختياري)
 */
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

        // ─── 1. تحميل صور الأشكال 3D من assets ───
        val shapes3DBytes = mutableMapOf<String, ByteArray>()
        shapes3D.forEach { fileName ->
            Shapes3DLoader.readBytes(context, fileName)?.let {
                shapes3DBytes[fileName] = it
            }
        }

        // ─── 2. تحميل صورة الخلفية ───
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

        // ─── 3. توليد صورة التدرّج (إن وُجد) كخلفية ───
        val gradientBytes: ByteArray? = if (
            backgroundBytes == null &&
            theme.gradientStart != null &&
            theme.gradientEnd != null
        ) {
            try {
                ShapesGenerator.generateGradientPNG(
                    theme.gradientStart!!,
                    theme.gradientEnd!!,
                    theme.gradientAngle,
                    512,
                )
            } catch (e: Exception) { null }
        } else null
        if (gradientBytes != null) backgroundName = "background.png"

        // ─── 4. تحميل الخط (إن وُجد) ───
        val fontBytes: ByteArray? = theme.fontName?.let { name ->
            try {
                context.assets.open("fonts/$name").use { it.readBytes() }
            } catch (e: Exception) {
                try {
                    context.assets.open(name).use { it.readBytes() }
                } catch (e2: Exception) { null }
            }
        }

        // ─── 5. بناء ZIP ───
        ZipOutputStream(FileOutputStream(outputFile)).use { zos ->
            zos.setLevel(9)

            // ملف الإصدار
            zos.putNextEntry(ZipEntry(VERSION_FILE))
            zos.write(ByteBuffer.allocate(9).apply {
                order(ByteOrder.LITTLE_ENDIAN)
                put(CURRENT_VERSION)
                putLong(Date().time)
            }.array())
            zos.closeEntry()

            // theme.txt
            zos.putNextEntry(ZipEntry(TOML_FILE))
            zos.write(
                TomlGenerator.generate(
                    theme, icons, shapes3DBytes.keys, backgroundName
                ).toByteArray()
            )
            zos.closeEntry()

            // الخط
            if (fontBytes != null && theme.fontName != null) {
                zos.putNextEntry(ZipEntry(theme.fontName!!))
                zos.write(fontBytes)
                zos.closeEntry()
            }

            // صور الخلفية (تدرّج أو صورة المستخدم)
            backgroundBytes?.let { bytes ->
                zos.putNextEntry(ZipEntry("background.png"))
                zos.write(bytes)
                zos.closeEntry()
            }
            gradientBytes?.let { bytes ->
                zos.putNextEntry(ZipEntry("background.png"))
                zos.write(bytes)
                zos.closeEntry()
            }

            // أزرار مخصصة (customButtonImages) — منفصلة عن الأسماء الرسمية
            theme.customButtonImages.forEach { (fileName, uriStr) ->
                try {
                    val uri = Uri.parse(uriStr)
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        zos.putNextEntry(ZipEntry(fileName))
                        input.copyTo(zos)
                        zos.closeEntry()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }

            // أيقونات مخصصة
            theme.customIconImages.forEach { (fileName, uriStr) ->
                try {
                    val uri = Uri.parse(uriStr)
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        zos.putNextEntry(ZipEntry(fileName))
                        input.copyTo(zos)
                        zos.closeEntry()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }

            // الأيقونات القديمة (للتوافق)
            icons.forEach { (name, uri) ->
                try {
                    context.contentResolver.openInputStream(uri)?.use { input ->
                        zos.putNextEntry(ZipEntry(name))
                        input.copyTo(zos)
                        zos.closeEntry()
                    }
                } catch (e: Exception) { e.printStackTrace() }
            }

            // الأشكال 3D
            shapes3DBytes.forEach { (name, bytes) ->
                zos.putNextEntry(ZipEntry("shapes_3d/$name"))
                zos.write(bytes)
                zos.closeEntry()
            }
        }
        return outputFile
    }
}
