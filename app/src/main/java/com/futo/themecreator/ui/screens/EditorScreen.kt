package com.futo.themecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.data.ThemeStorage
import com.futo.themecreator.ui.components.KeyboardPreview
import com.futo.themecreator.ui.sheets.*

enum class EditorSheet { COLORS, SHAPES, ICONS, PRESETS, EXPORT, NONE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val theme = ThemeState.theme
    var currentSheet by remember { mutableStateOf(EditorSheet.NONE) }
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = false)

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
                    IconButton(onClick = { ThemeStorage.save(context, ThemeState.theme) }) {
                        Icon(Icons.Default.Save, "حفظ")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier.fillMaxSize().padding(padding)
        ) {
            // صف الأزرار العائمة
            FloatingButtonsRow(
                onColorsClick = { currentSheet = EditorSheet.COLORS },
                onShapesClick = { currentSheet = EditorSheet.SHAPES },
                onIconsClick = { currentSheet = EditorSheet.ICONS },
                onPresetsClick = { currentSheet = EditorSheet.PRESETS },
                onExportClick = { currentSheet = EditorSheet.EXPORT },
            )

            // معاينة الكيبورد
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState()),
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
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        ) {
            when (currentSheet) {
                EditorSheet.COLORS -> ColorsSheet()
                EditorSheet.SHAPES -> ShapesSheet()
                EditorSheet.ICONS -> IconsSheet()
                EditorSheet.PRESETS -> PresetsSheet()
                EditorSheet.EXPORT -> ExportSheet(onDismiss = { currentSheet = EditorSheet.NONE })
                EditorSheet.NONE -> {}
            }
        }
    }
}

@Composable
private fun FloatingButtonsRow(
    onColorsClick: () -> Unit,
    onShapesClick: () -> Unit,
    onIconsClick: () -> Unit,
    onPresetsClick: () -> Unit,
    onExportClick: () -> Unit,
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        color = MaterialTheme.colorScheme.surfaceVariant,
        shadowElevation = 4.dp
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            SmallFab(
                icon = Icons.Default.AutoAwesome,
                label = "جاهزة",
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer,
                onClick = onPresetsClick
            )
            SmallFab(
                icon = Icons.Default.Palette,
                label = "ألوان",
                color = MaterialTheme.colorScheme.secondaryContainer,
                contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                onClick = onColorsClick
            )
            SmallFab(
                icon = Icons.Default.Category,
                label = "أشكال",
                color = MaterialTheme.colorScheme.tertiaryContainer,
                contentColor = MaterialTheme.colorScheme.onTertiaryContainer,
                onClick = onShapesClick
            )
            SmallFab(
                icon = Icons.Default.Image,
                label = "أيقونات",
                color = MaterialTheme.colorScheme.surfaceVariant,
                contentColor = MaterialTheme.colorScheme.onSurfaceVariant,
                onClick = onIconsClick
            )
            Spacer(Modifier.weight(1f))
            SmallFab(
                icon = Icons.Default.Upload,
                label = "تصدير",
                color = MaterialTheme.colorScheme.errorContainer,
                contentColor = MaterialTheme.colorScheme.onErrorContainer,
                onClick = onExportClick
            )
        }
    }
}

@Composable
private fun SmallFab(
    icon: ImageVector,
    label: String,
    color: androidx.compose.ui.graphics.Color,
    contentColor: androidx.compose.ui.graphics.Color,
    onClick: () -> Unit,
) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        FloatingActionButton(
            onClick = onClick,
            containerColor = color,
            contentColor = contentColor,
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.size(52.dp)
        ) {
            Icon(icon, label, modifier = Modifier.size(22.dp))
        }
        Spacer(Modifier.height(2.dp))
        Text(label, style = MaterialTheme.typography.labelSmall, color = contentColor)
    }
}
