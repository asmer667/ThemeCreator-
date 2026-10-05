package com.futo.themecreator.data

/**
 * أشكال جاهزة للاستدارة
 */
data class ShapePreset(
    val name: String,
    val emoji: String,
    val roundedness: Float,
)

val shapePresets: List<ShapePreset> = listOf(
    ShapePreset("حاد (Square)", "▢", 0.0f),
    ShapePreset("خفيف", "▢", 0.25f),
    ShapePreset("متوسط", "▢", 0.5f),
    ShapePreset("دائري", "◯", 0.75f),
    ShapePreset("كامل (Round)", "⬤", 1.0f),
)

/**
 * أحجام خط جاهزة
 */
data class FontSizePreset(
    val name: String,
    val scale: Float,
)

val fontSizePresets: List<FontSizePreset> = listOf(
    FontSizePreset("صغير", 0.8f),
    FontSizePreset("عادي", 1.0f),
    FontSizePreset("كبير", 1.2f),
    FontSizePreset("ضخم", 1.5f),
)

/**
 * أوزان النص
 */
data class FontWeightPreset(
    val name: String,
    val weight: Float,
)

val fontWeightPresets: List<FontWeightPreset> = listOf(
    FontWeightPreset("Thin (100)", 100f),
    FontWeightPreset("Light (300)", 300f),
    FontWeightPreset("Regular (400)", 400f),
    FontWeightPreset("Medium (500)", 500f),
    FontWeightPreset("Bold (700)", 700f),
    FontWeightPreset("Black (900)", 900f),
)
