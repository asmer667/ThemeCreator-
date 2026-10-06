package com.futo.themecreator.ui.sheets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.FileOpen
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

@Composable
fun FontsSheet() {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var tab by remember { mutableStateOf(0) }
    val tabs = listOf(
        "\ud83c\uddf8\ud83c\udde6 \u0639\u0631\u0628\u064a\u0629",
        "\ud83c\uddec\ud83c\udde7 \u0625\u0646\u062c\u0644\u064a\u0632\u064a\u0629",
        "\ud83d\udcbe \u0627\u0644\u0645\u062d\u0645\u0651\u0644\u0629"
    )

    val downloading = remember { mutableStateMapOf<String, Float>() }
    val version = ThemeState.fontsVersion
    val selected = ThemeState.theme.fontName

    // launcher لاستيراد خط عربي
    val importerArabic = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val target = FontDownloader.fontFile(context, "imported_arabic.ttf")
                if (FontDownloader.importFromUri(context, uri, target)) {
                    ThemeState.fontsVersion = ThemeState.fontsVersion + 1
                    ThemeState.replace(ThemeState.theme.copy(fontName = "imported_arabic.ttf"))
                }
            }
        }
    }

    // launcher لاستيراد خط إنجليزي
    val importerEnglish = rememberLauncherForActivityResult(
        ActivityResultContracts.OpenDocument()
    ) { uri: Uri? ->
        if (uri != null) {
            scope.launch {
                val target = FontDownloader.fontFile(context, "imported_english.ttf")
                if (FontDownloader.importFromUri(context, uri, target)) {
                    ThemeState.fontsVersion = ThemeState.fontsVersion + 1
                    ThemeState.replace(ThemeState.theme.copy(fontName = "imported_english.ttf"))
                }
            }
        }
    }

    Column(Modifier.fillMaxWidth()) {
        Text(
            "\ud83d\udd24 \u0627\u0644\u062e\u0637\u0648\u0637",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )

        Button(
            onClick = {
                importerArabic.launch(arrayOf(
                    "font/ttf", "font/otf",
                    "application/x-font-ttf",
                    "application/x-font-otf",
                    "*/*"
                ))
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Default.FileOpen, null)
            Spacer(Modifier.width(8.dp))
            Text("\ud83d\udcc2 \u0627\u0633\u062a\u064a\u0631\u0627\u062f \u062e\u0637 \u0639\u0631\u0628\u064a")
        }

        Spacer(Modifier.height(8.dp))

        Button(
            onClick = {
                importerEnglish.launch(arrayOf(
                    "font/ttf", "font/otf",
                    "application/x-font-ttf",
                    "application/x-font-otf",
                    "*/*"
                ))
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Icon(Icons.Default.FileOpen, null)
            Spacer(Modifier.width(8.dp))
            Text("\ud83d\udcc2 \u0627\u0633\u062a\u064a\u0631\u0627\u062f \u062e\u0637 \u0625\u0646\u062c\u0644\u064a\u0632\u064a")
        }

        Spacer(Modifier.height(8.dp))

        OutlinedButton(
            onClick = {
                scope.launch {
                    try {
                        FontCatalog.fetchAll()
                        ThemeState.fontsVersion = ThemeState.fontsVersion + 1
                    } catch (e: Exception) { e.printStackTrace() }
                }
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("\ud83c\udf10 \u062c\u0644\u0628 \u0627\u0644\u0645\u0632\u064a\u062f \u0645\u0646 \u0627\u0644\u062e\u0637\u0648\u0637")
        }



        Spacer(Modifier.height(12.dp))

        TabRow(selectedTabIndex = tab) {
            tabs.forEachIndexed { i, label ->
                Tab(selected = tab == i, onClick = { tab = i }, text = { Text(label, fontSize = 11.sp) })
            }
        }

        Spacer(Modifier.height(8.dp))

        val fonts: List<FontInfo> = when (tab) {
            0 -> FontCatalog.ARABIC
            1 -> FontCatalog.ENGLISH
            else -> FontDownloader.listDownloaded(context).mapNotNull { fileName ->
                FontCatalog.BUILT_IN.find { it.fileName == fileName }
                    ?: FontInfo(
                        id = fileName,
                        displayName = fileName.removeSuffix(".ttf").removeSuffix(".otf"),
                        fileName = fileName,
                        url = "",
                        language = FontLanguage.OTHER,
                        category = FontCategory.SANS,
                        source = FontSource.IMPORTED,
                    )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxWidth().heightIn(max = 500.dp),
            contentPadding = PaddingValues(12.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(fonts, key = { it.id }) { font ->
                val isDownloaded = remember(font.id, version) {
                    FontDownloader.fontExists(context, font.fileName)
                }
                val isSelected = selected == font.fileName
                val progress = downloading[font.fileName]

                FontCard(
                    font = font,
                    isDownloaded = isDownloaded,
                    isSelected = isSelected,
                    progress = progress,
                    onDownload = {
                        downloading[font.fileName] = 0f
                        scope.launch {
                            // نحل الـ URL الحقيقي عبر Google Fonts CSS API
                            val resolvedUrl = if (font.source == FontSource.GOOGLE_API) {
                                FontCatalog.resolveGoogleFontUrl(font.displayName) ?: font.url
                            } else font.url
                            val fontToDownload = if (resolvedUrl != font.url) font.copy(url = resolvedUrl) else font
                            val ok = FontDownloader.downloadWithProgress(context, fontToDownload) { cur, tot ->
                                if (tot > 0) downloading[font.fileName] = cur.toFloat() / tot
                            }
                            downloading.remove(font.fileName)
                            if (ok) ThemeState.fontsVersion = ThemeState.fontsVersion + 1
                        }
                    },
                    onSelect = {
                        ThemeState.replace(ThemeState.theme.copy(fontName = font.fileName))
                    },
                    onDelete = {
                        FontDownloader.delete(context, font.fileName)
                        ThemeState.fontsVersion = ThemeState.fontsVersion + 1
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
    progress: Float?,
    onDownload: () -> Unit,
    onSelect: () -> Unit,
    onDelete: () -> Unit,
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isSelected) MaterialTheme.colorScheme.primaryContainer
            else MaterialTheme.colorScheme.surfaceContainer
        )
    ) {
        Column(Modifier.padding(12.dp).fillMaxWidth()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(Modifier.weight(1f)) {
                    Text(font.displayName, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    Text(
                        "${font.category} · ${font.fileName}",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    // معاينة بخط النظام — تعطي انطباعًا أوليًا
                    Text(
                        "Aa \u0623\u0628\u062c\u062f 123",
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurface,
                        maxLines = 1
                    )
                }
                when {
                    progress != null -> {
                        CircularProgressIndicator(
                            progress = { progress },
                            modifier = Modifier.size(28.dp),
                            strokeWidth = 3.dp
                        )
                    }
                    isDownloaded -> {
                        if (isSelected) {
                            Icon(Icons.Default.Check, "مختار",
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(24.dp))
                        } else {
                            TextButton(onClick = onSelect) { Text("\u0627\u062e\u062a\u064a\u0627\u0631") }
                        }
                        IconButton(onClick = onDelete, modifier = Modifier.size(36.dp)) {
                            Icon(Icons.Default.Delete, "حذف",
                                tint = MaterialTheme.colorScheme.error,
                                modifier = Modifier.size(18.dp))
                        }
                    }
                    else -> {
                        FilledTonalButton(
                            onClick = onDownload,
                            contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Icon(Icons.Default.Download, null, modifier = Modifier.size(16.dp))
                            Spacer(Modifier.width(4.dp))
                            Text("\u062a\u062d\u0645\u064a\u0644", style = MaterialTheme.typography.labelMedium)
                        }
                    }
                }
            }
            if (progress != null) {
                Spacer(Modifier.height(8.dp))
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier.fillMaxWidth()
                )
                Spacer(Modifier.height(4.dp))
                Text(
                    "${(progress * 100).toInt()}%",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
