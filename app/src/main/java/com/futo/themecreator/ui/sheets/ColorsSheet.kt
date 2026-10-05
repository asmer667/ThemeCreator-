package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.ColorPickerDialog
import com.futo.themecreator.ui.components.ColorRow
import com.futo.themecreator.utils.ColorUtils

@Composable
fun ColorsSheet() {
    val theme = ThemeState.theme
    var selectedColorKey by remember { mutableStateOf<String?>(null) }
    var selectedColorName by remember { mutableStateOf("") }
    var selectedColorValue by remember { mutableStateOf("") }

    if (selectedColorKey != null) {
        ColorPickerDialog(
            initialColor = selectedColorValue,
            colorName = selectedColorName,
            onDismiss = { selectedColorKey = null },
            onConfirm = { newColor ->
                ThemeState.update(selectedColorKey!!, newColor)
                selectedColorKey = null
            }
        )
    }

    Column(Modifier.fillMaxWidth()) {
        Text("🎨 الألوان (44)", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp),
            contentPadding = PaddingValues(bottom = 32.dp)
        ) {
            ColorUtils.colorCategories.forEach { category ->
                item {
                    Text(category.name, style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 4.dp),
                        color = MaterialTheme.colorScheme.primary)
                    HorizontalDivider()
                }
                items(category.keys) { (key, displayName) ->
                    val currentValue = theme.allColors()
                        .firstOrNull { it.first == key }?.second ?: "#000000"
                    ColorRow(
                        colorKey = key, displayName = displayName, value = currentValue,
                        onClick = {
                            selectedColorKey = key
                            selectedColorName = displayName
                            selectedColorValue = currentValue
                        }
                    )
                }
            }
        }
    }
}
