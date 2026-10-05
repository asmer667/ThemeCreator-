package com.futo.themecreator.data

import android.graphics.*
import java.io.ByteArrayOutputStream

/**
 * يولّد أشكال هندسية 3D (توحي بالضغط)
 */
object ShapesGenerator {

    const val SIZE = 256

    val shapes = listOf(
        ShapeDef("square", "مربع", "▢"),
        ShapeDef("rounded", "دائري", "◯"),
        ShapeDef("circle", "دائرة", "⬤"),
        ShapeDef("hexagon", "سداسي", "⬡"),
        ShapeDef("diamond", "معيّن", "◆"),
        ShapeDef("star", "نجمة", "★"),
        ShapeDef("triangle", "مثلث", "▲"),
        ShapeDef("pentagon", "خماسي", "⬟"),
        ShapeDef("octagon", "ثماني", "⯃"),
        ShapeDef("shield", "درع", "🛡"),
        ShapeDef("capsule", "كبسولة", "▬"),
        ShapeDef("3d_bubble", "فقاعة 3D", "◉"),
        ShapeDef("3d_glass", "زجاج 3D", "◈"),
        ShapeDef("3d_neon", "نيون 3D", "✦"),
        ShapeDef("3d_metal", "معدن 3D", "⬢"),
        ShapeDef("3d_soft", "ناعم 3D", "◍"),
    )

    /**
     * يولّد PNG 3D للشكل المطلوب
     */
    fun generate3DPNG(
        shapeId: String,
        fillColor: Int,
        highlightAlpha: Int = 120,
        shadowAlpha: Int = 90,
    ): ByteArray {
        val bmp = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)

        val path = buildPath(shapeId)
        val bounds = RectF()
        path.computeBounds(bounds, true)

        // 1. الظل الخارجي (drop shadow)
        val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.argb(shadowAlpha, 0, 0, 0)
            setShadowLayer(12f, 0f, 6f, Color.argb(shadowAlpha, 0, 0, 0))
        }
        canvas.drawPath(path, shadowPaint)

        // 2. التدرج الأساسي (Linear 3D)
        val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, SIZE.toFloat(),
                intArrayOf(
                    lighten(fillColor, 0.35f),  // أعلى فاتح
                    fillColor,                    // وسط
                    darken(fillColor, 0.25f),    // أسفل داكن
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawPath(path, gradientPaint)

        // 3. اللمعة العلوية (highlight)
        val highlightPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, bounds.height() * 0.5f,
                intArrayOf(
                    Color.argb(highlightAlpha, 255, 255, 255),
                    Color.TRANSPARENT,
                ),
                null,
                Shader.TileMode.CLAMP
            )
        }
        canvas.save()
        canvas.clipPath(path)
        canvas.drawRect(0f, 0f, SIZE.toFloat(), bounds.height() * 0.5f, highlightPaint)
        canvas.restore()

        // 4. الحدود (Outline)
        val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = darken(fillColor, 0.45f)
        }
        canvas.drawPath(path, outlinePaint)

        // 5. حد داخلي فاتح (inner highlight)
        val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = Color.argb(120, 255, 255, 255)
        }
        canvas.save()
        val insetPath = Path(path)
        val matrix = Matrix()
        matrix.setScale(0.92f, 0.92f, SIZE / 2f, SIZE / 2f)
        insetPath.transform(matrix)
        canvas.drawPath(insetPath, innerPaint)
        canvas.restore()

        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        bmp.recycle()
        return out.toByteArray()
    }

    private fun buildPath(id: String): Path {
        val p = Path()
        val cx = SIZE / 2f
        val cy = SIZE / 2f
        val r = SIZE / 2f - 16f

        when (id) {
            "square" -> p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), 8f, 8f, Path.Direction.CW)
            "rounded" -> p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), r * 0.5f, r * 0.5f, Path.Direction.CW)
            "circle", "3d_bubble" -> p.addCircle(cx, cy, r, Path.Direction.CW)
            "capsule" -> p.addRoundRect(RectF(16f, cy - r * 0.5f, SIZE - 16f, cy + r * 0.5f), r * 0.5f, r * 0.5f, Path.Direction.CW)
            "hexagon", "3d_metal" -> regularPolygon(p, cx, cy, r, 6)
            "pentagon" -> regularPolygon(p, cx, cy, r, 5)
            "octagon" -> regularPolygon(p, cx, cy, r, 8)
            "triangle" -> {
                p.moveTo(cx, 16f); p.lineTo(SIZE - 16f, SIZE - 16f)
                p.lineTo(16f, SIZE - 16f); p.close()
            }
            "diamond", "3d_glass" -> {
                p.moveTo(cx, 16f); p.lineTo(SIZE - 16f, cy)
                p.lineTo(cx, SIZE - 16f); p.lineTo(16f, cy); p.close()
            }
            "star", "3d_neon" -> star(p, cx, cy, r, r * 0.45f, 5)
            "shield" -> {
                p.moveTo(cx, 16f)
                p.lineTo(SIZE - 24f, 60f)
                p.lineTo(SIZE - 24f, cy + 20f)
                p.quadTo(SIZE - 24f, SIZE - 16f, cx, SIZE - 16f)
                p.quadTo(24f, SIZE - 16f, 24f, cy + 20f)
                p.lineTo(24f, 60f)
                p.close()
            }
            else -> p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), 40f, 40f, Path.Direction.CW)
        }
        return p
    }

    private fun regularPolygon(p: Path, cx: Float, cy: Float, r: Float, sides: Int) {
        for (i in 0 until sides) {
            val a = Math.toRadians(-90.0 + 360.0 * i / sides)
            val x = cx + r * Math.cos(a).toFloat()
            val y = cy + r * Math.sin(a).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun star(p: Path, cx: Float, cy: Float, ro: Float, ri: Float, pts: Int) {
        for (i in 0 until pts * 2) {
            val r = if (i % 2 == 0) ro else ri
            val a = Math.toRadians(-90.0 + 180.0 * i / pts)
            val x = cx + r * Math.cos(a).toFloat()
            val y = cy + r * Math.sin(a).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun lighten(color: Int, factor: Float): Int {
        val r = (Color.red(color) + (255 - Color.red(color)) * factor).toInt().coerceIn(0, 255)
        val g = (Color.green(color) + (255 - Color.green(color)) * factor).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) + (255 - Color.blue(color)) * factor).toInt().coerceIn(0, 255)
        return Color.argb(Color.alpha(color), r, g, b)
    }

    private fun darken(color: Int, factor: Float): Int {
        val r = (Color.red(color) * (1 - factor)).toInt().coerceIn(0, 255)
        val g = (Color.green(color) * (1 - factor)).toInt().coerceIn(0, 255)
        val b = (Color.blue(color) * (1 - factor)).toInt().coerceIn(0, 255)
        return Color.argb(Color.alpha(color), r, g, b)
    }
}

data class ShapeDef(val id: String, val name: String, val emoji: String)
