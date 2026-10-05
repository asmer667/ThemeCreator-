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
    }
}
