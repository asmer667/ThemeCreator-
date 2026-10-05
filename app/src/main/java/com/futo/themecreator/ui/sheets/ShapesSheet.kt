package com.futo.themecreator.ui.sheets

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.*
import com.futo.themecreator.utils.ColorUtils

/**
 * تبويب الأشكال — 686 شكلًا مولّدًا برمجيًا.
 * عند الضغط على شكل: يفتح BottomSheet بالـ sliders.
 */
@Composable
fun ShapesSheet() {
    val context = LocalContext.current
    val theme = ThemeState.theme
    val selectedKey = ThemeState.selectedShapeKey
    var detailShape by remember { mutableStateOf<ShapeDef?>(null) }

    // اللون الأساسي للعرض
    val previewColor = remember(theme.primary) {
        ColorUtils.parseColor(theme.keyboardContainerVariant).toArgb()
    }

    Column(Modifier.fillMaxWidth()) {
        Text(
            "🔷 الأشكال الهندسية (${ShapesGenerator.shapes.size})",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(16.dp)
        )
        Text(
            "اضغط على أي شكل لتخصيصه بمجموعة المفاتيح الحالية (${selectedKey.display})",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            modifier = Modifier.padding(horizontal = 16.dp)
        )
        Spacer(Modifier.height(12.dp))

        LazyVerticalGrid(
            columns = GridCells.Fixed(4),
            modifier = Modifier.fillMaxWidth().heightIn(max = 520.dp),
            contentPadding = PaddingValues(12.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(ShapesGenerator.shapes, key = { it.id }) { shape ->
                ShapeCell(
                    shape = shape,
                    fillColor = previewColor,
                    isSelected = ThemeState.isShapeSelected(shape.id),
                    onClick = { detailShape = shape }
                )
            }
        }

        Spacer(Modifier.height(12.dp))

        OutlinedButton(
            onClick = {
                ShapeThumbnailCache.clear()
                ThemeState.clearShapeSelection()
            },
            modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
        ) {
            Text("🗑️ إزالة كل الأشكال")
        }
        Spacer(Modifier.height(32.dp))
    }

    // نافذة تفاصيل الشكل
    detailShape?.let { shape ->
        ShapeDetailSheet(
            shape = shape,
            onDismiss = { detailShape = null }
        )
    }
}

@Composable
private fun ShapeCell(
    shape: ShapeDef,
    fillColor: Int,
    isSelected: Boolean,
    onClick: () -> Unit,
) {
    val bitmap = remember(shape.id, fillColor) {
        ShapeThumbnailCache.get(shape.id, fillColor, 0f, 0.5f, 0f)
    }

    Box(
        modifier = Modifier
            .aspectRatio(1f)
            .clip(RoundedCornerShape(14.dp))
            .background(
                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                else MaterialTheme.colorScheme.surfaceContainerHigh
            )
            .border(
                width = if (isSelected) 3.dp else 1.dp,
                color = if (isSelected) MaterialTheme.colorScheme.primary
                        else MaterialTheme.colorScheme.outline.copy(alpha = 0.2f),
                shape = RoundedCornerShape(14.dp)
            )
            .clickable(onClick = onClick),
        contentAlignment = Alignment.Center
    ) {
        Image(
            bitmap = bitmap.asImageBitmap(),
            contentDescription = shape.name,
            modifier = Modifier.fillMaxSize().padding(6.dp)
        )
        if (isSelected) {
            Icon(
                Icons.Default.CheckCircle,
                contentDescription = null,
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.align(Alignment.TopEnd).padding(4.dp).size(18.dp)
            )
        }
    }
}
