package com.futo.themecreator.data

/**
 * يطبق Preset على ThemeData مباشرة
 */
object PresetApplier {

    fun applySolid(preset: SolidPreset, dark: Boolean) {
        val t = ThemeState.theme
        val onAccent = if (dark) "#FFFFFF" else "#FFFFFF"
        val onBg = if (dark) "#FFFFFF" else "#1A1A1A"

        ThemeState.replace(
            t.copy(
                primary = preset.accent,
                onPrimary = onAccent,
                primaryContainer = preset.container,
                onPrimaryContainer = onBg,
                inversePrimary = preset.accent,
                secondary = preset.accent,
                onSecondary = onAccent,
                secondaryContainer = preset.surface,
                onSecondaryContainer = onBg,
                tertiary = preset.accent,
                onTertiary = onAccent,
                tertiaryContainer = preset.container,
                onTertiaryContainer = onBg,
                background = preset.bg,
                onBackground = onBg,
                surface = preset.bg,
                onSurface = onBg,
                surfaceVariant = preset.surface,
                onSurfaceVariant = onBg,
                surfaceTint = preset.accent,
                inverseSurface = onBg,
                inverseOnSurface = preset.bg,
                outline = preset.accent,
                outlineVariant = preset.surface,
                surfaceBright = preset.surface,
                surfaceDim = preset.bg,
                surfaceContainer = preset.surface,
                surfaceContainerHigh = preset.container,
                surfaceContainerHighest = preset.container,
                surfaceContainerLow = preset.bg,
                surfaceContainerLowest = preset.bg,
                keyboardSurface = preset.bg,
                keyboardSurfaceDim = preset.bg,
                keyboardContainer = preset.surface,
                keyboardContainerVariant = preset.container,
                onKeyboardContainer = onBg,
                keyboardPress = preset.accent,
                keyboardContainerPressed = preset.container,
                onKeyboardContainerPressed = onBg,
            )
        )
    }

    fun applyGradient(preset: GradientPreset, dark: Boolean) {
        val t = ThemeState.theme
        val onAccent = "#FFFFFF"
        val onBg = if (dark) "#FFFFFF" else "#1A1A1A"

        ThemeState.replace(
            t.copy(
                primary = preset.color1,
                onPrimary = onAccent,
                primaryContainer = preset.color2,
                onPrimaryContainer = onAccent,
                inversePrimary = preset.color2,
                secondary = preset.color2,
                onSecondary = onAccent,
                secondaryContainer = preset.color1,
                onSecondaryContainer = onAccent,
                tertiary = preset.color2,
                onTertiary = onAccent,
                tertiaryContainer = preset.color1,
                onTertiaryContainer = onAccent,
                background = preset.bg,
                onBackground = onBg,
                surface = preset.bg,
                onSurface = onBg,
                surfaceVariant = preset.color1 + "22",
                onSurfaceVariant = onBg,
                surfaceTint = preset.color1,
                inverseSurface = onBg,
                inverseOnSurface = preset.bg,
                outline = preset.color1,
                outlineVariant = preset.color2,
                surfaceBright = preset.color2,
                surfaceDim = preset.bg,
                surfaceContainer = preset.color1 + "22",
                surfaceContainerHigh = preset.color2 + "22",
                surfaceContainerHighest = preset.color2,
                surfaceContainerLow = preset.bg,
                surfaceContainerLowest = preset.bg,
                keyboardSurface = preset.bg,
                keyboardSurfaceDim = preset.bg,
                keyboardContainer = preset.color1 + "22",
                keyboardContainerVariant = preset.color2,
                onKeyboardContainer = onBg,
                keyboardPress = preset.color1,
                keyboardContainerPressed = preset.color2,
                onKeyboardContainerPressed = onAccent,
            )
        )
    }
}
