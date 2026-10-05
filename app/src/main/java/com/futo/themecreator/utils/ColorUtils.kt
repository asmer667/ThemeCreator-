package com.futo.themecreator.utils

import androidx.compose.ui.graphics.Color

object ColorUtils {

    /** تحويل hex string إلى Compose Color */
    fun parseColor(hex: String): Color {
        return try {
            val clean = hex.removePrefix("#").trim()
            val argb = when (clean.length) {
                3 -> {
                    val r = clean[0]
                    val g = clean[1]
                    val b = clean[2]
                    "FF$r$r$g$g$b$b".toLong(16)
                }
                6 -> "FF$clean".toLong(16)
                8 -> clean.toLong(16)
                else -> 0xFF000000
            }
            Color(argb)
        } catch (e: Exception) {
            Color.Black
        }
    }

    /** تحويل Compose Color إلى hex string */
    fun toHex(color: Color): String {
        val argb = color.value.toLong()
        val a = ((argb shr 56) and 0xFF).toInt()
        val r = ((argb shr 48) and 0xFF).toInt()
        val g = ((argb shr 40) and 0xFF).toInt()
        val b = ((argb shr 32) and 0xFF).toInt()
        return if (a == 255) {
            String.format("#%02X%02X%02X", r, g, b)
        } else {
            String.format("#%02X%02X%02X%02X", a, r, g, b)
        }
    }

    /** فئات الألوان الـ 44 */
    val colorCategories: List<ColorCategory> = listOf(
        ColorCategory(
            name = "🎯 الألوان الأساسية (Primary)",
            keys = listOf(
                "primary" to "اللون الأساسي",
                "on_primary" to "نص على الأساسي",
                "primary_container" to "حاوية الأساسي",
                "on_primary_container" to "نص على الحاوية",
                "inverse_primary" to "الأساسي المعكوس",
            )
        ),
        ColorCategory(
            name = "🎨 الألوان الثانوية (Secondary)",
            keys = listOf(
                "secondary" to "الثانوي",
                "on_secondary" to "نص على الثانوي",
                "secondary_container" to "حاوية الثانوي",
                "on_secondary_container" to "نص على حاوية الثانوي",
            )
        ),
        ColorCategory(
            name = "🎭 الألوان الثلاثية (Tertiary)",
            keys = listOf(
                "tertiary" to "الثلاثي",
                "on_tertiary" to "نص على الثلاثي",
                "tertiary_container" to "حاوية الثلاثي",
                "on_tertiary_container" to "نص على حاوية الثلاثي",
            )
        ),
        ColorCategory(
            name = "🖼️ الخلفية (Background)",
            keys = listOf(
                "background" to "الخلفية",
                "on_background" to "نص على الخلفية",
            )
        ),
        ColorCategory(
            name = "⬜ السطح (Surface)",
            keys = listOf(
                "surface" to "السطح",
                "on_surface" to "نص على السطح",
                "surface_variant" to "متغير السطح",
                "on_surface_variant" to "نص على متغير السطح",
                "surface_tint" to "صبغة السطح",
                "inverse_surface" to "السطح المعكوس",
                "inverse_on_surface" to "نص على السطح المعكوس",
                "surface_bright" to "سطح ساطع",
                "surface_dim" to "سطح خافت",
            )
        ),
        ColorCategory(
            name = "🟥 الأخطاء (Error)",
            keys = listOf(
                "error" to "الخطأ",
                "on_error" to "نص على الخطأ",
                "error_container" to "حاوية الخطأ",
                "on_error_container" to "نص على حاوية الخطأ",
            )
        ),
        ColorCategory(
            name = "⭕ الحدود (Outline)",
            keys = listOf(
                "outline" to "الحدود",
                "outline_variant" to "متغير الحدود",
                "scrim" to "الغشاء",
            )
        ),
        ColorCategory(
            name = "📦 حاويات السطح",
            keys = listOf(
                "surface_container" to "حاوية السطح",
                "surface_container_high" to "حاوية سطح عالية",
                "surface_container_highest" to "حاوية سطح الأعلى",
                "surface_container_low" to "حاوية سطح منخفضة",
                "surface_container_lowest" to "حاوية سطح الأدنى",
            )
        ),
        ColorCategory(
            name = "⌨️ ألوان الكيبورد",
            keys = listOf(
                "keyboard_surface" to "سطح الكيبورد",
                "keyboard_surface_dim" to "سطح الكيبورد خافت",
                "keyboard_container" to "حاوية الكيبورد (الأزرار)",
                "keyboard_container_variant" to "حاوية متغيرة",
                "on_keyboard_container" to "نص على الأزرار",
                "keyboard_press" to "لون الضغط",
                "keyboard_container_pressed" to "زر مضغوط",
                "on_keyboard_container_pressed" to "نص مضغوط",
            )
        ),
    )
}

data class ColorCategory(
    val name: String,
    val keys: List<Pair<String, String>>,
)
