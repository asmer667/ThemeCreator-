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
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.futo.themecreator.data.Shapes3DLoader
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.ToggleRow
import com.futo.themecreator.ui.components.ValueSlider

@Composable
fun ShapesSheet() {
    val context = LocalContext.current
    val theme = ThemeState.theme
    val shapes3D by remember { mutableStateOf(ThemeState.shapes3D) }

    Column(Modifier.fillMaxWidth()) {
        // Header
        Text("🔷 الأشكال 3D",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))
        Text("اضغط على الشكل لإضافته للثيم (${shapes3D.size} مختار)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))

        // الأشكال
        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth().heightIn(max = 320.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(Shapes3DLoader.load(context)) { entry ->
                val isSelected = entry.file in shapes3D
                Box(
                    modifier = Modifier
                        .aspectRatio(1f)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) MaterialTheme.colorScheme.primaryContainer
                            else MaterialTheme.colorScheme.surfaceVariant
                        )
                        .border(
                            width = if (isSelected) 3.dp else 1.dp,
                            color = if (isSelected) MaterialTheme.colorScheme.primary
                            else MaterialTheme.colorScheme.outline.copy(alpha = 0.3f),
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable {
                            ThemeState.toggleShape3D(entry.file)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    AsyncImage(
                        model = "file:///android_asset/shapes_3d/${entry.file}",
                        contentDescription = entry.name,
                        contentScale = ContentScale.Fit,
                        modifier = Modifier.fillMaxSize().padding(6.dp)
                    )
                }
            }
        }

        HorizontalDivider(Modifier.padding(vertical = 8.dp))

        // خيارات إضافية
        Column(Modifier.padding(horizontal = 16.dp)) {
            Text("⚙️ خيارات عامة",
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(vertical = 8.dp))

            ValueSlider("استدارة الأزرار", "0.0 = حاد، 1.0 = دائري",
                theme.roundedness, 0f..1f, 0.01f) {
                ThemeState.updateNumber("roundedness", it)
            }
            ToggleRow("حدود تلقائية", "إظهار حدود حول الأزرار",
                theme.autoBorders) { ThemeState.updateBool("auto_borders", it) }
            ToggleRow("توسيط التلميحات", "توسيط النصوص",
                theme.centerHints) { ThemeState.updateBool("center_hints", it) }
            ValueSlider("حجم النص", "حجم الحروف",
                theme.scaleText, 0.5f..2f, 0.05f) {
                ThemeState.updateNumber("scale_text", it)
            }
            ValueSlider("وزن النص", "400 = عادي، 700 = عريض",
                theme.weightText, 100f..900f, 100f) {
                ThemeState.updateNumber("weight_text", it)
            }
        }
        Spacer(Modifier.height(32.dp))
    }
}
