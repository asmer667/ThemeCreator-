package com.futo.themecreator.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun HomeScreen(
    onCreate: () -> Unit,
    onEdit: () -> Unit,
    onExport: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    listOf(Color(0xFF0A0A1A), Color(0xFF1A0A2A))
                )
            )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(32.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text("🎨", style = MaterialTheme.typography.displayLarge)
            Spacer(Modifier.height(16.dp))
            Text(
                "Welcome to FUTO",
                style = MaterialTheme.typography.headlineMedium,
                fontWeight = FontWeight.Bold,
                color = Color.White,
                textAlign = TextAlign.Center
            )
            Text(
                "Theme Designer",
                style = MaterialTheme.typography.headlineSmall,
                color = Color(0xFF00BFFF),
                fontWeight = FontWeight.Bold
            )
            Spacer(Modifier.height(8.dp))
            Text(
                "صمّم ثيمات كيبوردك باحترافية",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f),
                textAlign = TextAlign.Center
            )
            Spacer(Modifier.height(48.dp))

            BigButton(
                icon = Icons.Default.Add,
                title = "Create New Theme",
                subtitle = "إنشاء ثيم جديد من الصفر",
                gradient = listOf(Color(0xFF4FC3F7), Color(0xFF1976D2)),
                onClick = onCreate
            )
            Spacer(Modifier.height(16.dp))
            BigButton(
                icon = Icons.Default.Edit,
                title = "Edit Existing Theme",
                subtitle = "افتح ثيماً موجوداً وعدّله",
                gradient = listOf(Color(0xFFBA68C8), Color(0xFF6A1B9A)),
                onClick = onEdit
            )
            Spacer(Modifier.height(16.dp))
            BigButton(
                icon = Icons.Default.Upload,
                title = "Export Themes",
                subtitle = "تصدير الثيمات كملف ZIP",
                gradient = listOf(Color(0xFF4DB6AC), Color(0xFF00695C)),
                onClick = onExport
            )
        }
    }
}

@Composable
private fun BigButton(
    icon: ImageVector,
    title: String,
    subtitle: String,
    gradient: List<Color>,
    onClick: () -> Unit,
) {
    Card(
        onClick = onClick,
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp)
            .clip(RoundedCornerShape(20.dp)),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent)
    ) {
        Box(
            Modifier
                .fillMaxSize()
                .background(Brush.horizontalGradient(gradient))
        ) {
            Row(
                Modifier
                    .fillMaxSize()
                    .padding(20.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = Color.White.copy(alpha = 0.2f),
                    modifier = Modifier.size(56.dp)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, title, tint = Color.White, modifier = Modifier.size(28.dp))
                    }
                }
                Spacer(Modifier.width(16.dp))
                Column(Modifier.weight(1f)) {
                    Text(title, style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold, color = Color.White)
                    Text(subtitle, style = MaterialTheme.typography.bodySmall,
                        color = Color.White.copy(alpha = 0.85f))
                }
            }
        }
    }
}
