package com.futo.themecreator.ui.components

import android.graphics.Color as AndroidColor
import androidx.compose.foundation.Image
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
import com.futo.themecreator.data.*
import com.futo.themecreator.utils.ColorUtils

@Composable
fun KeyboardPreview(theme: ThemeData) {
    val bgUri = ThemeState.backgroundImageUri
    val icons = ThemeState.icons
    val groupShapes = ThemeState.keyGroupShapes

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
        if (bgUri != null) {
            AsyncImage(
                model = bgUri, contentDescription = null,
                contentScale = ContentScale.Crop,
                alpha = theme.backgroundImageOpacity,
                modifier = Modifier.matchParentSize()
            )
        }

        Column(
            modifier = Modifier.fillMaxWidth().padding(horizontal = 4.dp, vertical = 6.dp)
        ) {
            KeyRow(
                listOf("1","2","3","4","5","6","7","8","9","0"),
                theme, icons["button.png"],
                groupShapes[KeyGroup.TOP_ROWS], kbContainer,
                keyRadius, onKb, 32.dp
            )
            Spacer(Modifier.height(6.dp))
            KeyRow(
                listOf("Q","W","E","R","T","Y","U","I","O","P"),
                theme, icons["button.png"],
                groupShapes[KeyGroup.LETTERS], kbContainer,
                keyRadius, onKb, 32.dp
            )
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.fillMaxWidth().padding(start = 16.dp, end = 16.dp),
                horizontalArrangement = Arrangement.Center
            ) {
                listOf("A","S","D","F","G","H","J","K","L").forEach { l ->
                    PreviewKey(l, theme, icons["button.png"],
                        groupShapes[KeyGroup.LETTERS], kbContainer, keyRadius, onKb)
                }
            }
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PreviewKey("⇧", theme, icons["functional_button.png"],
                    groupShapes[KeyGroup.TOP_ROWS], kbVariant, keyRadius, onKb, width = 46.dp)
                listOf("Z","X","C","V","B","N","M").forEach { l ->
                    PreviewKey(l, theme, icons["button.png"],
                        groupShapes[KeyGroup.LETTERS], kbContainer, keyRadius, onKb)
                }
                PreviewKey("⌫", theme, icons["functional_button.png"],
                    groupShapes[KeyGroup.TOP_ROWS], kbVariant, keyRadius, onKb, width = 46.dp)
            }
            Spacer(Modifier.height(6.dp))
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                PreviewKey("?123", theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbVariant, keyRadius, onKb,
                    width = 48.dp, fontSize = 10)
                Spacer(Modifier.width(3.dp))
                PreviewKey("😊", theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbVariant, keyRadius, onKb, width = 40.dp)
                Spacer(Modifier.width(3.dp))
                // Spacebar
                SpacebarKey(theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbContainer, keyRadius, onKb)
                Spacer(Modifier.width(3.dp))
                PreviewKey(".", theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbContainer, keyRadius, onKb, width = 32.dp)
                Spacer(Modifier.width(3.dp))
                PreviewKey("↵", theme, icons["action_button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], primary, keyRadius, onPrimary, width = 52.dp)
            }
        }
    }
}

@Composable
private fun KeyRow(
    letters: List<String>,
    theme: ThemeData,
    iconUri: android.net.Uri?,
    groupShape: ShapeConfig?,
    bgColor: Color,
    radius: androidx.compose.ui.unit.Dp,
    textColor: Color,
    keyWidth: androidx.compose.ui.unit.Dp,
) {
    Row(
        Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        letters.forEach { l ->
            PreviewKey(l, theme, iconUri, groupShape, bgColor, radius, textColor, keyWidth)
        }
    }
}

@Composable
private fun PreviewKey(
    text: String,
    theme: ThemeData,
    iconUri: android.net.Uri?,
    groupShape: ShapeConfig?,
    bgColor: Color,
    radius: androidx.compose.ui.unit.Dp,
    textColor: Color,
    width: androidx.compose.ui.unit.Dp = 32.dp,
    fontSize: Int = 13,
) {
    val outline = ColorUtils.parseColor(theme.outline)

    // ✅ توليد الشكل الفعلي
    val shapeImage = groupShape?.let { cfg ->
        remember(cfg, bgColor) {
            ShapeCache.get(KeyGroup.LETTERS, cfg, bgColor.value.toLong().toInt())
        }
    }

    Box(
        Modifier
            .padding(horizontal = 2.dp)
            .width(width)
            .height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        // ✅ الشكل الفعلي (إذا موجود)
        if (shapeImage != null) {
            Image(
                bitmap = shapeImage,
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            // الحدود العادية
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(radius))
                    .background(bgColor)
                    .then(
                        if (theme.autoBorders)
                            Modifier.border(1.dp, outline.copy(alpha = 0.35f), RoundedCornerShape(radius))
                        else Modifier
                    )
            )
        }

        // أيقونة الزر المرفوعة
        if (iconUri != null) {
            AsyncImage(
                model = iconUri, contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(radius))
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

@Composable
private fun SpacebarKey(
    theme: ThemeData,
    iconUri: android.net.Uri?,
    groupShape: ShapeConfig?,
    bgColor: Color,
    radius: androidx.compose.ui.unit.Dp,
    textColor: Color,
) {
    val outline = ColorUtils.parseColor(theme.outline)

    val shapeImage = groupShape?.let { cfg ->
        remember(cfg, bgColor) {
            ShapeCache.get(KeyGroup.BOTTOM_ROWS, cfg, bgColor.value.toLong().toInt())
        }
    }

    Box(
        Modifier.weight(1f).height(40.dp),
        contentAlignment = Alignment.Center
    ) {
        if (shapeImage != null) {
            Image(
                bitmap = shapeImage, contentDescription = null,
                contentScale = ContentScale.FillBounds,
                modifier = Modifier.fillMaxSize()
            )
        } else {
            Box(
                Modifier
                    .fillMaxSize()
                    .clip(RoundedCornerShape(radius))
                    .background(bgColor)
                    .then(
                        if (theme.autoBorders)
                            Modifier.border(1.dp, outline.copy(alpha=0.4f), RoundedCornerShape(radius))
                        else Modifier
                    )
            )
        }
        Text("مسافة",
            fontSize = (11 * theme.scaleText).sp,
            color = textColor.copy(alpha = 0.7f))
    }
}
