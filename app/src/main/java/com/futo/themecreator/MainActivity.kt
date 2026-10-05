package com.futo.themecreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.futo.themecreator.ui.theme.ThemeCreatorTheme
import com.futo.themecreator.ui.screens.*

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ThemeCreatorTheme {
                AppNavigation()
            }
        }
    }
}

@Composable
fun AppNavigation() {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("الألوان", "الأشكال", "الأيقونات", "المعاينة", "التصدير")

    Scaffold(
        bottomBar = {
            NavigationBar {
                tabs.forEachIndexed { index, label ->
                    NavigationBarItem(
                        selected = selectedTab == index,
                        onClick = { selectedTab = index },
                        icon = { Text("${index + 1}") },
                        label = { Text(label) }
                    )
                }
            }
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (selectedTab) {
                0 -> ColorsScreen()
                1 -> ShapesScreen()
                2 -> IconsScreen()
                3 -> PreviewScreen()
                4 -> ExportScreen()
            }
        }
    }
}
