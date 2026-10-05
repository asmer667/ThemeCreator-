package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.futo.themecreator.data.*
import kotlinx.coroutines.launch

/**
 * تبويب الخطوط — يعرض كتالوج Google Fonts ويسمح بالتحميل والاختيار.
 */
@Composable
fun FontsSheet() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var tab by remember { mutableStateOf(0) }
    val tabs = listOf("🇸🇦 عربية", "🇬🇧 إنجليزية")
    val fonts = if (tab == 0) FontCatalog.ARABIC else FontCatalog.ENGLISH

    // حالة التحميل: fileName → true/false/null
    val downloading = remember { mutableStateMapOf<String, Boolean>() }
    // يُستخدم للتحديث عند اكتمال تحميل
    val version = ThemeState.fontsVersion

    // الخط المُختار حاليًا
    val selected = ThemeState.theme.fontName

    Column(Modifier.fillMaxWidth()) {
        Text(
            "🔤 الخطوط",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            "اختر خطًا للكيبورد. الخطوط المحمّلة تُطبَّق فورًا في المعاينة وتُصدَّر مع الثيم.",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(12.dp))

        TabRow(selectedTabIndex = tab) {
            tabs.forEachIndexed { i, label ->
                Tab(
                    selected = tab == i,
                    onClick = { tab = i },
                    text = { Text(label) }
                )
            }
        }

        Spacer(Modifier.height(8.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fonts, key = { it.id }) { font ->
                val isDownloaded = remember(font.id, version) {
                    FontDownloader.isDownloaded(context, font.fileName)
                }
                val isSelected = (selected == font.fileName)
                val isDownloading = downloading[font.fileName] == true

                FontCard(
                    font = font,
                    isDownloaded = isDownloaded,
                    isSelected = isSelected,
                    isDownloading = isDownloading,
                    onDownload = {
                        downloading[font.fileName] = true
                        scope.launch {
                            val ok = FontDownloader.download(context, font)
                            downloading[font.fileName] = false
                            if (ok) {
                                ThemeState.fontsVersion = ThemeState.fontsVersion + 1
                            }
                        }
                    },
                    onSelect = {
                        ThemeState.replace(
                            ThemeState.theme.copy(fontName = font.fileName)
                        )
                    },
                    onDelete = {
                        FontDownloader.delete(context, font.fileName)
                        ThemeState.fontsVersion = ThemeState.fontsVersion + 1
                        // إن كان هذا الخط مُختارًا، ألغِ الاختيار
                        if (ThemeState.theme.fontName == font.fileName) {
                            ThemeState.replace(ThemeState.theme.copy(fontName = null))
                        }
                    }
                )
            }
        }
        Spacer(Modifier.height(24.dp))
    }
}

@Composable
private fun FontCard(
    font: FontInfo,
    isDownloaded: Boolean,
    isSelected: Boolean,
    isDownloading: Boolean,
    onDownload: () -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth().then(
            if (isSelected)
                Modifier.border(
                    2.dp,
                    MaterialTheme.colorScheme.primary,
                    RoundedCornerShape(12.dp)
                )
            else Modifier
        ),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected)
                MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Row(
            Modifier.padding(12.dp).fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // معلومات الخط
            Column(Modifier.weight(1f)) {
                Text(
                    font.displayName,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    "${font.category} · ${font.fileName}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // أزرار التحكم
            when {
                isDownloading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        strokeWidth = 2.dp
                    )
                }
                isDownloaded -> {
                    if (isSelected) {
                        Icon(
                            Icons.Default.Check,
                            "مختار",
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(24.dp)
                        )
                    } else {
                        TextButton(onClick = onSelect) {
                            Text("اختيار", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                    IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                        Icon(
                            Icons.Default.Delete,
                            "حذف",
                            tint = MaterialTheme.colorScheme.error,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
                else -> {
                    FilledTonalButton(
                        onClick = onDownload,
                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                    ) {
                        Icon(
                            Icons.Default.Download,
                            null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(Modifier.width(4.dp))
                        Text("تحميل", style = MaterialTheme.typography.labelMedium)
                    }
                }
            }
        }
    }
}
