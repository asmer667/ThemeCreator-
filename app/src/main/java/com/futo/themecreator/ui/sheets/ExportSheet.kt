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
    val theme = ThemeState.theme
    val icons = ThemeState.icons
    var status by remember { mutableStateOf("") }
    var lastFile by remember { mutableStateOf<File?>(null) }

    Column(Modifier.fillMaxWidth().padding(16.dp)) {
        Text("📦 تصدير الثيم", style = MaterialTheme.typography.titleLarge,
            fontWeight = FontWeight.Bold)
        Spacer(Modifier.height(8.dp))
        Text("الاسم: ${theme.name}")
        Text("الأيقونات: ${icons.size}")
        Spacer(Modifier.height(16.dp))

        Button(
            onClick = {
                try {
                    val dir = File(Environment.getExternalStoragePublicDirectory(
                        Environment.DIRECTORY_DOWNLOADS), "FUTOThemes")
                    dir.mkdirs()
                    val outFile = File(dir, "${theme.id.replace(".", "_")}.zip")
                    ZipPacker.pack(context, theme, icons, outFile)
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
                        context.startActivity(Intent.createChooser(intent, "مشاركة"))
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
