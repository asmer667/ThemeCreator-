package com.futo.themecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themecreator.data.*
import com.futo.themecreator.ui.components.ToggleRow
import com.futo.themecreator.ui.components.ValueSlider

@Composable
fun ShapesScreen() {
    val theme = ThemeState.theme

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // عنوان
        item {
            Text(
                "🔷 محرر الأشكال",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                "تحكم في استدارة الأزرار، الحدود، وأحجام النصوص",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
        }

        // ═══════════ 1. استدارة الأزرار ═══════════
        item {
            SectionTitle("🔘 استدارة الأزرار (Roundedness)")
        }

        item {
            // معاينة الأزرار
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape((theme.roundedness * 25).dp))
                        .background(MaterialTheme.colorScheme.primary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("A", color = MaterialTheme.colorScheme.onPrimary)
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape((theme.roundedness * 25).dp))
                        .background(MaterialTheme.colorScheme.secondary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("B", color = MaterialTheme.colorScheme.onSecondary)
                }
                Box(
                    modifier = Modifier
                        .size(50.dp)
                        .clip(RoundedCornerShape((theme.roundedness * 25).dp))
                        .background(MaterialTheme.colorScheme.tertiary),
                    contentAlignment = Alignment.Center
                ) {
                    Text("C", color = MaterialTheme.colorScheme.onTertiary)
                }
            }
        }

        item {
            ValueSlider(
                label = "الاستدارة",
                description = "0.0 = حاد، 1.0 = دائري كامل",
                value = theme.roundedness,
                valueRange = 0f..1f,
                step = 0.01f,
                onValueChange = { ThemeState.updateNumber("roundedness", it) }
            )
        }

        // أشكال جاهزة
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(shapePresets) { preset ->
                    AssistChip(
                        onClick = { ThemeState.updateNumber("roundedness", preset.roundedness) },
                        label = { Text("${preset.emoji} ${preset.name}") },
                        colors = if (theme.roundedness == preset.roundedness) {
                            AssistChipDefaults.assistChipColors(
                                containerColor = MaterialTheme.colorScheme.primaryContainer
                            )
                        } else {
                            AssistChipDefaults.assistChipColors()
                        }
                    )
                }
            }
        }

        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }

        // ═══════════ 2. الحدود ═══════════
        item {
            SectionTitle("▢ الحدود (Borders)")
        }

        item {
            ToggleRow(
                label = "حدود تلقائية",
                description = "إظهار حدود حول الأزرار",
                checked = theme.autoBorders,
                onCheckedChange = { ThemeState.updateBool("auto_borders", it) }
            )
        }

        item {
            ToggleRow(
                label = "توسيط التلميحات",
                description = "توسيط النصوص الثانوية على الأزرار",
                checked = theme.centerHints,
                onCheckedChange = { ThemeState.updateBool("center_hints", it) }
            )
        }

        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }

        // ═══════════ 3. حجم النص ═══════════
        item {
            SectionTitle("📏 حجم النصوص")
        }

        item {
            ValueSlider(
                label = "حجم النص الرئيسي",
                description = "حجم الحروف على الأزرار",
                value = theme.scaleText,
                valueRange = 0.5f..2f,
                step = 0.05f,
                onValueChange = { ThemeState.updateNumber("scale_text", it) }
            )
        }

        item {
            ValueSlider(
                label = "حجم التلميحات",
                description = "حجم الأرقام والرموز الصغيرة",
                value = theme.scaleHints,
                valueRange = 0.5f..2f,
                step = 0.05f,
                onValueChange = { ThemeState.updateNumber("scale_hints", it) }
            )
        }

        // أحجام جاهزة
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(fontSizePresets) { preset ->
                    AssistChip(
                        onClick = {
                            ThemeState.updateNumber("scale_text", preset.scale)
                            ThemeState.updateNumber("scale_hints", preset.scale)
                        },
                        label = { Text(preset.name) }
                    )
                }
            }
        }

        item { HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp)) }

        // ═══════════ 4. أوزان النص ═══════════
        item {
            SectionTitle("⚖️ أوزان النصوص")
        }

        item {
            ValueSlider(
                label = "وزن النص الرئيسي",
                description = "400 = عادي، 700 = عريض",
                value = theme.weightText,
                valueRange = 100f..900f,
                step = 100f,
                onValueChange = { ThemeState.updateNumber("weight_text", it) }
            )
        }

        item {
            ValueSlider(
                label = "وزن التلميحات",
                description = "وزن النصوص الثانوية",
                value = theme.weightHints,
                valueRange = 100f..900f,
                step = 100f,
                onValueChange = { ThemeState.updateNumber("weight_hints", it) }
            )
        }

        // أوزان جاهزة
        item {
            LazyRow(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                items(fontWeightPresets) { preset ->
                    AssistChip(
                        onClick = {
                            ThemeState.updateNumber("weight_text", preset.weight)
                            ThemeState.updateNumber("weight_hints", preset.weight)
                        },
                        label = { Text(preset.name) }
                    )
                }
            }
        }

        // ═══════════ 5. معاينة نهائية ═══════════
        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))
            SectionTitle("👁️ معاينة مباشرة")
        }

        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .background(Color.Black, RoundedCornerShape(8.dp))
                    .padding(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // صف أول
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PreviewKey("Q", theme)
                    PreviewKey("W", theme)
                    PreviewKey("E", theme)
                    PreviewKey("R", theme)
                }
                Spacer(modifier = Modifier.height(4.dp))
                // صف ثاني
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PreviewKey("A", theme)
                    PreviewKey("S", theme)
                    PreviewKey("D", theme)
                    PreviewKey("F", theme)
                }
                Spacer(modifier = Modifier.height(4.dp))
                // صف ثالث
                Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                    PreviewKey("Z", theme)
                    PreviewKey("X", theme)
                    PreviewKey("C", theme)
                    PreviewKey("V", theme)
                }
            }
        }
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 4.dp)
    )
}

@Composable
private fun PreviewKey(letter: String, theme: ThemeData) {
    Box(
        modifier = Modifier
            .size(40.dp)
            .clip(RoundedCornerShape((theme.roundedness * 20).dp))
            .background(
                com.futo.themecreator.utils.ColorUtils.parseColor(theme.keyboardContainer)
            )
            .border(
                1.dp,
                com.futo.themecreator.utils.ColorUtils.parseColor(theme.outline),
                RoundedCornerShape((theme.roundedness * 20).dp)
            ),
        contentAlignment = Alignment.Center
    ) {
        Text(
            letter,
            fontSize = (16 * theme.scaleText).sp,
            fontWeight = FontWeight(theme.weightText.toInt()),
            color = com.futo.themecreator.utils.ColorUtils.parseColor(theme.onKeyboardContainer)
        )
    }
}
