package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.ToggleRow
import com.futo.themecreator.ui.components.ValueSlider

@Composable
fun ShapesSheet() {
    val theme = ThemeState.theme
    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Text("🔷 الأشكال", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        }
        item {
            ValueSlider("استدارة الأزرار", "0.0 = حاد، 1.0 = دائري",
                theme.roundedness, 0f..1f, 0.01f) {
                ThemeState.updateNumber("roundedness", it)
            }
        }
        item {
            ToggleRow("حدود تلقائية", "إظهار حدود حول الأزرار",
                theme.autoBorders) { ThemeState.updateBool("auto_borders", it) }
        }
        item {
            ToggleRow("توسيط التلميحات", "توسيط النصوص الثانوية",
                theme.centerHints) { ThemeState.updateBool("center_hints", it) }
        }
        item { HorizontalDivider(Modifier.padding(vertical = 8.dp)) }
        item {
            ValueSlider("حجم النص", "حجم الحروف",
                theme.scaleText, 0.5f..2f, 0.05f) {
                ThemeState.updateNumber("scale_text", it)
            }
        }
        item {
            ValueSlider("حجم التلميحات", "حجم الأرقام الصغيرة",
                theme.scaleHints, 0.5f..2f, 0.05f) {
                ThemeState.updateNumber("scale_hints", it)
            }
        }
        item { HorizontalDivider(Modifier.padding(vertical = 8.dp)) }
        item {
            ValueSlider("وزن النص", "400 = عادي، 700 = عريض",
                theme.weightText, 100f..900f, 100f) {
                ThemeState.updateNumber("weight_text", it)
            }
        }
        item {
            ValueSlider("وزن التلميحات", "وزن النصوص الثانوية",
                theme.weightHints, 100f..900f, 100f) {
                ThemeState.updateNumber("weight_hints", it)
            }
        }
    }
}
