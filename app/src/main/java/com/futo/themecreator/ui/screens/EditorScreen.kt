package com.futo.themecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.data.ThemeStorage
import com.futo.themecreator.ui.components.KeyboardPreview
import com.futo.themecreator.ui.sheets.*

enum class EditorSheet { COLORS, PRESETS, READY2, SHAPES, ICONS, BACKGROUND, EXPORT, NONE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val theme = ThemeState.theme
    var currentSheet by remember { mutableStateOf(EditorSheet.NONE) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(theme.name, fontWeight = FontWeight.Bold, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "رجوع")
                    }
                },
                actions = {
                    IconButton(onClick = {
                        ThemeStorage.save(context, ThemeState.theme)
                    }) {
                        Icon(Icons.Default.Save, "حفظ")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {
            // شريط Tabs قابل للتمرير
            ScrollableTabRow(
                selectedTabIndex = 0,
                edgePadding = 8.dp,
                containerColor = MaterialTheme.colorScheme.surfaceContainerLow
            ) {
                EditorTab("🎨 ألوان") { currentSheet = EditorSheet.COLORS }
                EditorTab("✨ جاهزة") { currentSheet = EditorSheet.PRESETS }
                EditorTab("🎯 جاهزة 2") { currentSheet = EditorSheet.READY2 }
                EditorTab("🔷 أشكال") { currentSheet = EditorSheet.SHAPES }
                EditorTab("🖼️ أيقونات") { currentSheet = EditorSheet.ICONS }
                EditorTab("🌄 خلفية") { currentSheet = EditorSheet.BACKGROUND }
                EditorTab("📤 تصدير") { currentSheet = EditorSheet.EXPORT }
            }

            // المعاينة الحية
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                KeyboardPreview(theme)
            }
        }
    }

    if (currentSheet != EditorSheet.NONE) {
        ModalBottomSheet(
            onDismissRequest = { currentSheet = EditorSheet.NONE },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            when (currentSheet) {
                EditorSheet.COLORS -> ColorsSheet()
                EditorSheet.PRESETS -> PresetsSheet()
                EditorSheet.READY2 -> Ready2Sheet()
                EditorSheet.SHAPES -> ShapesSheet()
                EditorSheet.ICONS -> IconsSheet()
                EditorSheet.BACKGROUND -> BackgroundSheet()
                EditorSheet.EXPORT -> ExportSheet(onDismiss = { currentSheet = EditorSheet.NONE })
                EditorSheet.NONE -> {}
            }
        }
    }
}

@Composable
private fun EditorTab(label: String, onClick: () -> Unit) {
    Tab(
        selected = false,
        onClick = onClick,
        text = { Text(label, style = MaterialTheme.typography.labelMedium) }
    )
}
