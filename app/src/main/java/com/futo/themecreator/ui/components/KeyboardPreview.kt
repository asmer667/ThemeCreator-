package com.futo.themecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themecreator.data.ThemeData
import com.futo.themecreator.utils.ColorUtils

/**
 * معاينة كاملة للكيبورد مع الثيم المطبق
 */
@Composable
fun KeyboardPreview(theme: ThemeData) {
    val bg = ColorUtils.parseColor(theme.background)
    val kbSurface = ColorUtils.parseColor(theme.keyboardSurface)
    val kbContainer = ColorUtils.parseColor(theme.keyboardContainer)
    val onKbContainer = ColorUtils.parseColor(theme.onKeyboardContainer)
    val kbPress = ColorUtils.parseColor(theme.keyboardPress)
    val kbVariant = ColorUtils.parseColor(theme.keyboardContainerVariant)
    val primary = ColorUtils.parseColor(theme.primary)
    val onPrimary = ColorUtils.parseColor(theme.onPrimary)
    val outline = ColorUtils.parseColor(theme.outline)

    val radius = (theme.roundedness * 8).dp
    val keyRadius = (theme.roundedness * 12).dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(bg, RoundedCornerShape(radius))
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ═══════ صف الأرقام ═══════
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("1", "2", "3", "4", "5", "6", "7", "8", "9", "0").forEach { num ->
                PreviewKey(
                    text = num,
                    theme = theme,
                    bgColor = kbContainer,
                    textColor = onKbContainer,
                    outline = outline,
                    radius = keyRadius,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ═══════ صف QWERTY ═══════
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("Q", "W", "E", "R", "T", "Y", "U", "I", "O", "P").forEach { letter ->
                PreviewKey(
                    text = letter,
                    theme = theme,
                    bgColor = kbContainer,
                    textColor = onKbContainer,
                    outline = outline,
                    radius = keyRadius,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ═══════ صف ASDF ═══════
        Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
            listOf("A", "S", "D", "F", "G", "H", "J", "K", "L").forEach { letter ->
                PreviewKey(
                    text = letter,
                    theme = theme,
                    bgColor = kbContainer,
                    textColor = onKbContainer,
                    outline = outline,
                    radius = keyRadius,
                )
            }
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ═══════ صف ZXCV + Shift ═══════
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Shift (functional)
            PreviewKey(
                text = "⇧",
                theme = theme,
                bgColor = kbVariant,
                textColor = onKbContainer,
                outline = outline,
                radius = keyRadius,
                width = 42.dp
            )
            listOf("Z", "X", "C", "V", "B", "N", "M").forEach { letter ->
                PreviewKey(
                    text = letter,
                    theme = theme,
                    bgColor = kbContainer,
                    textColor = onKbContainer,
                    outline = outline,
                    radius = keyRadius,
                )
            }
            // Backspace
            PreviewKey(
                text = "⌫",
                theme = theme,
                bgColor = kbContainer,
                textColor = onKbContainer,
                outline = outline,
                radius = keyRadius,
                width = 42.dp
            )
        }

        Spacer(modifier = Modifier.height(6.dp))

        // ═══════ صف سفلي ═══════
        Row(
            horizontalArrangement = Arrangement.spacedBy(4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ?123
            PreviewKey(
                text = "?123",
                theme = theme,
                bgColor = kbContainer,
                textColor = onKbContainer,
                outline = outline,
                radius = keyRadius,
                width = 42.dp
            )
            // Emoji
            PreviewKey(
                text = "😊",
                theme = theme,
                bgColor = kbContainer,
                textColor = onKbContainer,
                outline = outline,
                radius = keyRadius,
                width = 42.dp
            )
            // Space
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(42.dp)
                    .clip(RoundedCornerShape(keyRadius))
                    .background(kbContainer)
                    .border(1.dp, outline.copy(alpha = 0.3f), RoundedCornerShape(keyRadius)),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    "Arabic",
                    fontSize = (12 * theme.scaleText).sp,
                    color = onKbContainer.copy(alpha = 0.7f)
                )
            }
            // . 
            PreviewKey(
                text = ".",
                theme = theme,
                bgColor = kbContainer,
                textColor = onKbContainer,
                outline = outline,
                radius = keyRadius,
                width = 42.dp
            )
            // Enter (Action)
            PreviewKey(
                text = "↵",
                theme = theme,
                bgColor = primary,
                textColor = onPrimary,
                outline = outline,
                radius = keyRadius,
                width = 42.dp
            )
        }
    }
}

@Composable
private fun PreviewKey(
    text: String,
    theme: ThemeData,
    bgColor: Color,
    textColor: Color,
    outline: Color,
    radius: androidx.compose.ui.unit.Dp,
    width: androidx.compose.ui.unit.Dp = 30.dp,
) {
    Box(
        modifier = Modifier
            .width(width)
            .height(42.dp)
            .clip(RoundedCornerShape(radius))
            .background(bgColor)
            .then(
                if (theme.autoBorders) {
                    Modifier.border(1.dp, outline.copy(alpha = 0.4f), RoundedCornerShape(radius))
                } else {
                    Modifier
                }
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text,
            fontSize = (14 * theme.scaleText).sp,
            fontWeight = FontWeight(theme.weightText.toInt()),
            color = textColor
        )
    }
}
