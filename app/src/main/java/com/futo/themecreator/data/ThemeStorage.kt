package com.futo.themecreator.data

import android.content.Context
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.File

object ThemeStorage {
    private const val DIR_NAME = "themes"
    private val _themes = MutableStateFlow<List<ThemeData>>(emptyList())
    val themes: StateFlow<List<ThemeData>> = _themes

    private fun dir(context: Context): File {
        val d = File(context.filesDir, DIR_NAME); d.mkdirs(); return d
    }

    fun load(context: Context) {
        val files = dir(context).listFiles { f -> f.extension == "json" } ?: emptyArray()
        _themes.value = files.mapNotNull {
            try { ThemeSerializer.load(it.readText()) } catch (e: Exception) { null }
        }.sortedByDescending { it.id }
    }

    fun save(context: Context, theme: ThemeData) {
        File(dir(context), "${theme.id}.json").writeText(ThemeSerializer.save(theme))
        load(context)
    }

    fun delete(context: Context, themeId: String) {
        File(dir(context), "$themeId.json").delete(); load(context)
    }

    fun createNew(context: Context): ThemeData {
        val newTheme = ThemeData(
            name = "ثيمي ${_themes.value.size + 1}",
            id = "com.futo.creator.theme_${System.currentTimeMillis()}",
        )
        save(context, newTheme); return newTheme
    }
}
