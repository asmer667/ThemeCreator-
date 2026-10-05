package com.futo.themecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
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

    val outline = ColorUtils.parseColor(theme.outline)
    val keyRadius = (theme.roundedness * 10).dp

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .background(ColorUtils.parseColor(theme.keyboardSurface))
            .border(1.dp, outline.copy(alpha = 0.3f), RoundedCornerShape(20.dp))
    ) {
        // خلفية الصورة (خلف الأزرار)
        if (bgUri != null) {
            AsyncImage(
                model = bgUri,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = theme.backgroundImageOpacity,
                modifier = Modifier.matchParentSize()
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Row 1
            KeyRow(
                listOf("Q","W","E","R","T","Y","U","I","O","P"),
                theme, firstShape, keyRadius
            )
            Spacer(Modifier.height(5.dp))
            // Row 2
            KeyRow(
                listOf("A","S","D","F","G","H","J","K","L"),
                theme, firstShape, keyRadius
            )
            Spacer(Modifier.height(5.dp))
            // Row 3 (with shift and backspace)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                PreviewKey("⇧", theme, firstShape, keyRadius,
                    bgColor = ColorUtils.parseColor(theme.keyboardContainerVariant),
                    width = 42.dp)
                listOf("Z","X","C","V","B","N","M").forEach { l ->
                    PreviewKey(l, theme, firstShape, keyRadius)
                }
                PreviewKey("⌫", theme, firstShape, keyRadius,
                    bgColor = ColorUtils.parseColor(theme.keyboardContainerVariant),
                    width = 42.dp)
            }
            Spacer(Modifier.height(5.dp))
            // Row 4 (bottom bar)
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PreviewKey("?123", theme, firstShape, keyRadius,
                    bgColor = ColorUtils.parseColor(theme.keyboardContainerVariant),
                    width = 46.dp)
                PreviewKey("😊", theme, firstShape, keyRadius,
                    bgColor = ColorUtils.parseColor(theme.keyboardContainerVariant),
                    width = 40.dp)
                // Spacebar
                Box(
                    Modifier.width(130.dp).height(40.dp)
                        .clip(RoundedCornerShape(keyRadius))
                        .background(ColorUtils.parseColor(theme.keyboardContainer))
                        .border(1.dp, outline.copy(alpha = 0.4f), RoundedCornerShape(keyRadius)),
                    contentAlignment = Alignment.Center
                ) {
                    Text("Arabic",
                        fontSize = (12 * theme.scaleText).sp,
                        color = ColorUtils.parseColor(theme.onKeyboardContainer).copy(alpha = 0.7f))
                }
                PreviewKey(".", theme, firstShape, keyRadius, width = 32.dp)
                PreviewKey("↵", theme, firstShape, keyRadius,
                    bgColor = ColorUtils.parseColor(theme.primary),
                    textColor = ColorUtils.parseColor(theme.onPrimary),
                    width = 48.dp)
            }
        }
    }
}

@Composable
private fun KeyRow(
    letters: List<String>,
    theme: ThemeData,
    shape: String?,
    radius: androidx.compose.ui.unit.Dp,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {
        letters.forEach { l ->
            PreviewKey(l, theme, shape, radius)
        }
    }
}

@Composable
private fun PreviewKey(
    text: String,
    theme: ThemeData,
    shape: String?,
    radius: androidx.compose.ui.unit.Dp,
    bgColor: Color = ColorUtils.parseColor(theme.keyboardContainer),
    textColor: Color = ColorUtils.parseColor(theme.onKeyboardContainer),
    width: androidx.compose.ui.unit.Dp = 32.dp,
) {
    val interaction = remember { MutableInteractionSource() }
    val isPressed by interaction.collectIsPressedAsState()
    val kbPress = ColorUtils.parseColor(theme.keyboardPress)
    val outline = ColorUtils.parseColor(theme.outline)

    Box(
        modifier = Modifier
            .padding(horizontal = 2.dp)
            .width(width)
            .height(40.dp)
            .clip(RoundedCornerShape(radius))
            .background(if (isPressed) kbPress else bgColor)
            .then(
                if (theme.autoBorders)
                    Modifier.border(1.dp, outline.copy(alpha = 0.4f), RoundedCornerShape(radius))
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
                modifier = Modifier.fillMaxSize().padding(3.dp)
            )
        }
        Text(
            text,
            fontSize = (13 * theme.scaleText).sp,
            fontWeight = FontWeight(theme.weightText.toInt().coerceIn(100, 900)),
            color = textColor
        )
    }
}
