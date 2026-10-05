package com.futo.themecreator.data

/**
 * Preset colors - ready to apply
 */
object PresetColors {

    // ═══════════ SOLID DARK ═══════════
    val solidDark = listOf(
        SolidPreset(
            name = "Midnight Blue",
            emoji = "🌊",
            accent = "#00BFFF",
            bg = "#0A0A1A",
            surface = "#151728",
            container = "#1E2238",
        ),
        SolidPreset(
            name = "Deep Purple",
            emoji = "💜",
            accent = "#9C27B0",
            bg = "#150A1A",
            surface = "#231535",
            container = "#2D1A45",
        ),
        SolidPreset(
            name = "Forest Green",
            emoji = "🌲",
            accent = "#00C853",
            bg = "#0A1A0A",
            surface = "#0F2810",
            container = "#153520",
        ),
        SolidPreset(
            name = "Crimson Red",
            emoji = "🔴",
            accent = "#FF1744",
            bg = "#1A0A0A",
            surface = "#281010",
            container = "#351515",
        ),
        SolidPreset(
            name = "Amber Gold",
            emoji = "🟡",
            accent = "#FFB300",
            bg = "#1A150A",
            surface = "#282015",
            container = "#352A1A",
        ),
        SolidPreset(
            name = "Ocean Teal",
            emoji = "🌊",
            accent = "#00BCD4",
            bg = "#0A1A1A",
            surface = "#0F2828",
            container = "#153535",
        ),
        SolidPreset(
            name = "Slate Grey",
            emoji = "⚫",
            accent = "#B0BEC5",
            bg = "#121212",
            surface = "#1E1E1E",
            container = "#2A2A2A",
        ),
        SolidPreset(
            name = "Rose Pink",
            emoji = "🌸",
            accent = "#FF1493",
            bg = "#1A0A10",
            surface = "#28151D",
            container = "#351A25",
        ),
        SolidPreset(
            name = "Blood Orange",
            emoji = "🟠",
            accent = "#FF6D00",
            bg = "#1A0E0A",
            surface = "#281510",
            container = "#351A15",
        ),
        SolidPreset(
            name = "Lime Neon",
            emoji = "🟢",
            accent = "#C6FF00",
            bg = "#0F150A",
            surface = "#1A2515",
            container = "#253520",
        ),
    )

    // ═══════════ SOLID LIGHT ═══════════
    val solidLight = listOf(
        SolidPreset(
            name = "Sky Blue",
            emoji = "☁️",
            accent = "#0288D1",
            bg = "#F0F8FF",
            surface = "#E1F0FF",
            container = "#D0E5F5",
        ),
        SolidPreset(
            name = "Lavender",
            emoji = "💐",
            accent = "#7B1FA2",
            bg = "#F5F0FF",
            surface = "#EBE0FF",
            container = "#DDD0F0",
        ),
        SolidPreset(
            name = "Mint",
            emoji = "🌿",
            accent = "#00897B",
            bg = "#F0FFF5",
            surface = "#E0FFE8",
            container = "#C8F0D5",
        ),
        SolidPreset(
            name = "Peach",
            emoji = "🍑",
            accent = "#E64A19",
            bg = "#FFF5F0",
            surface = "#FFE5D5",
            container = "#F5D5C0",
        ),
        SolidPreset(
            name = "Lemon",
            emoji = "🍋",
            accent = "#F9A825",
            bg = "#FFFFF0",
            surface = "#FFF8D5",
            container = "#F5EBB5",
        ),
        SolidPreset(
            name = "Cloud",
            emoji = "☁️",
            accent = "#616161",
            bg = "#FAFAFA",
            surface = "#EEEEEE",
            container = "#E0E0E0",
        ),
        SolidPreset(
            name = "Blush",
            emoji = "🌸",
            accent = "#C2185B",
            bg = "#FFF0F5",
            surface = "#FFDDE8",
            container = "#F5C8D8",
        ),
        SolidPreset(
            name = "Ivory",
            emoji = "🤍",
            accent = "#5D4037",
            bg = "#FFFDF5",
            surface = "#F5EFD8",
            container = "#E8DCC0",
        ),
        SolidPreset(
            name = "Coral",
            emoji = "🪸",
            accent = "#FF5722",
            bg = "#FFF0EC",
            surface = "#FFDCD1",
            container = "#F5C5B5",
        ),
        SolidPreset(
            name = "Aqua",
            emoji = "💎",
            accent = "#00ACC1",
            bg = "#F0FCFF",
            surface = "#DDF5FA",
            container = "#C0E8F0",
        ),
    )

    // ═══════════ GRADIENT DARK ═══════════
    val gradientDark = listOf(
        GradientPreset(
            name = "Purple Blue",
            emoji = "🌌",
            color1 = "#6A1B9A",
            color2 = "#1565C0",
            bg = "#0A0515",
        ),
        GradientPreset(
            name = "Sunset Orange",
            emoji = "🌅",
            color1 = "#FF6F00",
            color2 = "#D81B60",
            bg = "#150A0A",
        ),
        GradientPreset(
            name = "Cyan Green",
            emoji = "🌊",
            color1 = "#00BCD4",
            color2 = "#00E676",
            bg = "#051415",
        ),
        GradientPreset(
            name = "Pink Purple",
            emoji = "🌸",
            color1 = "#E91E63",
            color2 = "#9C27B0",
            bg = "#150A15",
        ),
        GradientPreset(
            name = "Red Orange",
            emoji = "🔥",
            color1 = "#F44336",
            color2 = "#FF9800",
            bg = "#150A0A",
        ),
        GradientPreset(
            name = "Blue Teal",
            emoji = "💙",
            color1 = "#1976D2",
            color2 = "#00BCD4",
            bg = "#05101A",
        ),
    )

    // ═══════════ GRADIENT LIGHT ═══════════
    val gradientLight = listOf(
        GradientPreset(
            name = "Peach Sunset",
            emoji = "🌅",
            color1 = "#FFB74D",
            color2 = "#F06292",
            bg = "#FFF5F0",
        ),
        GradientPreset(
            name = "Sky Mint",
            emoji = "🌤️",
            color1 = "#4FC3F7",
            color2 = "#81C784",
            bg = "#F0FFF8",
        ),
        GradientPreset(
            name = "Lavender Pink",
            emoji = "💐",
            color1 = "#BA68C8",
            color2 = "#F48FB1",
            bg = "#FDF5FF",
        ),
        GradientPreset(
            name = "Sunny Yellow",
            emoji = "☀️",
            color1 = "#FFD54F",
            color2 = "#FF8A65",
            bg = "#FFFDF0",
        ),
        GradientPreset(
            name = "Ocean Breeze",
            emoji = "🌊",
            color1 = "#4DD0E1",
            color2 = "#7986CB",
            bg = "#F0FBFF",
        ),
        GradientPreset(
            name = "Mint Lime",
            emoji = "🌿",
            color1 = "#4DB6AC",
            color2 = "#AED581",
            bg = "#F5FFF5",
        ),
    )
}

data class SolidPreset(
    val name: String,
    val emoji: String,
    val accent: String,
    val bg: String,
    val surface: String,
    val container: String,
)

data class GradientPreset(
    val name: String,
    val emoji: String,
    val color1: String,
    val color2: String,
    val bg: String,
)
