package com.futo.themecreator.data

import android.graphics.BitmapFactory
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.graphics.asImageBitmap

/**
 * Cache للأشكال المُولَّدة — لتظهر فوراً في المعاينة
 */
object ShapeCache {
    private val cache = mutableMapOf<String, ImageBitmap>()

    fun get(group: KeyGroup, config: ShapeConfig, fillColor: Int): ImageBitmap? {
        val key = "${group.name}_${config.shapeId}_${config.rotation}_${config.sharpness}_${config.tilt}_${fillColor}"
        cache[key]?.let { return it }
        return try {
            val bytes = ShapeGenerator2.generate(config, fillColor, 0)
            val bmp = BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            val img = bmp.asImageBitmap()
            cache[key] = img
            img
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    fun clear() { cache.clear() }
}
