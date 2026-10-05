package com.futo.themecreator.ui.screens

import android.content.Intent
import android.os.Environment
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.futo.themecreator.data.*
import java.io.File

@Composable
fun ExportScreen() {
    val context = LocalContext.current
    val theme = ThemeState.theme
    val icons = ThemeState.icons

    var status by remember { mutableStateOf("") }
    var lastFile by remember { mutableStateOf<File?>(null) }
    var showSaved by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
            .padding(16.dp)
    ) {
        Text(
            "📦 تصدير الثيم",
            style = MaterialTheme.typography.headlineSmall
        )
        Text(
            "احفظ الثيم كملف ZIP جاهز للاستيراد",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(16.dp))

        // معلومات الثيم
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primaryContainer
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "📋 ملخص",
                    style = MaterialTheme.typography.titleSmall,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text("الاسم: ${theme.name}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("ID: ${theme.id}", color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("الألوان: 44", color = MaterialTheme.colorScheme.onPrimaryContainer)
                Text("الأيقونات: ${icons.size}", color = MaterialTheme.colorScheme.onPrimaryContainer)
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // زر التصدير
        Button(
            onClick = {
                try {
                    val dir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "FUTOThemes"
                    )
                    dir.mkdirs()
                    val fileName = "${theme.id.replace(".", "_")}.zip"
                    val outFile = File(dir, fileName)

                    ZipPacker.pack(context, theme, icons, outFile)

                    lastFile = outFile
                    status = "✅ تم الحفظ في:\n${outFile.absolutePath}"
                    showSaved = true
                } catch (e: Exception) {
                    status = "❌ خطأ: ${e.message}"
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
        ) {
            Icon(Icons.Default.Check, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("تصدير ZIP", style = MaterialTheme.typography.titleMedium)
        }

        Spacer(modifier = Modifier.height(8.dp))

        // زر المشاركة
        OutlinedButton(
            onClick = {
                lastFile?.let { file ->
                    try {
                        val intent = Intent(Intent.ACTION_SEND).apply {
                            type = "application/zip"
                            putExtra(Intent.EXTRA_STREAM, android.net.Uri.fromFile(file))
                        }
                        context.startActivity(Intent.createChooser(intent, "مشاركة الثيم"))
                    } catch (e: Exception) {
                        status = "❌ خطأ في المشاركة: ${e.message}"
                    }
                }
            },
            enabled = lastFile != null,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(Icons.Default.Share, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("مشاركة")
        }

        Spacer(modifier = Modifier.height(8.dp))

        // زر حفظ المشروع (JSON)
        OutlinedButton(
            onClick = {
                try {
                    val dir = File(
                        Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
                        "FUTOThemes/projects"
                    )
                    dir.mkdirs()
                    val file = File(dir, "${theme.id}.json")
                    file.writeText(ThemeSerializer.save(theme))
                    status = "✅ تم حفظ المشروع في:\n${file.absolutePath}"
                } catch (e: Exception) {
                    status = "❌ خطأ: ${e.message}"
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("💾 حفظ المشروع (JSON)")
        }

        // الحالة
        if (status.isNotEmpty()) {
            Spacer(modifier = Modifier.height(16.dp))
            Card(
                modifier = Modifier.fillMaxWidth(),
                colors = CardDefaults.cardColors(
                    containerColor = if (showSaved)
                        MaterialTheme.colorScheme.tertiaryContainer
                    else
                        MaterialTheme.colorScheme.errorContainer
                )
            ) {
                Text(
                    status,
                    modifier = Modifier.padding(12.dp),
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // تعليمات
        Card(
            modifier = Modifier.fillMaxWidth(),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceVariant
            )
        ) {
            Column(modifier = Modifier.padding(12.dp)) {
                Text(
                    "📖 كيف تستورد الثيم؟",
                    style = MaterialTheme.typography.titleSmall
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    "1. افتح FUTO Keyboard\n" +
                    "2. الإعدادات → الثيمات\n" +
                    "3. اضغط زر ➕ (أو ⚙️ المزيد → استيراد)\n" +
                    "4. اختر ملف ZIP\n" +
                    "5. اختر الثيم من القائمة",
                    style = MaterialTheme.typography.bodySmall
                )
            }
        }
    }
}
