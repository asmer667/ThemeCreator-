package com.futo.themecreator.data

import kotlinx.serialization.json.Json

/**
 * يحفظ ويحمّل مشروع الثيم كـ JSON
 */
object ThemeSerializer {

    private val json = Json {
        prettyPrint = true
        ignoreUnknownKeys = true
        encodeDefaults = true
    }

    fun save(theme: ThemeData): String = json.encodeToString(ThemeData.serializer(), theme)

    fun load(content: String): ThemeData = json.decodeFromString(ThemeData.serializer(), content)
}
