package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.*
import com.futo.themecreator.utils.ColorUtils

@Composable
fun PresetsSheet() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("🎨 أحادية", "🌈 متدرجة")

    Column(Modifier.fillMaxWidth()) {
        Text("✨ ألوان جاهزة", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
        Text("اختر لوناً ليُطبق فوراً على الكيبورد",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))

        TabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { i, title ->
                Tab(selected = selectedTab == i, onClick = { selectedTab = i },
                    text = { Text(title) })
            }
        }

        Spacer(Modifier.height(8.dp))

        if (selectedTab == 0) SolidTab() else GradientTab()
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SolidTab() {
    var dark by remember { mutableStateOf(true) }
    val list = if (dark) PresetColors.solidDark else PresetColors.solidLight

    Column {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = dark, onClick = { dark = true }, label = { Text("🌙 داكن") })
            FilterChip(selected = !dark, onClick = { dark = false }, label = { Text("☀️ فاتح") })
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { preset -> SolidPresetCard(preset, dark) }
        }
    }
}

@Composable
private fun GradientTab() {
    var dark by remember { mutableStateOf(true) }
    val list = if (dark) PresetColors.gradientDark else PresetColors.gradientLight

    Column {
        Row(Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            FilterChip(selected = dark, onClick = { dark = true }, label = { Text("🌙 داكن") })
            FilterChip(selected = !dark, onClick = { dark = false }, label = { Text("☀️ فاتح") })
        }
        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().heightIn(max = 400.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { preset -> GradientPresetCard(preset, dark) }
        }
    }
}

@Composable
private fun SolidPresetCard(preset: SolidPreset, dark: Boolean) {
    val accent = ColorUtils.parseColor(preset.accent)
    val bg = ColorUtils.parseColor(preset.bg)
    val onBg = if (dark) Color.White else Color.Black
    Card(
        onClick = { PresetApplier.applySolid(preset, dark) },
        modifier = Modifier.fillMaxWidth().height(90.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Column(Modifier.fillMaxSize().padding(10.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(preset.emoji, style = MaterialTheme.typography.titleLarge)
                Spacer(Modifier.width(6.dp))
                Text(preset.name, style = MaterialTheme.typography.bodyMedium,
                    color = onBg, fontWeight = FontWeight.Bold, maxLines = 1)
            }
            Spacer(Modifier.weight(1f))
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                Box(Modifier.size(20.dp).clip(RoundedCornerShape(6.dp)).background(accent))
                Box(Modifier.size(20.dp).clip(RoundedCornerShape(6.dp))
                    .background(ColorUtils.parseColor(preset.container)))
            }
        }
    }
}

@Composable
private fun GradientPresetCard(preset: GradientPreset, dark: Boolean) {
    val c1 = ColorUtils.parseColor(preset.color1)
    val c2 = ColorUtils.parseColor(preset.color2)
    val onBg = if (dark) Color.White else Color.Black
    Card(
        onClick = { PresetApplier.applyGradient(preset, dark) },
        modifier = Modifier.fillMaxWidth().height(90.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = ColorUtils.parseColor(preset.bg))
    ) {
        Box(Modifier.fillMaxSize().background(
            Brush.horizontalGradient(listOf(c1.copy(alpha=0.3f), c2.copy(alpha=0.3f))))) {
            Column(Modifier.fillMaxSize().padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(preset.emoji, style = MaterialTheme.typography.titleLarge)
                    Spacer(Modifier.width(6.dp))
                    Text(preset.name, style = MaterialTheme.typography.bodyMedium,
                        color = onBg, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Spacer(Modifier.weight(1f))
                Box(Modifier.fillMaxWidth().height(6.dp)
                    .clip(RoundedCornerShape(3.dp))
                    .background(Brush.horizontalGradient(listOf(c1, c2))))
            }
        }
    }
}
