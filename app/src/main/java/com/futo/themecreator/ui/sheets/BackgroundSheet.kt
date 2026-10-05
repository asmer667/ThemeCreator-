package com.futo.themecreator.ui.sheets

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddPhotoAlternate
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.futo.themecreator.data.ThemeState

@Composable
fun BackgroundSheet() {
    val bgUri = ThemeState.backgroundImageUri
    var pendingUri by remember { mutableStateOf<Uri?>(null) }

    val picker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) ThemeState.setBackgroundImage(uri)
        pendingUri = null
    }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("🖼️ صورة الخلفية",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(4.dp))
        Text("صورة تظهر خلف الأزرار (مثل Gboard)",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant)
        Spacer(Modifier.height(16.dp))

        // Preview
        Card(
            modifier = Modifier.fillMaxWidth().height(200.dp),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHigh
            )
        ) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                if (bgUri != null) {
                    AsyncImage(
                        model = bgUri,
                        contentDescription = "Background",
                        contentScale = ContentScale.Crop,
                        alpha = ThemeState.backgroundImageOpacity,
                        modifier = Modifier.fillMaxSize().clip(RoundedCornerShape(16.dp))
                    )
                } else {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Icon(Icons.Default.AddPhotoAlternate, null,
                            modifier = Modifier.size(64.dp),
                            tint = MaterialTheme.colorScheme.onSurfaceVariant)
                        Spacer(Modifier.height(8.dp))
                        Text("لم يتم اختيار صورة",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant)
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        Button(
            onClick = { pendingUri = null; picker.launch("image/*") },
            modifier = Modifier.fillMaxWidth().height(52.dp)
        ) {
            Icon(Icons.Default.AddPhotoAlternate, null)
            Spacer(Modifier.width(8.dp))
            Text(if (bgUri == null) "اختيار صورة" else "تغيير الصورة")
        }

        if (bgUri != null) {
            Spacer(Modifier.height(8.dp))
            OutlinedButton(
                onClick = { ThemeState.setBackgroundImage(null) },
                modifier = Modifier.fillMaxWidth(),
                colors = ButtonDefaults.outlinedButtonColors(
                    contentColor = MaterialTheme.colorScheme.error
                )
            ) {
                Icon(Icons.Default.Delete, null)
                Spacer(Modifier.width(8.dp))
                Text("حذف الصورة")
            }

            Spacer(Modifier.height(16.dp))
            Text("شفافية الصورة: ${(ThemeState.backgroundImageOpacity * 100).toInt()}%",
                style = MaterialTheme.typography.bodyMedium)
            Slider(
                value = ThemeState.backgroundImageOpacity,
                onValueChange = { ThemeState.setBackgroundOpacity(it) },
                valueRange = 0.1f..1f
            )
        }
        Spacer(Modifier.height(32.dp))
    }
}
