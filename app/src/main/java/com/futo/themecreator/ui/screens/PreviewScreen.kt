package com.futo.themecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.KeyboardPreview

@Composable
fun PreviewScreen() {
    val theme = ThemeState.theme

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "👁️ المعاينة الحية",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            "شاهد شكل الكيبورد قبل التصدير",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // اسم الثيم
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "📝 معلومات الثيم",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text("الاسم: ${theme.name}", style = MaterialTheme.typography.bodySmall)
                Text("المؤلف: ${theme.author}", style = MaterialTheme.typography.bodySmall)
                Text("ID: ${theme.id}", style = MaterialTheme.typography.bodySmall)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // معاينة الكيبورد
        Text(
            "⌨️ الكيبورد",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        KeyboardPreview(theme)

        Spacer(modifier = Modifier.height(16.dp))

        // معاينة الألوان الرئيسية
        Text(
            "🎨 عينات الألوان",
            style = MaterialTheme.typography.titleMedium
        )
        Spacer(modifier = Modifier.height(8.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = Color.Black)
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                theme.allColors().take(12).forEach { (key, value) ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            key,
                            style = MaterialTheme.typography.bodySmall,
                            color = Color.White
                        )
                        Box(
                            modifier = Modifier
                                .width(60.dp)
                                .height(16.dp)
                                .background(
                                    com.futo.themecreator.utils.ColorUtils.parseColor(value)
                                )
                        )
                    }
                }
            }
        }
    }
}
