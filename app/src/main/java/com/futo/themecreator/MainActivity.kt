package com.futo.themecreator

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.runtime.*
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.futo.themecreator.data.ThemeState
import com.futo.themecreator.data.ThemeStorage
import com.futo.themecreator.ui.screens.EditorScreen
import com.futo.themecreator.ui.screens.HomeScreen
import com.futo.themecreator.ui.screens.ThemeListScreen
import com.futo.themecreator.ui.theme.ThemeCreatorTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        ThemeStorage.load(this)
        setContent {
            ThemeCreatorTheme {
                val nav = rememberNavController()
                NavHost(nav, startDestination = "home") {
                    composable("home") {
                        HomeScreen(
                            onCreate = {
                                val t = ThemeStorage.createNew(this@MainActivity)
                                ThemeState.replace(t)
                                ThemeState.icons = emptyMap()
                                ThemeState.shapes3D = emptySet()
                                nav.navigate("editor")
                            },
                            onEdit = { nav.navigate("list/edit") },
                            onExport = { nav.navigate("list/export") },
                        )
                    }
                    composable("list/edit") {
                        ThemeListScreen(
                            mode = ThemeListScreen.Mode.EDIT,
                            onBack = { nav.popBackStack() },
                            onSelect = { theme ->
                                ThemeState.replace(theme)
                                ThemeState.icons = emptyMap()
                                ThemeState.shapes3D = emptySet()
                                nav.navigate("editor")
                            }
                        )
                    }
                    composable("list/export") {
                        ThemeListScreen(
                            mode = ThemeListScreen.Mode.EXPORT,
                            onBack = { nav.popBackStack() },
                            onSelect = { theme ->
                                ThemeState.replace(theme)
                                ThemeState.icons = emptyMap()
                                ThemeState.shapes3D = emptySet()
                                nav.navigate("editor")
                            }
                        )
                    }
                    composable("editor") {
                        EditorScreen(
                            onBack = {
                                ThemeStorage.save(this@MainActivity, ThemeState.theme)
                                nav.popBackStack()
                            }
                        )
                    }
                }
            }
        }
    }
}
