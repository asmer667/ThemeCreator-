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
     * يولّد PNG 3D للشكل المطلوب مع تطبيق الدوران والحدة والميل.
     *
     * @param shapeId معرّف الشكل
     * @param fillColor لون التعبئة (ARGB)
     * @param rotation زاوية الدوران بالدرجات (0..360)
     * @param sharpness حِدّة الزوايا: 0 = حاد جدًا، 1 = دائري جدًا
     * @param tilt الميل بالدرجات (-45..45)
     */
    fun generate3DPNG(
        shapeId: String,
        fillColor: Int,
        rotation: Float = 0f,
        sharpness: Float = 0.5f,
        tilt: Float = 0f,
        highlightAlpha: Int = 120,
        shadowAlpha: Int = 90,
    ): ByteArray {
        val bmp = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)

        val path = buildPath(shapeId, sharpness)
        val bounds = RectF()
        path.computeBounds(bounds, true)

        // ─── تطبيق التحويلات (دوران + ميل) ───
        canvas.save()
        if (rotation != 0f || tilt != 0f) {
            val m = Matrix()
            m.postTranslate(-SIZE / 2f, -SIZE / 2f)
            if (tilt != 0f) {
                val skewRad = Math.toRadians(tilt.toDouble().coerceIn(-45.0, 45.0))
                m.postSkew(Math.tan(skewRad).toFloat(), 0f)
            }
            if (rotation != 0f) m.postRotate(rotation)
            m.postTranslate(SIZE / 2f, SIZE / 2f)
            canvas.concat(m)
        }

        // 1. الظل الخارجي
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
                    lighten(fillColor, 0.35f),
                    fillColor,
                    darken(fillColor, 0.25f),
                ),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawPath(path, gradientPaint)

        // 3. اللمعة العلوية
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

        // 4. الحدود
        val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = darken(fillColor, 0.45f)
        }
        canvas.drawPath(path, outlinePaint)

        // 5. حد داخلي فاتح
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

        canvas.restore() // استرجاع تحويلات الدوران

        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        bmp.recycle()
        return out.toByteArray()
    }

    private fun buildPath(id: String, sharpness: Float): Path {
        val p = Path()
        val cx = SIZE / 2f
        val cy = SIZE / 2f
        val r = SIZE / 2f - 16f
        val s = sharpness.coerceIn(0f, 1f)

        when (id) {
            "square" -> {
                val cr = (4f + s * (r * 0.4f)).coerceAtLeast(2f)
                p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), cr, cr, Path.Direction.CW)
            }
            "rounded" -> {
                val cr = r * (0.2f + s * 0.7f)
                p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), cr, cr, Path.Direction.CW)
            }
            "circle", "3d_bubble" -> p.addCircle(cx, cy, r, Path.Direction.CW)
            "capsule" -> p.addRoundRect(
                RectF(16f, cy - r * 0.5f, SIZE - 16f, cy + r * 0.5f),
                r * 0.5f, r * 0.5f, Path.Direction.CW
            )
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
            else -> {
                val cr = (10f + s * (r * 0.6f)).coerceAtLeast(4f)
                p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), cr, cr, Path.Direction.CW)
            }
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
