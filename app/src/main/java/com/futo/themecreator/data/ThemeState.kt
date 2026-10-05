package com.futo.themecreator.data

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * حالة الثيم المشتركة بين كل الشاشات
 */
object ThemeState {

    var theme by mutableStateOf(ThemeData())
        private set

    var icons by mutableStateOf<Map<String, Uri>>(emptyMap())
    var shapes3D by mutableStateOf<Set<String>>(emptySet())
    var keyGroupShapes by mutableStateOf<Map<KeyGroup, ShapeConfig>>(emptyMap())
    var fontsVersion by mutableStateOf(0)  // يُزاد عند تحميل خط جديد لإعادة الرسم

    // ─── الأشكال المختارة لكل مجموعة ───
    var selectedShapeKey by mutableStateOf(KeyGroup.LETTERS)
    var selectedShapes by mutableStateOf<Map<KeyGroup, String>>(emptyMap())

    fun isShapeSelected(shapeId: String): Boolean =
        selectedShapes[selectedShapeKey] == shapeId

    fun clearShapeSelection() {
        selectedShapes = selectedShapes - selectedShapeKey
        ShapeThumbnailCache.clear()
    }

    fun getShapeConfig(group: KeyGroup): ShapeConfig? =
        keyGroupShapes[group]

    fun setShapeConfig(group: KeyGroup, config: ShapeConfig) {
        keyGroupShapes = keyGroupShapes + (group to config)
    }

    fun clearShapeConfig(group: KeyGroup) {
        keyGroupShapes = keyGroupShapes - group
    }
    var backgroundImageUri by mutableStateOf<android.net.Uri?>(null)
    var backgroundImageOpacity by mutableStateOf(0.5f)

    /** تحديث حقل واحد في الثيم */
    fun update(field: String, value: String) {
        val t = theme
        theme = when (field) {
            "name" -> t.copy(name = value)
            "author" -> t.copy(author = value)
            "id" -> t.copy(id = value)
            "description" -> t.copy(description = value)
            "primary" -> t.copy(primary = value)
            "on_primary" -> t.copy(onPrimary = value)
            "primary_container" -> t.copy(primaryContainer = value)
            "on_primary_container" -> t.copy(onPrimaryContainer = value)
            "inverse_primary" -> t.copy(inversePrimary = value)
            "secondary" -> t.copy(secondary = value)
            "on_secondary" -> t.copy(onSecondary = value)
            "secondary_container" -> t.copy(secondaryContainer = value)
            "on_secondary_container" -> t.copy(onSecondaryContainer = value)
            "tertiary" -> t.copy(tertiary = value)
            "on_tertiary" -> t.copy(onTertiary = value)
            "tertiary_container" -> t.copy(tertiaryContainer = value)
            "on_tertiary_container" -> t.copy(onTertiaryContainer = value)
            "background" -> t.copy(background = value)
            "on_background" -> t.copy(onBackground = value)
            "surface" -> t.copy(surface = value)
            "on_surface" -> t.copy(onSurface = value)
            "surface_variant" -> t.copy(surfaceVariant = value)
            "on_surface_variant" -> t.copy(onSurfaceVariant = value)
            "surface_tint" -> t.copy(surfaceTint = value)
            "inverse_surface" -> t.copy(inverseSurface = value)
            "inverse_on_surface" -> t.copy(inverseOnSurface = value)
            "error" -> t.copy(error = value)
            "on_error" -> t.copy(onError = value)
            "error_container" -> t.copy(errorContainer = value)
            "on_error_container" -> t.copy(onErrorContainer = value)
            "outline" -> t.copy(outline = value)
            "outline_variant" -> t.copy(outlineVariant = value)
            "scrim" -> t.copy(scrim = value)
            "surface_bright" -> t.copy(surfaceBright = value)
            "surface_dim" -> t.copy(surfaceDim = value)
            "surface_container" -> t.copy(surfaceContainer = value)
            "surface_container_high" -> t.copy(surfaceContainerHigh = value)
            "surface_container_highest" -> t.copy(surfaceContainerHighest = value)
            "surface_container_low" -> t.copy(surfaceContainerLow = value)
            "surface_container_lowest" -> t.copy(surfaceContainerLowest = value)
            "keyboard_surface" -> t.copy(keyboardSurface = value)
            "keyboard_surface_dim" -> t.copy(keyboardSurfaceDim = value)
            "keyboard_container" -> t.copy(keyboardContainer = value)
            "keyboard_container_variant" -> t.copy(keyboardContainerVariant = value)
            "on_keyboard_container" -> t.copy(onKeyboardContainer = value)
            "keyboard_press" -> t.copy(keyboardPress = value)
            "keyboard_container_pressed" -> t.copy(keyboardContainerPressed = value)
            "on_keyboard_container_pressed" -> t.copy(onKeyboardContainerPressed = value)
            else -> t
        }
    }

    /** تحديث قيمة رقم */
    fun updateNumber(field: String, value: Float) {
        val t = theme
        theme = when (field) {
            "roundedness" -> t.copy(roundedness = value)
            "scale_text" -> t.copy(scaleText = value)
            "scale_hints" -> t.copy(scaleHints = value)
            "weight_text" -> t.copy(weightText = value)
            "weight_hints" -> t.copy(weightHints = value)
            else -> t
        }
    }

    /** تحديث قيمة منطقية */
    fun updateBool(field: String, value: Boolean) {
        val t = theme
        theme = when (field) {
            "auto_borders" -> t.copy(autoBorders = value)
            "center_hints" -> t.copy(centerHints = value)
            else -> t
        }
    }

    /** تحديث أيقونة */
    fun setIcon(name: String, uri: Uri?) {
        val current = icons.toMutableMap()
        if (uri == null) current.remove(name) else current[name] = uri
        icons = current
    }

    /** استبدال كامل للثيم (تحميل preset) */
    fun replace(newTheme: ThemeData) {
        theme = newTheme
    }

    /** إعادة تعيين */
    fun toggleShape3D(fileName: String) {
        shapes3D = if (fileName in shapes3D) shapes3D - fileName else shapes3D + fileName
    }

    fun setBackgroundImage(uri: android.net.Uri?) {
        backgroundImageUri = uri
    }
    fun setBackgroundOpacity(v: Float) {
        backgroundImageOpacity = v
    }

    fun reset() {
        shapes3D = emptySet()
        theme = ThemeData()
        icons = emptyMap()
    }


    fun setGradientBlur(v: Float) {
        theme = theme.copy(gradientBlur = v)
    }
    fun setGradientOpacity(v: Float) {
        theme = theme.copy(gradientOpacity = v)
    }
    fun setGradientAngle(v: Float) {
        theme = theme.copy(gradientAngle = v)
    }
    fun setGradientMidColor(v: String?) {
        theme = theme.copy(gradientMidColor = v)
    }
    fun setGradientMode(v: String) {
        theme = theme.copy(gradientMode = v)
    }
    fun setGradientStops(v: String) {
        theme = theme.copy(gradientStops = v)
    }
    fun setBackgroundBlur(v: Float) {
        theme = theme.copy(backgroundBlur = v)
    }
    fun setBackgroundSaturation(v: Float) {
        theme = theme.copy(backgroundSaturation = v)
    }
    fun setBackgroundBrightness(v: Float) {
        theme = theme.copy(backgroundBrightness = v)
    }
}
