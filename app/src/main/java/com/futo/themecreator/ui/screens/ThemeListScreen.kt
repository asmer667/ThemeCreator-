package com.futo.themecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeData
import com.futo.themecreator.data.ThemeStorage
import com.futo.themecreator.utils.ColorUtils

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ThemeListScreen(
    mode: Mode,
    onBack: () -> Unit,
    onSelect: (ThemeData) -> Unit,
) {
    val context = LocalContext.current
    val themes by ThemeStorage.themes.collectAsState()

    val title = when (mode) {
        Mode.EDIT -> "✏️ اختر ثيماً لتعديله"
        Mode.EXPORT -> "📤 اختر ثيماً لتصديره"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold, maxLines = 1) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, "رجوع")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surfaceContainer
                )
            )
        },
        floatingActionButton = {
            if (mode == Mode.EDIT) {
                ExtendedFloatingActionButton(
                    onClick = {
                        val newTheme = ThemeStorage.createNew(context)
                        onSelect(newTheme)
                    },
                    icon = { Icon(Icons.Default.Add, null) },
                    text = { Text("ثيم جديد") }
                )
            }
        }
    ) { padding ->
        if (themes.isEmpty()) {
            Box(
                Modifier.fillMaxSize().padding(padding),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("📭", style = MaterialTheme.typography.displayLarge)
                    Spacer(Modifier.height(12.dp))
                    Text("لا توجد ثيمات بعد", style = MaterialTheme.typography.titleMedium)
                    Spacer(Modifier.height(4.dp))
                    Text(
                        if (mode == Mode.EDIT) "اضغط + لإنشاء ثيم جديد"
                        else "أنشئ ثيماً أولاً",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }
        } else {
            LazyVerticalGrid(
                columns = GridCells.Adaptive(160.dp),
                modifier = Modifier.fillMaxSize().padding(padding),
                contentPadding = PaddingValues(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(themes, key = { it.id }) { theme ->
                    ThemeCard(
                        theme = theme,
                        onClick = { onSelect(theme) },
                        onDelete = { ThemeStorage.delete(context, theme.id) }
                    )
                }
            }
        }
    }
}

@Composable
private fun ThemeCard(
    theme: ThemeData,
    onClick: () -> Unit,
    onDelete: () -> Unit,
) {
    val primary = ColorUtils.parseColor(theme.primary)
    val bg = ColorUtils.parseColor(theme.background)
    val kbContainer = ColorUtils.parseColor(theme.keyboardContainer)
    val onKb = ColorUtils.parseColor(theme.onKeyboardContainer)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(180.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = bg)
    ) {
        Box(Modifier.fillMaxSize()) {
            Column(Modifier.fillMaxSize().padding(12.dp)) {
                Text(
                    theme.name,
                    style = MaterialTheme.typography.titleMedium,
                    color = onKb,
                    fontWeight = FontWeight.Bold,
                    maxLines = 1
                )
                Spacer(Modifier.height(8.dp))
                Column(
                    modifier = Modifier.weight(1f),
                    verticalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        MiniKey("Q", kbContainer, onKb, theme.roundedness)
                        MiniKey("W", kbContainer, onKb, theme.roundedness)
                        MiniKey("E", kbContainer, onKb, theme.roundedness)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        MiniKey("A", kbContainer, onKb, theme.roundedness)
                        MiniKey("S", kbContainer, onKb, theme.roundedness)
                        MiniKey("D", kbContainer, onKb, theme.roundedness)
                    }
                    Row(horizontalArrangement = Arrangement.spacedBy(3.dp)) {
                        MiniKey("Z", kbContainer, onKb, theme.roundedness)
                        MiniKey("X", kbContainer, onKb, theme.roundedness)
                        MiniKey("C", primary, onKb, theme.roundedness)
                    }
                }
            }
            IconButton(
                onClick = onDelete,
                modifier = Modifier.align(Alignment.TopEnd)
            ) {
                Icon(
                    Icons.Default.Delete,
                    contentDescription = "حذف",
                    tint = ColorUtils.parseColor(theme.error)
                )
            }
        }
    }
}

@Composable
private fun MiniKey(text: String, bg: Color, fg: Color, roundedness: Float) {
    Box(
        modifier = Modifier
            .size(28.dp)
            .clip(RoundedCornerShape((roundedness * 8).dp))
            .background(bg),
        contentAlignment = Alignment.Center
    ) {
        Text(text, color = fg, style = MaterialTheme.typography.labelSmall)
    }
}

enum class Mode { EDIT, EXPORT }
