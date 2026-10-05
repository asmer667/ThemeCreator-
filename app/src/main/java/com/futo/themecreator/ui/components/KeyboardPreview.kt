package com.futo.themecreator.ui.components

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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.futo.themecreator.data.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import com.futo.themecreator.utils.ColorUtils

/**
 * معاينة الكيبورد — ترتيب مطابق لـ Gboard + حجم معاينة أكبر
 * لا صورة خلفية وراء المفاتيح
 */
/**
 * يحمّل FontFamily من filesDir/fonts/<fontName>
 */
@Composable
fun loadFontFamily(fontName: String?): FontFamily? {
    val context = LocalContext.current
    return remember(fontName, ThemeState.fontsVersion) {
        if (fontName == null) return@remember null
        try {
            val file = com.futo.themecreator.data.FontDownloader.fontFile(context, fontName)
            if (file.exists() && file.length() > 1000) {
                FontFamily(Font(file))
            } else null
        } catch (e: Exception) { null }
    }
}

@Composable
fun KeyboardPreview(theme: ThemeData, scale: Float = 1.4f) {
    val icons = ThemeState.icons
    val groupShapes = ThemeState.keyGroupShapes

    val kbSurface = ColorUtils.parseColor(theme.keyboardSurface)
    val kbContainer = ColorUtils.parseColor(theme.keyboardContainer)
    val kbVariant = ColorUtils.parseColor(theme.keyboardContainerVariant)
    val onKb = ColorUtils.parseColor(theme.onKeyboardContainer)
    val primary = ColorUtils.parseColor(theme.primary)
    val onPrimary = ColorUtils.parseColor(theme.onPrimary)
    val outline = ColorUtils.parseColor(theme.outline)

    val keyW      = (34.dp * scale)
    val keyH      = (44.dp * scale)
    val wideKeyW  = (52.dp * scale)
    val wideKeyH  = (44.dp * scale)
    val spaceW    = (150.dp * scale)
    val rowGap    = (7.dp * scale)
    val keyRadius = (theme.roundedness * 8).dp
    val customFont = loadFontFamily(theme.fontName)

    val gradientBrush: Brush? = remember(
        theme.gradientStart, theme.gradientEnd, theme.gradientAngle
    ) {
        val s = theme.gradientStart
        val e = theme.gradientEnd
        if (s != null && e != null) {
            try {
                val c1 = ColorUtils.parseColor(s)
                val c2 = ColorUtils.parseColor(e)
                val rad = Math.toRadians(theme.gradientAngle.toDouble())
                val dx = (Math.cos(rad) * 2000f).toFloat()
                val dy = (Math.sin(rad) * 2000f).toFloat()
                Brush.linearGradient(
                    colors = listOf(c1, c2),
                    start = Offset.Zero,
                    end = Offset(dx, dy),
                )
            } catch (ex: Exception) { null }
        } else null
    }

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(20.dp))
            .then(
                if (gradientBrush != null) Modifier.background(gradientBrush)
                else Modifier.background(kbSurface)
            )
            .border(2.dp, outline.copy(alpha = 0.35f), RoundedCornerShape(20.dp))
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = (5.dp * scale), vertical = (8.dp * scale))
        ) {
            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("1","2","3","4","5","6","7","8","9","0").forEach { t ->
                    Key(t, KeyGroup.TOP_ROWS, theme, icons["button.png"],
                        groupShapes[KeyGroup.TOP_ROWS], kbContainer, onKb,
                        keyRadius, keyW, keyH, customFont = customFont)
                }
            }
            Spacer(Modifier.height(rowGap))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("Q","W","E","R","T","Y","U","I","O","P").forEach { t ->
                    Key(t, KeyGroup.LETTERS, theme, icons["button.png"],
                        groupShapes[KeyGroup.LETTERS], kbContainer, onKb,
                        keyRadius, keyW, keyH, customFont = customFont)
                }
            }
            Spacer(Modifier.height(rowGap))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                listOf("A","S","D","F","G","H","J","K","L").forEach { t ->
                    Key(t, KeyGroup.LETTERS, theme, icons["button.png"],
                        groupShapes[KeyGroup.LETTERS], kbContainer, onKb,
                        keyRadius, keyW * 1.08f, keyH, customFont = customFont)
                }
            }
            Spacer(Modifier.height(rowGap))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Key("⇧", KeyGroup.TOP_ROWS, theme, icons["functional_button.png"],
                    groupShapes[KeyGroup.TOP_ROWS], kbVariant, onKb,
                    keyRadius, wideKeyW, wideKeyH, customFont = customFont)
                Spacer(Modifier.width(3.dp))
                listOf("Z","X","C","V","B","N","M").forEach { t ->
                    Key(t, KeyGroup.LETTERS, theme, icons["button.png"],
                        groupShapes[KeyGroup.LETTERS], kbContainer, onKb,
                        keyRadius, keyW, keyH, customFont = customFont)
                }
                Spacer(Modifier.width(3.dp))
                Key("⌫", KeyGroup.TOP_ROWS, theme, icons["functional_button.png"],
                    groupShapes[KeyGroup.TOP_ROWS], kbVariant, onKb,
                    keyRadius, wideKeyW, wideKeyH, customFont = customFont)
            }
            Spacer(Modifier.height(rowGap))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Key("?123", KeyGroup.BOTTOM_ROWS, theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbVariant, onKb,
                    keyRadius, wideKeyW, wideKeyH, fontSize = 11, customFont = customFont)
                Spacer(Modifier.width(3.dp))
                Key("،", KeyGroup.BOTTOM_ROWS, theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbVariant, onKb,
                    keyRadius, keyW * 0.85f, keyH, customFont = customFont)
                Spacer(Modifier.width(3.dp))
                Key("😊", KeyGroup.BOTTOM_ROWS, theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbVariant, onKb,
                    keyRadius, keyW * 0.9f, keyH, fontSize = 15, customFont = customFont)
                Spacer(Modifier.width(3.dp))
                SpaceKey(theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbContainer, onKb,
                    keyRadius, spaceW, keyH, customFont = customFont)
                Spacer(Modifier.width(3.dp))
                Key(".", KeyGroup.BOTTOM_ROWS, theme, icons["button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], kbVariant, onKb,
                    keyRadius, keyW * 0.85f, keyH, customFont = customFont)
                Spacer(Modifier.width(3.dp))
                Key("↵", KeyGroup.BOTTOM_ROWS, theme, icons["action_button.png"],
                    groupShapes[KeyGroup.BOTTOM_ROWS], primary, onPrimary,
                    keyRadius, wideKeyW, wideKeyH, customFont = customFont)
            }
        }
    }
}

@Composable
private fun Key(
    text: String,
    group: KeyGroup,
    theme: ThemeData,
    iconUri: android.net.Uri?,
    groupShape: ShapeConfig?,
    bgColor: Color,
    textColor: Color,
    radius: androidx.compose.ui.unit.Dp,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    fontSize: Int = 14,
    customFont: androidx.compose.ui.text.font.FontFamily? = null,
) {
    val outline = ColorUtils.parseColor(theme.outline)
    val bgArgb = bgColor.toArgb()

    val shapeImage = groupShape?.let { cfg ->
        remember(cfg, group, bgArgb) {
            ShapeCache.get(group, cfg, bgArgb)
        }
    }

    val effW = width  * (groupShape?.widthScale  ?: 1f)
    val effH = height * (groupShape?.heightScale ?: 1f)

    Box(
        Modifier
            .padding(horizontal = 1.dp)
            .width(effW)
            .height(effH),
        contentAlignment = Alignment.Center
    ) {
        if (shapeImage != null) {
            Image(
                bitmap = shapeImage,
                contentDescription = null,
                contentScale = ContentScale.Fit,
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
                            Modifier.border(1.dp, outline.copy(alpha = 0.3f), RoundedCornerShape(radius))
                        else Modifier
                    )
            )
        }

        if (iconUri != null) {
            AsyncImage(
                model = iconUri, contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier.size(effW * 0.6f).clip(RoundedCornerShape(radius))
            )
        }

        Text(
            text,
            fontSize = (fontSize * theme.scaleText).sp,
            fontWeight = FontWeight(theme.weightText.toInt().coerceIn(100, 900)),
            color = textColor,
            textAlign = TextAlign.Center,
            fontFamily = customFont
        )
    }
}

@Composable
private fun SpaceKey(
    theme: ThemeData,
    iconUri: android.net.Uri?,
    groupShape: ShapeConfig?,
    bgColor: Color,
    textColor: Color,
    radius: androidx.compose.ui.unit.Dp,
    width: androidx.compose.ui.unit.Dp,
    height: androidx.compose.ui.unit.Dp,
    customFont: androidx.compose.ui.text.font.FontFamily? = null,
) {
    val outline = ColorUtils.parseColor(theme.outline)
    val bgArgb = bgColor.toArgb()

    val shapeImage = groupShape?.let { cfg ->
        remember(cfg, bgArgb) {
            ShapeCache.get(KeyGroup.BOTTOM_ROWS, cfg, bgArgb)
        }
    }

    val effH = height * (groupShape?.heightScale ?: 1f)

    Box(
        Modifier.width(width).height(effH),
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
                            Modifier.border(1.dp, outline.copy(alpha=0.3f), RoundedCornerShape(radius))
                        else Modifier
                    )
            )
        }
        Text("مسافة",
            fontSize = (11 * theme.scaleText).sp,
            color = textColor.copy(alpha = 0.7f),
            fontFamily = customFont)
    }
}
