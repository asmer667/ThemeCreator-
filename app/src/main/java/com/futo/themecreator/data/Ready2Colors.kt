package com.futo.themecreator.data

object Ready2Colors {

    val solidDark = listOf(
        Ready2Preset("Midnight","🌌","#000000",null,null),
        Ready2Preset("Deep Blue","🌊","#0A1428",null,null),
        Ready2Preset("Dark Forest","🌲","#0A1A0F",null,null),
        Ready2Preset("Night Purple","💜","#0F0518",null,null),
        Ready2Preset("Dark Crimson","🔴","#1A0505",null,null),
        Ready2Preset("Coal","⚫","#0F0F0F",null,null),
        Ready2Preset("Deep Sea","🐋","#050F15",null,null),
        Ready2Preset("Dark Amber","🟡","#1A1205",null,null),
    )

    val solidLight = listOf(
        Ready2Preset("Snow","🤍","#FFFFFF",null,null),
        Ready2Preset("Sky","☁️","#F0F8FF",null,null),
        Ready2Preset("Mint","🌿","#F0FFF5",null,null),
        Ready2Preset("Lavender","💐","#F5F0FF",null,null),
        Ready2Preset("Blush","🌸","#FFF0F5",null,null),
        Ready2Preset("Cream","🍦","#FFFDF5",null,null),
        Ready2Preset("Cloud","☁️","#FAFAFA",null,null),
        Ready2Preset("Peach","🍑","#FFF5F0",null,null),
    )

    val gradientDark = listOf(
        Ready2Preset("Purple Blue","🌌","#0A0515","#6A1B9A","#1565C0"),
        Ready2Preset("Sunset","🌅","#150A0A","#FF6F00","#D81B60"),
        Ready2Preset("Neon Cyber","⚡","#0A0A1A","#FF00FF","#00FFFF"),
        Ready2Preset("Fire Ice","🔥","#0A0A15","#FF1744","#00E5FF"),
        Ready2Preset("Deep Ocean","🌊","#050F15","#006064","#1A237E"),
        Ready2Preset("Royal","👑","#150A1A","#6A1B9A","#FFD700"),
        Ready2Preset("Cyber Green","💚","#051415","#00BCD4","#00E676"),
        Ready2Preset("Twilight","🌆","#150A1A","#E91E63","#9C27B0"),
    )

    val gradientLight = listOf(
        Ready2Preset("Peach Sunset","🌅","#FFF5F0","#FFB74D","#F06292"),
        Ready2Preset("Sky Mint","🌤️","#F0FFF8","#4FC3F7","#81C784"),
        Ready2Preset("Lavender Pink","💐","#FDF5FF","#BA68C8","#F48FB1"),
        Ready2Preset("Sunny","☀️","#FFFDF0","#FFD54F","#FF8A65"),
        Ready2Preset("Ocean Breeze","🌊","#F0FBFF","#4DD0E1","#7986CB"),
        Ready2Preset("Spring","🌸","#FFF5F0","#FFAB91","#F8BBD0"),
        Ready2Preset("Cotton","☁️","#F5F5FF","#B39DDB","#81D4FA"),
        Ready2Preset("Fresh","🌱","#F1F8E9","#43A047","#C6FF00"),
    )
}

data class Ready2Preset(
    val name: String,
    val emoji: String,
    val bg: String,
    val color1: String?,
    val color2: String?,
)
