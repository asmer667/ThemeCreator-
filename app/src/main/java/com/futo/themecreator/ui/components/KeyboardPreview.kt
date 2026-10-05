package com.futo.themecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.futo.themecreator.data.ThemeData
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.utils.ColorUtils

@Composable
fun KeyboardPreview(theme: ThemeData) {
    val bgUri = ThemeState.backgroundImageUri
    val shapes3D = ThemeState.shapes3D
    val firstShape = shapes3D.firstOrNull()

    // الألوان
    val kbSurface = ColorUtils.parseColor(theme.keyboardSurface)
    val kbContainer = ColorUtils.parseColor(theme.keyboardContainer)
    val kbVariant = ColorUtils.parseColor(theme.keyboardContainerVariant)
    val onKb = ColorUtils.parseColor(theme.onKeyboardContainer)
    val primary = ColorUtils.parseColor(theme.primary)
    val onPrimary = ColorUtils.parseColor(theme.onPrimary)
    val outline = ColorUtils.parseColor(theme.outline)

    val keyRadius = (theme.roundedness * 10).dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(kbSurface)
            .border(2.dp, outline.copy(alpha = 0.4f), RoundedCornerShape(18.dp))
    ) {
        // خلفية الصورة خلف الأزرار
        if (bgUri != null) {
            AsyncImage(
                model = bgUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = theme.backgroundImageOpacity,
                modifier = Modifier.matchParentSize()
            )
        }

        // محتوى الكيبورد
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            // ═══════ الصف 0: الأرقام ═══════
            KeyboardRow(
                keys = listOf(
                    KeySpec("1"), KeySpec("2"), KeySpec("3"),
                    KeySpec("4"), KeySpec("5"), KeySpec("6"),
                    KeySpec("7"), KeySpec("8"), KeySpec("9"),
                    KeySpec("0")
                ),
                theme = theme, shape = firstShape, radius = keyRadius,
                bgColor = kbContainer, textColor = onKb,
                keyWidth = 32.dp
            )

            Spacer(Modifier.height(6.dp))

            // ═══════ الصف 1: QWERTY (مع إزاحة طفيفة) ═══════
            KeyboardRow(
                keys = listOf(
                    KeySpec("Q"), KeySpec("W"), KeySpec("E"),
                    KeySpec("R"), KeySpec("T"), KeySpec("Y"),
                    KeySpec("U"), KeySpec("I"), KeySpec("O"),
                    KeySpec("P")
                ),
                theme = theme, shape = firstShape, radius = keyRadius,
                bgColor = kbContainer, textColor = onKb,
                keyWidth = 32.dp
            )

            Spacer(Modifier.height(6.dp))

            // ═══════ الصف 2: ASDF (مع إزاحة) ═══════
            Row(
                Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("A","S","D","F","G","H","J","K","L").forEach { l ->
                    PreviewKey(l, theme, firstShape, keyRadius, kbContainer, onKb)
                }
            }

            Spacer(Modifier.height(6.dp))

            // ═══════ الصف 3: Shift + ZXCV + Backspace ═══════
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Shift
                PreviewKey(
                    "⇧", theme, firstShape, keyRadius,
                    bgColor = kbVariant,
                    textColor = onKb,
                    width = 46.dp
                )
                listOf("Z","X","C","V","B","N","M").forEach { l ->
                    PreviewKey(l, theme, firstShape, keyRadius, kbContainer, onKb)
                }
                // Backspace
                PreviewKey(
                    "⌫", theme, firstShape, keyRadius,
                    bgColor = kbVariant,
                    textColor = onKb,
                    width = 46.dp
                )
            }

            Spacer(Modifier.height(6.dp))

            // ═══════ الصف 4: ?123 | Emoji | Space | . | Enter ═══════
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                // ?123
                PreviewKey(
                    "?123", theme, firstShape, keyRadius,
                    bgColor = kbVariant, textColor = onKb,
                    width = 48.dp, fontSize = 10
                )
                Spacer(Modifier.width(3.dp))
                // Emoji
                PreviewKey(
                    "😊", theme, firstShape, keyRadius,
                    bgColor = kbVariant, textColor = onKb,
                    width = 40.dp
                )
                Spacer(Modifier.width(3.dp))
                // Space (يأخذ معظم العرض)
                Box(
                    Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(keyRadius))
                        .background(kbContainer)
                        .then(
                            if (theme.autoBorders)
                                Modifier.border(1.dp, outline.copy(alpha = 0.4f), RoundedCornerShape(keyRadius))
                            else Modifier
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        "مسافة",
                        fontSize = (11 * theme.scaleText).sp,
                        color = onKb.copy(alpha = 0.7f)
                    )
                }
                Spacer(Modifier.width(3.dp))
                // .
                PreviewKey(
                    ".", theme, firstShape, keyRadius,
                    bgColor = kbContainer, textColor = onKb,
                    width = 32.dp
                )
                Spacer(Modifier.width(3.dp))
                // Enter
                PreviewKey(
                    "↵", theme, firstShape, keyRadius,
                    bgColor = primary, textColor = onPrimary,
                    width = 52.dp
                )
            }
        }
    }
}

// ═════════════════════════════════════════
// Helper composables
// ═════════════════════════════════════════

data class KeySpec(val label: String, val width: androidx.compose.ui.unit.Dp = 32.dp)

@Composable
private fun KeyboardRow(
    keys: List<KeySpec>,
    theme: ThemeData,
    shape: String?,
    radius: androidx.compose.ui.unit.Dp,
    bgColor: Color,
    textColor: Color,
    keyWidth: androidx.compose.ui.unit.Dp,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        keys.forEach { key ->
            PreviewKey(
                key.label, theme, shape, radius,
                bgColor = bgColor, textColor = textColor,
                width = keyWidth
            )
        }
    }
}

@Composable
private fun PreviewKey(
    text: String,
    theme: ThemeData,
    shape: String?,
    radius: androidx.compose.ui.unit.Dp,
    bgColor: Color,
    textColor: Color,
    width: androidx.compose.ui.unit.Dp = 32.dp,
    fontSize: Int = 13,
) {
    val outline = ColorUtils.parseColor(theme.outline)

    Box(
        Modifier
            .padding(horizontal = 2.dp)
            .width(width)
            .height(40.dp)
            .clip(RoundedCornerShape(radius))
            .background(bgColor)
            .then(
                if (theme.autoBorders)
                    Modifier.border(1.dp, outline.copy(alpha = 0.35f), RoundedCornerShape(radius))
                else Modifier
            ),
        contentAlignment = Alignment.Center
    ) {
        // شكل 3D خلف الزر
        if (shape != null) {
            AsyncImage(
                model = "file:///android_asset/shapes_3d/$shape",
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize().padding(2.dp)
            )
        }
        Text(
            text,
            fontSize = (fontSize * theme.scaleText).sp,
            fontWeight = FontWeight(theme.weightText.toInt().coerceIn(100, 900)),
            color = textColor,
            textAlign = TextAlign.Center
        )
    }
}
