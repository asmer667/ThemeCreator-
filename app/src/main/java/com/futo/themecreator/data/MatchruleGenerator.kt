package com.futo.themecreator.data

/**
 * يُولّد قسم [matchrules] في TOML لربط الأيقونات بالأزرار
 */
object MatchruleGenerator {

    /**
     * يُنتج سطور matchrules للأيقونات المرفوعة
     */
    fun generate(icons: Map<String, android.net.Uri>): String {
        if (icons.isEmpty()) return ""

        val sb = StringBuilder()
        sb.appendLine()
        sb.appendLine("[matchrules]")

        // Borders
        icons.keys.filter { it.endsWith("button.png") || it == "blank.png" }.forEach { fileName ->
            val name = fileName.removeSuffix(".png")
            sb.appendLine("border = [")
            sb.appendLine("    { selector = \"$name\", asset = \"$fileName\" },")
            sb.appendLine("]")
        }

        return sb.toString()
    }
}
