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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.Ready2Applier
import com.futo.themecreator.data.Ready2Colors
import com.futo.themecreator.data.Ready2Preset
import com.futo.themecreator.utils.ColorUtils

@Composable
fun Ready2Sheet() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("🌙 أحادية داكنة", "☀️ أحادية فاتحة", "🌌 تدرج داكن", "🌅 تدرج فاتح")

    Column(Modifier.fillMaxWidth()) {
        Text("🎨 جاهزة 2 — الخلفية فقط",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp))
        Text("تُغيّر الخلفية فقط، لا تلمس ألوان الأزرار",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(Modifier.height(8.dp))

        ScrollableTabRow(selectedTabIndex = selectedTab, edgePadding = 8.dp) {
            tabs.forEachIndexed { i, t ->
                Tab(selected = selectedTab == i, onClick = { selectedTab = i },
                    text = { Text(t, style = MaterialTheme.typography.labelMedium) })
            }
        }
        Spacer(Modifier.height(8.dp))

        val (list, isGradient) = when (selectedTab) {
            0 -> Ready2Colors.solidDark to false
            1 -> Ready2Colors.solidLight to false
            2 -> Ready2Colors.gradientDark to true
            else -> Ready2Colors.gradientLight to true
        }

        LazyVerticalGrid(
            columns = GridCells.Fixed(2),
            modifier = Modifier.fillMaxWidth().heightIn(max = 420.dp),
            contentPadding = PaddingValues(16.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(list) { preset -> Ready2Card(preset, isGradient) }
        }
        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun Ready2Card(preset: Ready2Preset, isGradient: Boolean) {
    val bg = ColorUtils.parseColor(preset.bg)
    val isDark = isDarkColor(bg)
    val onBg = if (isDark) Color.White else Color.Black

    Card(
        onClick = {
            if (isGradient) Ready2Applier.applyGradient(preset)
            else Ready2Applier.applySolid(preset)
        },
        modifier = Modifier.fillMaxWidth().height(90.dp),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Box(
            Modifier.fillMaxSize().then(
                if (isGradient && preset.color1 != null && preset.color2 != null) {
                    Modifier.background(Brush.horizontalGradient(listOf(
                        ColorUtils.parseColor(preset.color1).copy(alpha = 0.6f),
                        ColorUtils.parseColor(preset.color2).copy(alpha = 0.6f)
                    )))
                } else Modifier
            )
        ) {
            Column(Modifier.fillMaxSize().padding(10.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(preset.emoji, style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.width(6.dp))
                    Text(preset.name, style = MaterialTheme.typography.labelLarge,
                        color = onBg, fontWeight = FontWeight.Bold, maxLines = 1)
                }
                Spacer(Modifier.weight(1f))
                Box(
                    Modifier.fillMaxWidth().height(6.dp)
                        .clip(RoundedCornerShape(3.dp))
                        .background(
                            if (isGradient && preset.color1 != null && preset.color2 != null)
                                Brush.horizontalGradient(listOf(
                                    ColorUtils.parseColor(preset.color1),
                                    ColorUtils.parseColor(preset.color2)
                                ))
                            else Brush.horizontalGradient(listOf(bg, bg))
                        )
                )
            }
        }
    }
}

private fun isDarkColor(c: Color): Boolean {
    return (c.red * 0.299 + c.green * 0.587 + c.blue * 0.114) < 0.5
}
