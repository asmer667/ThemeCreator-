package com.futo.themecreator.data

import kotlinx.serialization.Serializable

/**
 * نموذج كامل لثيم FUTO Keyboard
 * يحتوي على 44 لون + إعدادات + أيقونات
 */
@Serializable
data class ThemeData(
    // ═══════════ معلومات أساسية ═══════════
    val name: String = "My Theme",
    val author: String = "Anonymous",
    val id: String = "com.example.mytheme",
    val version: Int = 1,
    val description: String = "Theme created with FUTO Theme Creator",

    // ═══════════ Background Image (behind keys) ═══════════
    val backgroundImageUri: String? = null,
    val backgroundImageOpacity: Float = 0.5f,
    val gradientStart: String? = null,
    val gradientEnd: String? = null,
    val gradientAngle: Float = 45f,
    val gradientMidColor: String? = null,
    val gradientBlur: Float = 0f,
    val gradientOpacity: Float = 1f,
    val gradientMode: String = "linear",
    val gradientStops: String = "0.0,0.5,1.0",
    // ─── خلفية الصورة ───
    val backgroundBlur: Float = 0f,
    val backgroundSaturation: Float = 1f,
    val backgroundBrightness: Float = 1f,

    // ═══════════ Font ═══════════
    /** اسم ملف الخط داخل مجلد fonts/ مثال: "Cairo-Regular.ttf" */
    val fontName: String? = null,

    // ═══════════ Custom Button Images ═══════════
    /**
     * خرائط اسم زر FUTO الرسمي → مسار/اسم الملف.
     * الأسماء الرسمية: Button-default.png, Button-default-press.png,
     * Button-default-dark.png, Button-default-press-dark.png,
     * Button-function.png, Button-function-pressed.png,
     * Button-function2.png, Button-function2-pressed.png,
     * Button-space-dark.png, Button-space-press.png,
     * Button-action.png, Button-action-press.png,
     * Button-alt1.png, Button-alt2.png, Button-alt3.png
     */
    val customButtonImages: Map<String, String> = emptyMap(),
    val customIconImages: Map<String, String> = emptyMap(),
    val backgroundActionBarOpacity: Float = 0.5f,

    // ═══════════ Options ═══════════
    val autoBorders: Boolean = true,
    val centerHints: Boolean = false,
    val roundedness: Float = 1.0f,
    val scaleText: Float = 1.0f,
    val scaleHints: Float = 1.0f,
    val weightText: Float = 400.0f,
    val weightHints: Float = 500.0f,

    // ═══════════ 44 Colors ═══════════
    // Primary Group (5)
    val primary: String = "#E86F73",
    val onPrimary: String = "#351114",
    val primaryContainer: String = "#F2A1A4",
    val onPrimaryContainer: String = "#351114",
    val inversePrimary: String = "#FFB7BA",

    // Secondary Group (4)
    val secondary: String = "#E86F73",
    val onSecondary: String = "#351114",
    val secondaryContainer: String = "#F2A1A4",
    val onSecondaryContainer: String = "#351114",

    // Tertiary Group (4)
    val tertiary: String = "#087CFF",
    val onTertiary: String = "#FFFFFF",
    val tertiaryContainer: String = "#063B7A",
    val onTertiaryContainer: String = "#FFFFFF",

    // Background Group (2)
    val background: String = "#000000",
    val onBackground: String = "#FFFFFF",

    // Surface Group (9)
    val surface: String = "#000000",
    val onSurface: String = "#FFFFFF",
    val surfaceVariant: String = "#101114",
    val onSurfaceVariant: String = "#FFFFFF",
    val surfaceTint: String = "#087CFF",
    val inverseSurface: String = "#FFFFFF",
    val inverseOnSurface: String = "#000000",
    val surfaceBright: String = "#15171A",
    val surfaceDim: String = "#000000",

    // Error Group (4)
    val error: String = "#FF4D5E",
    val onError: String = "#FFFFFF",
    val errorContainer: String = "#5A1018",
    val onErrorContainer: String = "#FFECEF",

    // Outline Group (2)
    val outline: String = "#087CFFFF",
    val outlineVariant: String = "#087CFF99",

    // Scrim (1)
    val scrim: String = "#000000",

    // Surface Containers (6)
    val surfaceContainer: String = "#0B0C0E",
    val surfaceContainerHigh: String = "#111317",
    val surfaceContainerHighest: String = "#15171A",
    val surfaceContainerLow: String = "#050608",
    val surfaceContainerLowest: String = "#000000",

    // Keyboard-specific (7)
    val keyboardSurface: String = "#000000",
    val keyboardSurfaceDim: String = "#000000",
    val keyboardContainer: String = "#101114",
    val keyboardContainerVariant: String = "#087CFF",
    val onKeyboardContainer: String = "#FFFFFF",
    val keyboardPress: String = "#087CFF",
    val keyboardContainerPressed: String = "#087CFF88",
    val onKeyboardContainerPressed: String = "#FFFFFF",
) {
    /** عدد الألوان الكلي */
    val colorCount: Int = 44

    /** يُرجع قائمة (اسم الحقل، القيمة) */
    fun allColors(): List<Pair<String, String>> = listOf(
        "primary" to primary,
        "on_primary" to onPrimary,
        "primary_container" to primaryContainer,
        "on_primary_container" to onPrimaryContainer,
        "inverse_primary" to inversePrimary,
        "secondary" to secondary,
        "on_secondary" to onSecondary,
        "secondary_container" to secondaryContainer,
        "on_secondary_container" to onSecondaryContainer,
        "tertiary" to tertiary,
        "on_tertiary" to onTertiary,
        "tertiary_container" to tertiaryContainer,
        "on_tertiary_container" to onTertiaryContainer,
        "background" to background,
        "on_background" to onBackground,
        "surface" to surface,
        "on_surface" to onSurface,
        "surface_variant" to surfaceVariant,
        "on_surface_variant" to onSurfaceVariant,
        "surface_tint" to surfaceTint,
        "inverse_surface" to inverseSurface,
        "inverse_on_surface" to inverseOnSurface,
        "error" to error,
        "on_error" to onError,
        "error_container" to errorContainer,
        "on_error_container" to onErrorContainer,
        "outline" to outline,
        "outline_variant" to outlineVariant,
        "scrim" to scrim,
        "surface_bright" to surfaceBright,
        "surface_dim" to surfaceDim,
        "surface_container" to surfaceContainer,
        "surface_container_high" to surfaceContainerHigh,
        "surface_container_highest" to surfaceContainerHighest,
        "surface_container_low" to surfaceContainerLow,
        "surface_container_lowest" to surfaceContainerLowest,
        "keyboard_surface" to keyboardSurface,
        "keyboard_surface_dim" to keyboardSurfaceDim,
        "keyboard_container" to keyboardContainer,
        "keyboard_container_variant" to keyboardContainerVariant,
        "on_keyboard_container" to onKeyboardContainer,
        "keyboard_press" to keyboardPress,
        "keyboard_container_pressed" to keyboardContainerPressed,
        "on_keyboard_container_pressed" to onKeyboardContainerPressed,
    )
}
