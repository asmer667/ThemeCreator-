package com.futo.themecreator.data

import android.net.Uri

object TomlGenerator {
    fun generate(
        theme: ThemeData,
        icons: Map<String, Uri> = emptyMap(),
        shapeFiles: Set<String> = emptySet(),
    ): String {
        val sb = StringBuilder()
        sb.appendLine("# Format version: 1.0")
        sb.appendLine("name = \"${esc(theme.name)}\"")
        sb.appendLine("author = \"${esc(theme.author)}\"")
        sb.appendLine("id = \"${esc(theme.id)}\"")
        sb.appendLine("version = ${theme.version}")
        sb.appendLine("description = \"${esc(theme.description)}\"")
        sb.appendLine()
        sb.appendLine("[options]")
        sb.appendLine("auto_borders = ${theme.autoBorders}")
        sb.appendLine("center_hints = ${theme.centerHints}")
        sb.appendLine("roundedness = ${theme.roundedness}")
        sb.appendLine("scale_text = ${theme.scaleText}")
        sb.appendLine("scale_hints = ${theme.scaleHints}")
        sb.appendLine("weight_text = ${theme.weightText}")
        sb.appendLine("weight_hints = ${theme.weightHints}")
        sb.appendLine()
        sb.appendLine("[colors]")
        theme.allColors().forEach { (k, v) -> sb.appendLine("$k = \"$v\"") }

        if (icons.isNotEmpty() || shapeFiles.isNotEmpty()) {
            sb.appendLine()
            sb.appendLine("[matchrules]")

            // أيقونات مرفوعة
            icons.keys.forEach { name ->
                val base = name.removeSuffix(".png")
                sb.appendLine("border = [{ selector = \"$base\", asset = \"$name\" }]")
            }

            // أشكال 3D (يُطبّق على كل الأزرار)
            shapeFiles.forEach { fileName ->
                val base = fileName.removeSuffix(".png")
                sb.appendLine("border = [{ selector = \"$base\", asset = \"$fileName\" }]")
            }
        }
        return sb.toString()
    }

    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
}
