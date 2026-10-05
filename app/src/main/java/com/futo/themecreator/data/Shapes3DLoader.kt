package com.futo.themecreator.data

import android.content.Context
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class Shape3DEntry(
    val file: String,
    val name: String,
    val category: String,
)

object Shapes3DLoader {
    private val json = Json { ignoreUnknownKeys = true }
    private var cache: List<Shape3DEntry>? = null

    fun load(context: Context): List<Shape3DEntry> {
        cache?.let { return it }
        return try {
            val text = context.assets.open("shapes_3d/index.json")
                .bufferedReader().use { it.readText() }
            val list = json.decodeFromString<List<Shape3DEntry>>(text)
            cache = list
            list
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
    }

    fun categories(context: Context): List<String> =
        load(context).map { it.category }.distinct().sorted()

    fun byCategory(context: Context, category: String): List<Shape3DEntry> =
        load(context).filter { it.category == category }

    fun readBytes(context: Context, fileName: String): ByteArray? = try {
        context.assets.open("shapes_3d/$fileName").readBytes()
    } catch (e: Exception) { null }
}
