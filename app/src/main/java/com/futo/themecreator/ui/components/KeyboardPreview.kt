package com.futo.themecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
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
 * معاينة كاملة للكيبورد — الألوان مفصلة لكل صف
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

    val radius = (theme.roundedness * 6).dp
    val keyRadius = (theme.roundedness * 10).dp

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(kbSurface, RoundedCornerShape(radius))
            .padding(6.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // ═══════ صف الأرقام ═══════
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            listOf("1","2","3","4","5","6","7","8","9","0").forEach { num ->
                PreviewKey(num, theme, kbContainer, onKbContainer, outline, keyRadius)
            }
        }
        Spacer(Modifier.height(4.dp))

        // ═══════ QWERTY ═══════
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            listOf("Q","W","E","R","T","Y","U","I","O","P").forEach { l ->
                PreviewKey(l, theme, kbContainer, onKbContainer, outline, keyRadius)
            }
        }
        Spacer(Modifier.height(4.dp))

        // ═══════ ASDF ═══════
        Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
            listOf("A","S","D","F","G","H","J","K","L").forEach { l ->
                PreviewKey(l, theme, kbContainer, onKbContainer, outline, keyRadius)
            }
        }
        Spacer(Modifier.height(4.dp))

        // ═══════ ZXCV + Shift + Backspace ═══════
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            PreviewKey("⇧", theme, kbVariant, onKbContainer, outline, keyRadius, width = 38.dp)
            listOf("Z","X","C","V","B","N","M").forEach { l ->
                PreviewKey(l, theme, kbContainer, onKbContainer, outline, keyRadius)
            }
            PreviewKey("⌫", theme, kbVariant, onKbContainer, outline, keyRadius, width = 38.dp)
        }
        Spacer(Modifier.height(4.dp))

        // ═══════ الصف السفلي ═══════
        Row(
            horizontalArrangement = Arrangement.spacedBy(3.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // ?123
            PreviewKey("?123", theme, kbVariant, onKbContainer, outline, keyRadius, width = 40.dp)
            // Emoji
            PreviewKey("😊", theme, kbVariant, onKbContainer, outline, keyRadius, width = 40.dp)
            // Spacebar
            Box(
                modifier = Modifier
                    .width(120.dp)
                    .height(38.dp)
                    .clip(RoundedCornerShape(keyRadius))
                    .background(kbContainer)
                    .then(
                        if (theme.autoBorders)
                            Modifier.border(1.dp, outline.copy(alpha=0.4f), RoundedCornerShape(keyRadius))
                        else Modifier
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text("Arabic",
                    fontSize = (11 * theme.scaleText).sp,
                    color = onKbContainer.copy(alpha = 0.7f))
            }
            // .
            PreviewKey(".", theme, kbContainer, onKbContainer, outline, keyRadius, width = 30.dp)
            // Enter
            PreviewKey("↵", theme, primary, onPrimary, outline, keyRadius, width = 44.dp)
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
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val kbPress = ColorUtils.parseColor(theme.keyboardPress)

    val effectiveBg = if (isPressed) kbPress else bgColor

    Box(
        modifier = Modifier
            .width(width)
            .height(38.dp)
            .clip(RoundedCornerShape(radius))
            .background(effectiveBg)
            .then(
                if (theme.autoBorders)
                    Modifier.border(1.dp, outline.copy(alpha=0.4f), RoundedCornerShape(radius))
                else Modifier
            )
            .clickable(interactionSource = interaction, indication = null) {},
        contentAlignment = Alignment.Center
    ) {
        Text(text,
            fontSize = (13 * theme.scaleText).sp,
            fontWeight = FontWeight(theme.weightText.toInt().coerceIn(100, 900)),
            color = textColor)
    }
}
