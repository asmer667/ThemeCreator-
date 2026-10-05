package com.futo.themecreator.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ZoomIn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.data.ThemeStorage
import com.futo.themecreator.ui.components.KeyboardPreview
import com.futo.themecreator.ui.sheets.*
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.ui.draw.shadow

enum class EditorSheet { COLORS, PRESETS, READY2, SHAPES, ICONS, BUTTONS, FONTS, BACKGROUND, EXPORT, TEXT, NONE }

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditorScreen(onBack: () -> Unit) {
    val context = LocalContext.current
    val theme = ThemeState.theme

    var currentSheet by remember { mutableStateOf(EditorSheet.NONE) }
    var previewScale by remember { mutableStateOf(1.4f) }
    var showZoomBar by remember { mutableStateOf(false) }
    var savedFlash by remember { mutableStateOf(false) }

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    // شريط الحالة الأخضر المؤقت بعد الحفظ
    LaunchedEffect(savedFlash) {
        if (savedFlash) {
            kotlinx.coroutines.delay(1200)
            savedFlash = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(theme.name, fontWeight = FontWeight.Bold, maxLines = 1)
                        Text(
                            "معاينة حية",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "رجوع")
                    }
                },
                actions = {
                    // زر تكبير المعاينة
                    IconButton(onClick = { showZoomBar = !showZoomBar }) {
                        Icon(
                            Icons.Default.ZoomIn,
                            "تكبير المعاينة",
                            tint = if (showZoomBar) MaterialTheme.colorScheme.primary
                                   else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    // زر الحفظ
                    IconButton(onClick = {
                        ThemeStorage.save(context, ThemeState.theme)
                        savedFlash = true
                    }) {
                        Icon(
                            Icons.Default.Save,
                            "حفظ",
                            tint = if (savedFlash) MaterialTheme.colorScheme.primary
                                   else LocalContentColor.current
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        }
    ) { padding ->
        Column(Modifier.fillMaxSize().padding(padding)) {

            // ─── شريط التكبير (يظهر عند الضغط على 🔍) ───
            AnimatedVisibility(
                visible = showZoomBar,
                enter = slideInVertically() + fadeIn(),
                exit  = slideOutVertically() + fadeOut()
            ) {
                Surface(
                    color = MaterialTheme.colorScheme.surfaceContainerHigh,
                    tonalElevation = 2.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(
                        Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            "حجم المعاينة",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(Modifier.width(12.dp))
                        Slider(
                            value = previewScale,
                            onValueChange = { previewScale = it },
                            valueRange = 0.8f..2.0f,
                            steps = 23,
                            modifier = Modifier.weight(1f)
                        )
                        Spacer(Modifier.width(12.dp))
                        Text(
                            "${(previewScale * 100).toInt()}%",
                            style = MaterialTheme.typography.labelMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // ─── شريط التبويبات ───
            PillTabRow(
                items = listOf(
                    PillTabItem("🎨 ألوان",    EditorSheet.COLORS),
                    PillTabItem("✨ جاهزة",    EditorSheet.PRESETS),
                    PillTabItem("🎯 جاهزة 2",  EditorSheet.READY2),
                    PillTabItem("🔷 أشكال",    EditorSheet.SHAPES),
                    PillTabItem("🖼️ أيقونات",  EditorSheet.ICONS),
                    PillTabItem("🔘 أزرار",    EditorSheet.BUTTONS),
                    PillTabItem("🔤 خطوط",     EditorSheet.FONTS),
                    PillTabItem("📝 نص",       EditorSheet.TEXT),
                    PillTabItem("🌄 خلفية",    EditorSheet.BACKGROUND),
                    PillTabItem("📤 تصدير",    EditorSheet.EXPORT),
                ),
                selected = currentSheet,
                onSelect = { currentSheet = it },
            )

            // ─── المعاينة الحية ───
            Box(
                Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.background)
                    .verticalScroll(rememberScrollState())
                    .padding(12.dp),
                contentAlignment = Alignment.Center
            ) {
                Crossfade(
                    targetState = theme,
                    animationSpec = tween(180),
                    label = "preview-crossfade"
                ) { animatedTheme ->
                    KeyboardPreview(theme = animatedTheme, scale = previewScale)
                }
            }
        }
    }

    // ─── BottomSheet مع أنيميشن ───
    if (currentSheet != EditorSheet.NONE) {
        ModalBottomSheet(
            onDismissRequest = { currentSheet = EditorSheet.NONE },
            sheetState = sheetState,
            containerColor = MaterialTheme.colorScheme.surfaceContainerLow
        ) {
            Crossfade(targetState = currentSheet, animationSpec = tween(150)) { sheet ->
                when (sheet) {
                    EditorSheet.COLORS     -> ColorsSheet()
                    EditorSheet.PRESETS    -> PresetsSheet()
                    EditorSheet.READY2     -> Ready2Sheet()
                    EditorSheet.SHAPES     -> ShapesSheet()
                    EditorSheet.ICONS      -> IconsSheet()
                    EditorSheet.BUTTONS    -> ButtonImagesSheet()
                    EditorSheet.FONTS -> FontsSheet()
                    EditorSheet.TEXT -> TextSheet()
                    EditorSheet.BACKGROUND -> BackgroundSheet()
                    EditorSheet.EXPORT     -> ExportSheet(onDismiss = { currentSheet = EditorSheet.NONE })
                    EditorSheet.NONE       -> {}
                }
            }
        }
    }
}

// ═══════════════════════════════════════════════════════════════
//  Pill Tabs — نمط عصري بدون أي API experimental
// ═══════════════════════════════════════════════════════════════

data class PillTabItem(val label: String, val sheet: EditorSheet)

@Composable
private fun PillTabRow(
    items: List<PillTabItem>,
    selected: EditorSheet,
    onSelect: (EditorSheet) -> Unit,
) {
    val scrollState = rememberScrollState()

    LaunchedEffect(selected) {
        val idx = items.indexOfFirst { it.sheet == selected }
        if (idx >= 0) {
            try {
                scrollState.animateScrollTo((idx * 110).coerceAtLeast(0))
            } catch (e: Exception) { /* تجاهل */ }
        }
    }

    Box(
        Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surfaceContainerLow)
    ) {
        Row(
            Modifier
                .fillMaxWidth()
                .horizontalScroll(scrollState)
                .padding(horizontal = 12.dp, vertical = 10.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            items.forEach { item ->
                PillTab(
                    label = item.label,
                    selected = item.sheet == selected,
                    onClick = { onSelect(item.sheet) }
                )
            }
        }
    }
}

@Composable
private fun PillTab(
    label: String,
    selected: Boolean,
    onClick: () -> Unit,
) {
    val bgColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.primary
        else
            MaterialTheme.colorScheme.surfaceContainerHigh,
        animationSpec = tween(220),
        label = "pill-bg"
    )
    val fgColor by animateColorAsState(
        targetValue = if (selected)
            MaterialTheme.colorScheme.onPrimary
        else
            MaterialTheme.colorScheme.onSurfaceVariant,
        animationSpec = tween(220),
        label = "pill-fg"
    )

    Box(
        Modifier
            .height(38.dp)
            .clip(RoundedCornerShape(50))
            .background(bgColor)
            .then(
                if (selected)
                    Modifier.shadow(
                        elevation = 3.dp,
                        shape = RoundedCornerShape(50),
                        clip = false
                    )
                else Modifier
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(
            label,
            style = MaterialTheme.typography.labelLarge,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = fgColor,
            maxLines = 1
        )
    }
}
