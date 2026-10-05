package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.ColorPickerDialog
import com.futo.themecreator.utils.ColorUtils

@Composable
fun ColorsSheet() {
    val theme = ThemeState.theme
    var selectedKey by remember { mutableStateOf<String?>(null) }
    var selectedName by remember { mutableStateOf("") }
    var selectedValue by remember { mutableStateOf("") }

    if (selectedKey != null) {
        ColorPickerDialog(
            initialColor = selectedValue,
            colorName = selectedName,
            onDismiss = { selectedKey = null },
            onConfirm = { newColor ->
                ThemeState.update(selectedKey!!, newColor)
                selectedKey = null
            }
        )
    }

    Column(Modifier.fillMaxWidth()) {
        Text("🎨 محرر الألوان",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))
        Text("اضغط على أي لون لتعديله",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ColorUtils.colorCategories.forEach { category ->
                item(span = { androidx.compose.foundation.lazy.grid.GridItemSpan(2) }) {
                    Text(category.name,
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(top = 8.dp))
                }
                items(category.keys) { (key, displayName) ->
                    val value = theme.allColors().firstOrNull { it.first == key }?.second ?: "#000000"
                    Card(
                        onClick = {
                            selectedKey = key
                            selectedName = displayName
                            selectedValue = value
                        },
                        modifier = Modifier.fillMaxWidth().height(70.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = MaterialTheme.colorScheme.surfaceVariant
                        )
                    ) {
                        Row(
                            Modifier.fillMaxSize().padding(8.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                Modifier.size(32.dp).clip(RoundedCornerShape(8.dp))
                                    .background(ColorUtils.parseColor(value))
                                    .border(1.dp, Color.Gray, RoundedCornerShape(8.dp))
                            )
                            Spacer(Modifier.width(8.dp))
                            Column(Modifier.weight(1f)) {
                                Text(displayName, style = MaterialTheme.typography.labelMedium, maxLines = 1)
                                Text(value, style = MaterialTheme.typography.labelSmall,
                                    color = MaterialTheme.colorScheme.primary)
                            }
                        }
                    }
                }
            }
        }

        // ─── قسم تحسينات التدرّج (يظهر فقط عند وجود تدرّج) ───
        GradientTuningSection()

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun GradientTuningSection() {
    val theme = ThemeState.theme
    if (theme.gradientStart == null || theme.gradientEnd == null) return

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Divider(Modifier.padding(vertical = 12.dp))
        Text("\u2699\ufe0f \u062a\u062d\u0633\u064a\u0646\u0627\u062a \u0627\u0644\u062a\u062f\u0631\u0651\u062c",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))

        // نمط التدرّج
        Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
            listOf("linear", "radial", "sweep").forEach { mode ->
                FilterChip(
                    selected = theme.gradientMode == mode,
                    onClick = { ThemeState.setGradientMode(mode) },
                    label = { Text(when(mode) {
                        "linear" -> "\u062e\u0637\u064a"
                        "radial" -> "\u062f\u0627\u0626\u0631\u064a"
                        else -> "\u0634\u0639\u0627\u0639\u064a"
                    }, style = MaterialTheme.typography.labelMedium) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        TuningSlider("\u0627\u062a\u062c\u0627\u0647",
            theme.gradientAngle, 0f..360f) { ThemeState.setGradientAngle(it) }
        TuningSlider("\u0636\u0628\u0627\u0628\u064a\u0629",
            theme.gradientBlur, 0f..100f) { ThemeState.setGradientBlur(it) }
        TuningSlider("\u0634\u0641\u0627\u0641\u064a\u0629",
            theme.gradientOpacity, 0.1f..1f) { ThemeState.setGradientOpacity(it) }
        TuningSlider("\u062a\u0648\u0632\u064a\u0639 \u0627\u0644\u0648\u0633\u0637",
            theme.gradientStops.split(",").getOrNull(1)?.toFloatOrNull() ?: 0.5f,
            0.1f..0.9f) {
                ThemeState.setGradientStops("0.0,${"%.2f".format(it)},1.0")
        }
    }
}

@Composable
private fun TuningSlider(label: String, value: Float, range: ClosedFloatingPointRange<Float>, onChange: (Float) -> Unit) {
    Column(Modifier.fillMaxWidth().padding(vertical = 4.dp)) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(label, style = MaterialTheme.typography.labelMedium, modifier = Modifier.weight(1f))
            Text("%.2f".format(value),
                style = MaterialTheme.typography.labelMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary)
        }
        Slider(value = value, onValueChange = onChange, valueRange = range)
    }
}
