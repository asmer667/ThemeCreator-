package com.futo.themecreator.ui.sheets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.IconManager
import com.futo.themecreator.data.IconSpec
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.ui.components.IconCard

@Composable
fun IconsSheet() {
    val icons = ThemeState.icons
    var pendingIcon by remember { mutableStateOf<IconSpec?>(null) }
    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null && pendingIcon != null) {
            ThemeState.setIcon(pendingIcon!!.fileName, uri)
        }
        pendingIcon = null
    }

    LazyColumn(
        modifier = Modifier.fillMaxWidth(),
        contentPadding = PaddingValues(bottom = 32.dp)
    ) {
        item {
            Text("🖼️ الأيقونات", style = MaterialTheme.typography.titleLarge,
                fontWeight = FontWeight.Bold, modifier = Modifier.padding(16.dp))
            Text("ارفع صور PNG مخصصة (9-slice)",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.padding(horizontal = 16.dp))
            Spacer(Modifier.height(12.dp))
        }
        items(IconManager.supportedIcons) { spec ->
            IconCard(
                spec = spec,
                currentUri = icons[spec.fileName],
                onPickImage = { pendingIcon = spec; picker.launch("image/*") },
                onClear = { ThemeState.setIcon(spec.fileName, null) }
            )
        }
    }
}
