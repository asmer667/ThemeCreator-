package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.CompactSliderRow

/**
 * تبويب ضبط النص — حجم، وزن، تلميحات
 */
@Composable
fun TextSheet() {
    val theme = ThemeState.theme

    Column(Modifier.fillMaxWidth()) {
        Text(
            "📝 ضبط النص",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            "كل التغييرات تظهر فورًا في المعاينة، وتُصدَّر مع الثيم إلى FUTO Keyboard.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(12.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Column(
                Modifier.padding(16.dp).fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    "نموذج نص",
                    fontSize = (20 * theme.scaleText).sp,
                    fontWeight = FontWeight(theme.weightText.toInt().coerceIn(100, 900))
                )
                Spacer(Modifier.height(6.dp))
                Text(
                    "A Quick Brown Fox",
                    fontSize = (14 * theme.scaleText).sp,
                    fontWeight = FontWeight(theme.weightText.toInt().coerceIn(100, 900))
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "تلميح",
                    fontSize = (11 * theme.scaleHints).sp,
                    fontWeight = FontWeight(theme.weightHints.toInt().coerceIn(100, 900)),
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        Spacer(Modifier.height(16.dp))

        SliderRow(
            label = "حجم النص",
            value = theme.scaleText,
            range = 0.5f..2.0f,
            step = 0.05f,
            emoji = "🔠"
        ) { ThemeState.updateNumber("scale_text", it) }

        SliderRow(
            label = "وزن النص",
            value = theme.weightText,
            range = 100f..900f,
            step = 50f,
            emoji = "🅱️"
        ) { ThemeState.updateNumber("weight_text", it) }

        SliderRow(
            label = "حجم التلميحات",
            value = theme.scaleHints,
            range = 0.5f..2.0f,
            step = 0.05f,
            emoji = "🔡"
        ) { ThemeState.updateNumber("scale_hints", it) }

        SliderRow(
            label = "وزن التلميحات",
            value = theme.weightHints,
            range = 100f..900f,
            step = 50f,
            emoji = "🅱️"
        ) { ThemeState.updateNumber("weight_hints", it) }

        Row(
            Modifier.fillMaxWidth().padding(16.dp, 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("🎯", fontSize = 18.sp)
            Spacer(Modifier.width(12.dp))
            Text(
                "تمركز التلميحات",
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Switch(
                checked = theme.centerHints,
                onCheckedChange = { ThemeState.updateBool("center_hints", it) }
            )
        }

        Spacer(Modifier.height(32.dp))
    }
}

@Composable
private fun SliderRow(
    label: String,
    value: Float,
    range: ClosedFloatingPointRange<Float>,
    step: Float,
    emoji: String,
    onChange: (Float) -> Unit,
) {
    Column(
        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp)
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Text(emoji, fontSize = 16.sp)
            Spacer(Modifier.width(8.dp))
            Text(
                label,
                style = MaterialTheme.typography.bodyMedium,
                modifier = Modifier.weight(1f)
            )
            Text(
                "%.2f".format(value),
                style = MaterialTheme.typography.labelMedium,
                color = MaterialTheme.colorScheme.primary,
                fontWeight = FontWeight.Bold
            )
        }
        Spacer(Modifier.height(4.dp))
        CompactSliderRow(
            label = "",
            value = value,
            onValueChange = onChange,
            range = range,
        )
    }
}
