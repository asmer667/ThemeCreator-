package com.futo.themecreator.data

/**
 * يطبّق "جاهزة 2" — الخلفية فقط، لا يلمس الأزرار
 */
object Ready2Applier {

    fun applySolid(preset: Ready2Preset) {
        val t = ThemeState.theme
        val bg = preset.bg
        val onBg = if (isDark(bg)) "#FFFFFF" else "#1A1A1A"

        ThemeState.replace(t.copy(
            background = bg,
            onBackground = onBg,
            surface = bg,
            onSurface = onBg,
            surfaceVariant = shade(bg, 0.1f),
            onSurfaceVariant = onBg,
            surfaceBright = shade(bg, 0.15f),
            surfaceDim = shade(bg, -0.1f),
            surfaceContainer = shade(bg, 0.05f),
            surfaceContainerHigh = shade(bg, 0.1f),
            surfaceContainerHighest = shade(bg, 0.15f),
            surfaceContainerLow = shade(bg, -0.05f),
            surfaceContainerLowest = shade(bg, -0.1f),
            keyboardSurface = bg,
            keyboardSurfaceDim = shade(bg, -0.05f),
            gradientStart = null,
            gradientEnd = null,
            // لا نلمس keyboardContainer أو primary
        ))
    }

    fun applyGradient(preset: Ready2Preset) {
        val t = ThemeState.theme
        val c1 = preset.color1 ?: preset.bg
        val c2 = preset.color2 ?: preset.bg
        val bg = preset.bg
        val onBg = if (isDark(bg)) "#FFFFFF" else "#1A1A1A"

        ThemeState.replace(t.copy(
            background = bg,
            onBackground = onBg,
            surface = c1,
            onSurface = onBg,
            surfaceVariant = c2,
            onSurfaceVariant = onBg,
            surfaceBright = c2,
            surfaceDim = bg,
            surfaceContainer = c1,
            surfaceContainerHigh = c2,
            surfaceContainerHighest = c2,
            surfaceContainerLow = bg,
            surfaceContainerLowest = bg,
            keyboardSurface = bg,
            keyboardSurfaceDim = bg,
            gradientStart = c1,
            gradientEnd = c2,
            gradientAngle = 135f,
            // لا نلمس keyboardContainer
        ))
    }

    private fun isDark(hex: String): Boolean {
        return try {
            val h = hex.removePrefix("#")
            val r = h.substring(0, 2).toInt(16)
            val g = h.substring(2, 4).toInt(16)
            val b = h.substring(4, 6).toInt(16)
            (r * 0.299 + g * 0.587 + b * 0.114) < 128
        } catch (e: Exception) { true }
    }

    private fun shade(hex: String, amount: Float): String {
        return try {
            val h = hex.removePrefix("#")
            var r = h.substring(0, 2).toInt(16)
            var g = h.substring(2, 4).toInt(16)
            var b = h.substring(4, 6).toInt(16)
            if (amount > 0) {
                r = (r + (255 - r) * amount).toInt().coerceIn(0, 255)
                g = (g + (255 - g) * amount).toInt().coerceIn(0, 255)
                b = (b + (255 - b) * amount).toInt().coerceIn(0, 255)
            } else {
                r = (r * (1 + amount)).toInt().coerceIn(0, 255)
                g = (g * (1 + amount)).toInt().coerceIn(0, 255)
                b = (b * (1 + amount)).toInt().coerceIn(0, 255)
            }
            "#%02X%02X%02X".format(r, g, b)
        } catch (e: Exception) { hex }
    }
}
