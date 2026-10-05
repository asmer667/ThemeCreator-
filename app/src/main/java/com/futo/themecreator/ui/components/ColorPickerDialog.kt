package com.futo.themecreator.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.futo.themecreator.utils.ColorUtils

/**
 * منتقي ألوان احترافي
 */
@Composable
fun ColorPickerDialog(
    initialColor: String,
    colorName: String,
    onDismiss: () -> Unit,
    onConfirm: (String) -> Unit,
) {
    var hexInput by remember { mutableStateOf(initialColor) }
    var previewColor by remember { mutableStateOf(ColorUtils.parseColor(initialColor)) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("🎨 $colorName") },
        text = {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                // 1. معاينة اللون
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(80.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(previewColor)
                        .border(2.dp, MaterialTheme.colorScheme.onSurface, RoundedCornerShape(12.dp))
                )

                // 2. حقل Hex
                OutlinedTextField(
                    value = hexInput,
                    onValueChange = { newValue ->
                        hexInput = newValue
                        if (newValue.matches(Regex("^#?[0-9A-Fa-f]{3,8}$"))) {
                            previewColor = ColorUtils.parseColor(newValue)
                        }
                    },
                    label = { Text("Hex Color") },
                    placeholder = { Text("#RRGGBB أو #AARRGGBB") },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text),
                    modifier = Modifier.fillMaxWidth()
                )

                // 3. ألوان سريعة
                Text(
                    "ألوان سريعة:",
                    style = MaterialTheme.typography.labelMedium
                )
                LazyVerticalGrid(
                    columns = GridCells.Fixed(8),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(120.dp),
                    horizontalArrangement = Arrangement.spacedBy(4.dp),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    items(quickColors) { color ->
                        Box(
                            modifier = Modifier
                                .aspectRatio(1f)
                                .clip(RoundedCornerShape(4.dp))
                                .background(color)
                                .border(1.dp, Color.Gray, RoundedCornerShape(4.dp))
                                .clickable {
                                    hexInput = ColorUtils.toHex(color)
                                    previewColor = color
                                }
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = {
                onConfirm(hexInput)
                onDismiss()
            }) {
                Text("حفظ")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء")
            }
        }
    )
}

/** ألوان جاهزة */
private val quickColors: List<Color> = listOf(
    Color(0xFF000000), Color(0xFFFFFFFF), Color(0xFF9E9E9E),
    Color(0xFFF44336), Color(0xFFE91E63), Color(0xFF9C27B0),
    Color(0xFF673AB7), Color(0xFF3F51B5), Color(0xFF2196F3),
    Color(0xFF03A9F4), Color(0xFF00BCD4), Color(0xFF009688),
    Color(0xFF4CAF50), Color(0xFF8BC34A), Color(0xFFCDDC39),
    Color(0xFFFFEB3B), Color(0xFFFFC107), Color(0xFFFF9800),
    Color(0xFFFF5722), Color(0xFF795548), Color(0xFF607D8B),
    Color(0xFF00BFFF), Color(0xFF4FC3F7), Color(0xFFFF1493),
    Color(0xFF00C853), Color(0xFFE86F73), Color(0xFF087CFF),
    Color(0xFF101114), Color(0xFF0A0A0A), Color(0xFF1A1A1A),
    Color(0xFF2D001E), Color(0xFF003D1A),
)
