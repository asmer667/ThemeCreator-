package com.futo.themecreator.data

/**
 * مجموعة المفاتيح في لوحة المفاتيح — تُستخدم لتخصيص شكل كل مجموعة.
 */
enum class KeyGroup {
    TOP_ROWS,     // صف الأرقام و ⇧ / ⌫
    LETTERS,      // صفوف الحروف
    BOTTOM_ROWS,  // ?123 / 😊 / مسافة / . / ↵
}

/**
 * إعدادات شكل مجموعة المفاتيح — تُخزَّن لكل KeyGroup.
 */
data class ShapeConfig(
    val shapeId: String = "rounded",
    val rotation: Float = 0f,
    val sharpness: Float = 0.5f,
    val tilt: Float = 0f,
    val filled: Boolean = true,
    val borderWidth: Float = 4f,
    val widthScale: Float = 1.0f,   // 0.5f .. 1.5f
    val heightScale: Float = 1.0f,  // 0.5f .. 1.5f
)
