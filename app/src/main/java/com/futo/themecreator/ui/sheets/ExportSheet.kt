package com.futo.themecreator.ui.sheets

import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.data.ZipPacker
import java.io.File

@Composable
fun ExportSheet(onDismiss: () -> Unit) {
    val context = LocalContext.current
    var themeName by remember { mutableStateOf(ThemeState.theme.name) }
    var fileName by remember { mutableStateOf(ThemeState.theme.id.replace(".", "_")) }
    var status by remember { mutableStateOf("") }
    var lastFile by remember { mutableStateOf<File?>(null) }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("📦 تصدير الثيم",
            style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(12.dp))

        OutlinedTextField(
            value = themeName,
            onValueChange = { themeName = it },
            label = { Text("اسم الثيم (يظهر في الكيبورد)") },
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(Modifier.height(8.dp))
        OutlinedTextField(
            value = fileName,
            onValueChange = { fileName = it },
            label = { Text("اسم الملف") },
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(Modifier.height(12.dp))
        Text("الأيقونات: ${ThemeState.icons.size}",
            style = MaterialTheme.typography.bodySmall)
        Text("الأشكال 3D: ${ThemeState.shapes3D.size}",
            style = MaterialTheme.typography.bodySmall)
        Text("الخلفية: ${if (ThemeState.backgroundImageUri != null) "موجودة" else "لا يوجد"}",
            style = MaterialTheme.typography.bodySmall)

        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val updated = ThemeState.theme.copy(name = themeName)
                    ThemeState.replace(updated)

                    val dir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "FUTOThemes"
                    )
                    dir.mkdirs()
                    val safeName = fileName.replace(Regex("[^a-zA-Z0-9_-]"), "_")
                    val outFile = File(dir, "$safeName.zip")

                    ZipPacker.pack(
                        context, updated, ThemeState.icons,
                        ThemeState.shapes3D, ThemeState.backgroundImageUri, outFile
                    )
                    lastFile = outFile
                    status = "✅ تم الحفظ:\n${outFile.absolutePath}"
                } catch (e: Exception) {
                    status = "❌ خطأ: ${e.message}"
                }
            },
            modifier = Modifier.fillMaxWidth().height(56.dp)
        ) {
            Icon(Icons.Default.Check, null)
            Spacer(Modifier.width(8.dp))
            Text("تصدير ZIP")
        }

        Spacer(Modifier.height(8.dp))
        OutlinedButton(
            onClick = {
                lastFile?.let { file ->
                    try {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/zip"
                            putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(file))
                        }
                        context.startActivity(Intent.createChooser(intent, "مشاركة الثيم"))
                    } catch (e: Exception) { status = "❌ ${e.message}" }
                }
            },
            enabled = lastFile != null,
            modifier = Modifier.fillMaxWidth()
        ) {
            Icon(Icons.Default.Share, null)
            Spacer(Modifier.width(8.dp))
            Text("مشاركة")
        }

        if (status.isNotEmpty()) {
            Spacer(Modifier.height(12.dp))
            Card { Text(status, Modifier.padding(12.dp)) }
        }
        Spacer(Modifier.height(16.dp))
    }
}
