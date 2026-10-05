package com.futo.themecreator.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.ColorPickerDialog
import com.futo.themecreator.ui.components.ColorRow
import com.futo.themecreator.utils.ColorUtils

@Composable
fun ColorsScreen() {
    val theme = ThemeState.theme
    var selectedColorKey by remember { mutableStateOf<String?>(null) }
    var selectedColorName by remember { mutableStateOf("") }
    var selectedColorValue by remember { mutableStateOf("") }

    // Dialog
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

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // عنوان
        item {
            Text(
                "🎨 محرر الألوان (44 لون)",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                "اضغط على أي لون لتعديله",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // كل فئة
        ColorUtils.colorCategories.forEach { category ->
            item {
                Text(
                    category.name,
                    style = MaterialTheme.typography.titleMedium,
                    modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 4.dp)
                )
                HorizontalDivider(modifier = Modifier.padding(horizontal = 16.dp))
            }

            items(category.keys) { (key, displayName) ->
                // البحث عن القيمة الحالية
                val currentValue = theme.allColors().firstOrNull { it.first == key }?.second ?: "#000000"

                ColorRow(
                    colorKey = key,
                    displayName = displayName,
                    value = currentValue,
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
