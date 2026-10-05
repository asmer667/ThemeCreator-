package com.futo.themecreator.data

import android.net.Uri

/**
 * يحوّل ThemeData + الأيقونات إلى ملف theme.txt (TOML)
 */
object TomlGenerator {

    fun generate(theme: ThemeData, icons: Map<String, Uri> = emptyMap()): String {
        val sb = StringBuilder()

        // ═══════ Metadata ═══════
        sb.appendLine("# Format version: 1.0")
        sb.appendLine("name = \"${escape(theme.name)}\"")
        sb.appendLine("author = \"${escape(theme.author)}\"")
        sb.appendLine("id = \"${escape(theme.id)}\"")
        sb.appendLine("version = ${theme.version}")
        sb.appendLine("description = \"${escape(theme.description)}\"")
        sb.appendLine()

        // ═══════ Options ═══════
        sb.appendLine("[options]")
        sb.appendLine("auto_borders = ${theme.autoBorders}")
        sb.appendLine("center_hints = ${theme.centerHints}")
        sb.appendLine("roundedness = ${theme.roundedness}")
        sb.appendLine("scale_text = ${theme.scaleText}")
        sb.appendLine("scale_hints = ${theme.scaleHints}")
        sb.appendLine("weight_text = ${theme.weightText}")
        sb.appendLine("weight_hints = ${theme.weightHints}")
        sb.appendLine()

        // ═══════ Colors ═══════
        sb.appendLine("[colors]")
        theme.allColors().forEach { (key, value) ->
            sb.appendLine("$key = \"$value\"")
        }

        // ═══════ Matchrules (للأيقونات) ═══════
        if (icons.isNotEmpty()) {
            sb.append(MatchruleGenerator.generate(icons))
        }

        return sb.toString()
    }

    private fun escape(s: String): String =
        s.replace("\\", "\\\\").replace("\"", "\\\"")
}
