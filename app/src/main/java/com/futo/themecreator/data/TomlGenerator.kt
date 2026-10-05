package com.futo.themecreator.data

import android.net.Uri

object TomlGenerator {
    fun generate(
        theme: ThemeData,
        icons: Map<String, Uri> = emptyMap(),
        shapeFiles: Set<String> = emptySet(),
        backgroundName: String? = null,
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
        
        if (backgroundName != null) {
            sb.appendLine()
            sb.appendLine("[options.background]")
            sb.appendLine("image = \"$backgroundName\"")
            sb.appendLine("opacity = ${theme.backgroundImageOpacity}")
            sb.appendLine("action_bar_opacity = ${theme.backgroundActionBarOpacity}")
            sb.appendLine("cropping = [0.0, 0.0, 1.0, 1.0]")
        }
        
        sb.appendLine()
        sb.appendLine("[colors]")
        theme.allColors().forEach { (k, v) -> sb.appendLine("$k = \"$v\"") }

        if (icons.isNotEmpty() || shapeFiles.isNotEmpty()) {
            sb.appendLine()
            sb.appendLine("[matchrules]")
            icons.keys.forEach { name ->
                val base = name.removeSuffix(".png")
                sb.appendLine("border = [{ selector = \"$base\", asset = \"$name\" }]")
            }
            shapeFiles.forEach { fileName ->
                val base = fileName.removeSuffix(".png")
                sb.appendLine("border = [{ selector = \"$base\", asset = \"$fileName\" }]")
            }
        }
        return sb.toString()
    }
    private fun esc(s: String) = s.replace("\\", "\\\\").replace("\"", "\\\"")
}
