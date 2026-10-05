package com.futo.themecreator.data

/**
 * ثيمات جاهزة للبدء
 */
object Presets {

    val NeonBlue = ThemeData(
        name = "Neon Blue",
        id = "com.futo.creator.neonblue",
        primary = "#00BFFF",
        onPrimary = "#FFFFFF",
        background = "#0A0A0A",
        onBackground = "#FFFFFF",
        keyboardSurface = "#0A0A0A",
        keyboardContainer = "#1A1A1A",
        onKeyboardContainer = "#00BFFF",
        keyboardPress = "#00BFFF",
    )

    val NeonPink = ThemeData(
        name = "Neon Pink",
        id = "com.futo.creator.neonpink",
        primary = "#FF1493",
        onPrimary = "#FFFFFF",
        background = "#1A0011",
        onBackground = "#FFE0EE",
        keyboardSurface = "#1A0011",
        keyboardContainer = "#2D001E",
        onKeyboardContainer = "#FF1493",
        keyboardPress = "#FF1493",
    )

    val EmeraldDark = ThemeData(
        name = "Emerald Dark",
        id = "com.futo.creator.emeralddark",
        primary = "#00C853",
        onPrimary = "#000000",
        background = "#001A0A",
        onBackground = "#B9F6CA",
        keyboardSurface = "#001A0A",
        keyboardContainer = "#003D1A",
        onKeyboardContainer = "#00C853",
        keyboardPress = "#00C853",
    )

    val all: List<ThemeData> = listOf(NeonBlue, NeonPink, EmeraldDark)
}
