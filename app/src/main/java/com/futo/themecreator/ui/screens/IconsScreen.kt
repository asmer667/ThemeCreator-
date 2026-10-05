package com.futo.themecreator.ui.screens

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.IconManager
import com.futo.themecreator.data.IconSpec
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.IconCard

@Composable
fun IconsScreen() {
    val icons = ThemeState.icons
    var pendingIcon by remember { mutableStateOf<IconSpec?>(null) }

    // منتقي الصور
    val pickImageLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && pendingIcon != null) {
            ThemeState.setIcon(pendingIcon!!.fileName, uri)
        }
        pendingIcon = null
    }

    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(bottom = 16.dp)
    ) {
        // عنوان
        item {
            Text(
                "🖼️ محرر الأيقونات",
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.padding(16.dp)
            )
            Text(
                "ارفع صور PNG مخصصة لأزرار الكيبورد",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
        }

        // تحذير
        item {
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.tertiaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "⚠️ نصائح مهمة",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "• استخدم صور PNG بخلفية شفافة\n" +
                        "• الأبعاد المثالية: 200x200 بكسل\n" +
                        "• الحجم الأقصى: 1 MB لكل صورة\n" +
                        "• الأصول الموصى بها: 9-slice borders",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                }
            }
        }

        // الأيقونات
        item {
            Text(
                "الأيقونات المدعومة",
                style = MaterialTheme.typography.titleMedium,
                modifier = Modifier.padding(16.dp, 12.dp, 16.dp, 4.dp)
            )
        }

        items(IconManager.supportedIcons) { spec ->
            IconCard(
                spec = spec,
                currentUri = icons[spec.fileName],
                onPickImage = {
                    pendingIcon = spec
                    pickImageLauncher.launch("image/*")
                },
                onClear = {
                    ThemeState.setIcon(spec.fileName, null)
                }
            )
        }

        // ملخص
        item {
            HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp))
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Column(modifier = Modifier.padding(12.dp)) {
                    Text(
                        "📊 الملخص",
                        style = MaterialTheme.typography.titleSmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        "الأيقونات المرفوعة: ${icons.size} / ${IconManager.supportedIcons.size}",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                    Text(
                        "الأيقونات المتبقية: ${IconManager.supportedIcons.size - icons.size}",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}
