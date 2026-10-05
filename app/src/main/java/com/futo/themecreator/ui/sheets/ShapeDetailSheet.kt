package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themecreator.data.*
import com.futo.themecreator.utils.ColorUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ShapeDetailSheet(
    shape: ShapeDef,
    onDismiss: () -> Unit,
) {
    val theme = ThemeState.theme
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    var targetGroup by remember { mutableStateOf(ThemeState.selectedShapeKey) }
    var config by remember(targetGroup) {
        mutableStateOf(
            (ThemeState.getShapeConfig(targetGroup) ?: ShapeConfig()).copy(shapeId = shape.id)
        )
    }

    val previewColor = remember(theme.keyboardContainerVariant) {
        ColorUtils.parseColor(theme.keyboardContainerVariant).toArgb()
    }

    val previewBitmap = remember(config) {
        ShapeThumbnailCache.get(
            shapeId = config.shapeId,
            fillColor = previewColor,
            rotation = config.rotation,
            sharpness = config.sharpness,
            tilt = config.tilt,
        )
    }

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = MaterialTheme.colorScheme.surfaceContainerLow
    ) {
        Column(
            Modifier.fillMaxWidth().padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Text(
                "\ud83d\udd37 " + shape.name,
                style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold
            )
            Text(
                shape.id + " \u00b7 " + shape.family,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(Modifier.height(12.dp))

            Box(
                Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .clip(RoundedCornerShape(16.dp))
                    .background(MaterialTheme.colorScheme.surfaceContainerHigh)
                    .border(
                        1.dp,
                        MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                        RoundedCornerShape(16.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Image(
                    bitmap = previewBitmap.asImageBitmap(),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize().padding(12.dp)
                )
            }

            Spacer(Modifier.height(12.dp))

            Text(
                "\u0627\u0644\u0645\u062c\u0645\u0648\u0639\u0629 \u0627\u0644\u0645\u0633\u062a\u0647\u062f\u0641\u0629",
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(4.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                KeyGroup.values().forEach { g ->
                    FilterChip(
                        selected = targetGroup == g,
                        onClick = {
                            targetGroup = g
                            config = (ThemeState.getShapeConfig(g) ?: ShapeConfig()).copy(shapeId = shape.id)
                        },
                        label = { Text(g.display, fontSize = 11.sp) }
                    )
                }
            }

            Spacer(Modifier.height(12.dp))

            CompactSlider("\u0627\u0644\u062f\u0648\u0631\u0627\u0646",
                config.rotation, 0f..360f) { config = config.copy(rotation = it) }
            CompactSlider("\u0627\u0644\u062d\u062f\u0629",
                config.sharpness, 0f..1f) { config = config.copy(sharpness = it) }
            CompactSlider("\u0627\u0644\u0645\u064a\u0644\u0627\u0646",
                config.tilt, -45f..45f) { config = config.copy(tilt = it) }
            CompactSlider("\u0639\u0631\u0636 \u0627\u0644\u0632\u0631",
                config.widthScale, 0.5f..2.0f) { config = config.copy(widthScale = it) }
            CompactSlider("\u0627\u0631\u062a\u0641\u0627\u0639 \u0627\u0644\u0632\u0631",
                config.heightScale, 0.5f..2.0f) { config = config.copy(heightScale = it) }

            Spacer(Modifier.height(16.dp))

            Row(
                Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = {
                        ThemeState.clearShapeConfig(targetGroup)
                        ShapeThumbnailCache.clear()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("\ud83d\uddd1\ufe0f \u0625\u0632\u0627\u0644\u0629")
                }
                Button(
                    onClick = {
                        ThemeState.setShapeConfig(targetGroup, config)
                        ShapeThumbnailCache.clear()
                        onDismiss()
                    },
                    modifier = Modifier.weight(1f)
                ) {
                    Text("\u2705 \u062a\u0637\u0628\u064a\u0642")
                }
            }
            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun CompactSlider(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    onChange: (Float) -> Unit,
) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(
                label,
                style = MaterialTheme.typography.labelMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                "%.2f".format(value),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary
            )
        }
        Slider(
            value = value,
            onValueChange = onChange,
            valueRange = range,
        )
    }
}
