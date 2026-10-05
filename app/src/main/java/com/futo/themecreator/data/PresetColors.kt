package com.futo.themecreator.data

object PresetColors {
    val solidDark = listOf(
        SolidPreset("Midnight Blue","🌊","#00BFFF","#0A0A1A","#151728","#1E2238"),
        SolidPreset("Deep Purple","💜","#9C27B0","#150A1A","#231535","#2D1A45"),
        SolidPreset("Forest Green","🌲","#00C853","#0A1A0A","#0F2810","#153520"),
        SolidPreset("Crimson Red","🔴","#FF1744","#1A0A0A","#281010","#351515"),
        SolidPreset("Amber Gold","🟡","#FFB300","#1A150A","#282015","#352A1A"),
        SolidPreset("Ocean Teal","🌊","#00BCD4","#0A1A1A","#0F2828","#153535"),
        SolidPreset("Slate Grey","⚫","#B0BEC5","#121212","#1E1E1E","#2A2A2A"),
        SolidPreset("Rose Pink","🌸","#FF1493","#1A0A10","#28151D","#351A25"),
        SolidPreset("Blood Orange","🟠","#FF6D00","#1A0E0A","#281510","#351A15"),
        SolidPreset("Lime Neon","🟢","#C6FF00","#0F150A","#1A2515","#253520"),
    )
    val solidLight = listOf(
        SolidPreset("Sky Blue","☁️","#0288D1","#F0F8FF","#E1F0FF","#D0E5F5"),
        SolidPreset("Lavender","💐","#7B1FA2","#F5F0FF","#EBE0FF","#DDD0F0"),
        SolidPreset("Mint","🌿","#00897B","#F0FFF5","#E0FFE8","#C8F0D5"),
        SolidPreset("Peach","🍑","#E64A19","#FFF5F0","#FFE5D5","#F5D5C0"),
        SolidPreset("Lemon","🍋","#F9A825","#FFFFF0","#FFF8D5","#F5EBB5"),
        SolidPreset("Cloud","☁️","#616161","#FAFAFA","#EEEEEE","#E0E0E0"),
        SolidPreset("Blush","🌸","#C2185B","#FFF0F5","#FFDDE8","#F5C8D8"),
        SolidPreset("Ivory","🤍","#5D4037","#FFFDF5","#F5EFD8","#E8DCC0"),
        SolidPreset("Coral","🪸","#FF5722","#FFF0EC","#FFDCD1","#F5C5B5"),
        SolidPreset("Aqua","💎","#00ACC1","#F0FCFF","#DDF5FA","#C0E8F0"),
    )
    val gradientDark = listOf(
        GradientPreset("Purple Blue","🌌","#6A1B9A","#1565C0","#0A0515"),
        GradientPreset("Sunset Orange","🌅","#FF6F00","#D81B60","#150A0A"),
        GradientPreset("Cyan Green","🌊","#00BCD4","#00E676","#051415"),
        GradientPreset("Pink Purple","🌸","#E91E63","#9C27B0","#150A15"),
        GradientPreset("Red Orange","🔥","#F44336","#FF9800","#150A0A"),
        GradientPreset("Blue Teal","💙","#1976D2","#00BCD4","#05101A"),
    )
    val gradientLight = listOf(
        GradientPreset("Peach Sunset","🌅","#FFB74D","#F06292","#FFF5F0"),
        GradientPreset("Sky Mint","🌤️","#4FC3F7","#81C784","#F0FFF8"),
        GradientPreset("Lavender Pink","💐","#BA68C8","#F48FB1","#FDF5FF"),
        GradientPreset("Sunny Yellow","☀️","#FFD54F","#FF8A65","#FFFDF0"),
        GradientPreset("Ocean Breeze","🌊","#4DD0E1","#7986CB","#F0FBFF"),
        GradientPreset("Mint Lime","🌿","#4DB6AC","#AED581","#F5FFF5"),
    )
}

data class SolidPreset(val name: String, val emoji: String, val accent: String,
    val bg: String, val surface: String, val container: String)

data class GradientPreset(val name: String, val emoji: String, val color1: String,
    val color2: String, val bg: String)
