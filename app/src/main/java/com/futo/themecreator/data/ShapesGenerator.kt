package com.futo.themecreator.data

import android.graphics.*
import java.io.ByteArrayOutputStream

/**
 * مولّد الأشكال — 686 شكلًا هندسيًا + 3D
 * العائلات: polygon, star, rounded, ellipse, flower, gear, cross,
 *           heart, shield, arrow, blob, capsule, diamond, teardrop,
 *           wave, cloud + نسخ 3D
 */
object ShapesGenerator {

    const val SIZE = 256

    val shapes: List<ShapeDef> = buildShapes()

    private fun buildShapes(): List<ShapeDef> = buildList {

        // ─── 1. مضلعات منتظمة (3..40 ضلعًا) = 38
        for (n in 3..40) add(ShapeDef("poly$n", "مضلع $n", polygonEmoji(n), "polygon", n.toFloat()))

        // ─── 2. نجوم (5..30 رأسًا × 5 نسب داخلية) = 130
        for (pts in 5..30) for (ir in listOf(0.25f, 0.35f, 0.45f, 0.55f, 0.70f))
            add(ShapeDef("star${pts}_${(ir * 100).toInt()}", "نجمة $pts", "★", "star", pts.toFloat(), ir))

        // ─── 3. مستطيلات مستديرة (20 درجة) = 20
        for (i in 0..19) {
            val r = i / 19f
            add(ShapeDef("rrect$i", "مستدير $i", "▢", "rounded", r))
        }

        // ─── 4. بيضاويات (12 نسبة) = 12
        for (i in 0..11) {
            val ratio = 0.40f + i * 0.1f
            add(ShapeDef("ellipse$i", "بيضاوي $i", "⬭", "ellipse", ratio))
        }

        // ─── 5. زهور (5..24 بتلة × 4 أعماق) = 80
        for (pt in 5..24) for (d in listOf(0.25f, 0.40f, 0.55f, 0.70f))
            add(ShapeDef("flower${pt}_${(d * 100).toInt()}", "زهرة $pt", "✿", "flower", pt.toFloat(), d))

        // ─── 6. تروس (6..36 سنًا × 3 أعماق) = 93
        for (t in 6..36) for (d in listOf(0.12f, 0.20f, 0.28f))
            add(ShapeDef("gear${t}_${(d * 100).toInt()}", "ترس $t", "⚙", "gear", t.toFloat(), d))

        // ─── 7. صلبان (10 سماكات) = 10
        for (i in 0..9) {
            val th = 0.20f + i * 0.06f
            add(ShapeDef("cross$i", "صليب $i", "✚", "cross", th))
        }

        // ─── 8. قلوب (15 درجة حدّة) = 15
        for (i in 0..14) {
            val p = i / 14f
            add(ShapeDef("heart$i", "قلب $i", "♥", "heart", p))
        }

        // ─── 9. دروع (عرض × حدّة × 6×3) = 18
        for (i in 0..5) for (pi in 0..2) {
            val w = 0.40f + i * 0.12f
            add(ShapeDef("shield${i}_$pi", "درع $i$pi", "🛡", "shield", w, pi / 2f))
        }

        // ─── 10. أسهم (اتجاه × سماكة × 4×10) = 40
        for (dir in 0..3) for (i in 0..9) {
            val th = 0.15f + i * 0.08f
            add(ShapeDef("arrow${dir}_$i", "سهم $dir-$i", "➤", "arrow", dir.toFloat(), th))
        }

        // ─── 11. فقاعات (بذرة × سعة × 5×12) = 60
        for (seed in 0..4) for (i in 0..11) {
            val amp = i / 11f
            add(ShapeDef("blob${seed}_${i}", "فقاعة $seed-$i", "◍", "blob", seed.toFloat(), amp))
        }

        // ─── 12. كبسولات (10 نسب) = 10
        for (i in 0..9) {
            val r = 0.25f + i * 0.08f
            add(ShapeDef("capsule$i", "كبسولة $i", "▬", "capsule", r))
        }

        // ─── 13. معيّنات (12 درجة استدارة) = 12
        for (i in 0..11) {
            val r = i / 11f
            add(ShapeDef("diamond$i", "معيّن $i", "◆", "diamond", r))
        }

        // ─── 14. قطرات (15 حدّة) = 15
        for (i in 0..14) {
            val p = i / 14f
            add(ShapeDef("teardrop$i", "قطرة $i", "💧", "teardrop", p))
        }

        // ─── 15. أمواج (15 سعة) = 15
        for (i in 0..14) {
            val a = i / 14f
            add(ShapeDef("wave$i", "موجة $i", "〜", "wave", a))
        }

        // ─── 16. سحابات (9 نتوءات × 2 نمط) = 18
        for (b in 4..12) for (v in 0..1)
            add(ShapeDef("cloud${b}_$v", "سحابة $b-$v", "☁", "cloud", b.toFloat(), v.toFloat()))

        // ─── 17. نسخ 3D لأول 100 شكل = 100
        val first100 = take(100).toList()
        for (base in first100)
            add(base.copy(id = "3d_${base.id}", name = "3D ${base.name}"))
    }

    private fun polygonEmoji(n: Int): String = when (n) {
        3 -> "△"; 4 -> "▢"; 5 -> "⬟"; 6 -> "⬡"; 7 -> "◈"; 8 -> "⯃"; 9 -> "⬢"; 10 -> "⬢"
        else -> "●"
    }

    fun generate3DPNG(
        shapeId: String,
        fillColor: Int,
        rotation: Float = 0f,
        sharpness: Float = 0.5f,
        tilt: Float = 0f,
        highlightAlpha: Int = 120,
        shadowAlpha: Int = 90,
    ): ByteArray {
        val def = shapes.find { it.id == shapeId } ?: shapes.first()
        val is3D = def.id.startsWith("3d_")

        val bmp = Bitmap.createBitmap(SIZE, SIZE, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)
        canvas.drawColor(Color.TRANSPARENT)

        val path = buildPath(def, sharpness)
        val bounds = RectF()
        path.computeBounds(bounds, true)

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

        // 1. ظل
        if (is3D) {
            val shadowPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                color = Color.argb(shadowAlpha, 0, 0, 0)
                setShadowLayer(14f, 0f, 8f, Color.argb(shadowAlpha, 0, 0, 0))
            }
            canvas.drawPath(path, shadowPaint)
        }

        // 2. تعبئة متدرجة
        val gradientPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, 0f, SIZE.toFloat(),
                intArrayOf(lighten(fillColor, 0.35f), fillColor, darken(fillColor, 0.25f)),
                floatArrayOf(0f, 0.5f, 1f),
                Shader.TileMode.CLAMP
            )
        }
        canvas.drawPath(path, gradientPaint)

        // 3. لمعة علوية
        if (is3D) {
            val hl = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                shader = LinearGradient(
                    0f, 0f, 0f, bounds.height() * 0.5f,
                    intArrayOf(Color.argb(highlightAlpha, 255, 255, 255), Color.TRANSPARENT),
                    null, Shader.TileMode.CLAMP
                )
            }
            canvas.save()
            canvas.clipPath(path)
            canvas.drawRect(0f, 0f, SIZE.toFloat(), bounds.height() * 0.5f, hl)
            canvas.restore()
        }

        // 4. حدود
        val outlinePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = if (is3D) 5f else 3f
            color = darken(fillColor, 0.45f)
        }
        canvas.drawPath(path, outlinePaint)

        // 5. حد داخلي
        if (is3D) {
            val innerPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
                style = Paint.Style.STROKE
                strokeWidth = 2f
                color = Color.argb(120, 255, 255, 255)
            }
            canvas.save()
            val inset = Path(path)
            val m = Matrix()
            m.setScale(0.92f, 0.92f, SIZE / 2f, SIZE / 2f)
            inset.transform(m)
            canvas.drawPath(inset, innerPaint)
            canvas.restore()
        }

        canvas.restore()

        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        bmp.recycle()
        return out.toByteArray()
    }

    /**
     * يولّد صورة PNG لتدرّج خطّي بين لونين.
     * تُستخدم لخلفية الكيبورد (FUTO تقرأ الصور لا النصوص).
     */
    fun generateGradientPNG(
        color1Hex: String,
        color2Hex: String,
        angleDeg: Float = 45f,
        size: Int = 512,
    ): ByteArray {
        val c1 = android.graphics.Color.parseColor(color1Hex)
        val c2 = android.graphics.Color.parseColor(color2Hex)
        val bmp = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
        val canvas = Canvas(bmp)

        val rad = Math.toRadians(angleDeg.toDouble())
        val dx = Math.cos(rad).toFloat() * size
        val dy = Math.sin(rad).toFloat() * size

        val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            shader = LinearGradient(
                0f, 0f, dx, dy,
                intArrayOf(c1, c2),
                floatArrayOf(0f, 1f),
                Shader.TileMode.CLAMP,
            )
        }
        canvas.drawRect(0f, 0f, size.toFloat(), size.toFloat(), paint)

        val out = ByteArrayOutputStream()
        bmp.compress(Bitmap.CompressFormat.PNG, 100, out)
        bmp.recycle()
        return out.toByteArray()
    }

    private fun buildPath(def: ShapeDef, sharpness: Float): Path {
        val p = Path()
        val cx = SIZE / 2f
        val cy = SIZE / 2f
        val r = SIZE / 2f - 16f
        val s = sharpness.coerceIn(0f, 1f)

        when (def.family) {
            "polygon"  -> regularPolygon(p, cx, cy, r, def.p1.toInt())
            "star"     -> star(p, cx, cy, r, r * def.p2, def.p1.toInt())
            "rounded"  -> {
                val cr = r * (0.02f + def.p1 * 0.98f)
                p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), cr, cr, Path.Direction.CW)
            }
            "ellipse"  -> {
                val rx = r * def.p1
                val ry = r / def.p1
                p.addOval(RectF(cx - rx, cy - ry, cx + rx, cy + ry), Path.Direction.CW)
            }
            "flower"   -> flower(p, cx, cy, r, def.p1.toInt(), def.p2)
            "gear"     -> gear(p, cx, cy, r, def.p1.toInt(), def.p2)
            "cross"    -> cross(p, cx, cy, r, def.p1)
            "heart"    -> heart(p, cx, cy, r, def.p1)
            "shield"   -> shield(p, cx, cy, r, def.p1, def.p2)
            "arrow"    -> arrow(p, cx, cy, r, def.p1.toInt(), def.p2)
            "blob"     -> blob(p, cx, cy, r, def.p1.toInt(), def.p2)
            "capsule"  -> {
                val h = r * def.p1
                p.addRoundRect(RectF(16f, cy - h, SIZE - 16f, cy + h), h, h, Path.Direction.CW)
            }
            "diamond"  -> diamond(p, cx, cy, r, def.p1)
            "teardrop" -> teardrop(p, cx, cy, r, def.p1)
            "wave"     -> wave(p, cx, cy, r, def.p1)
            "cloud"    -> cloud(p, cx, cy, r, def.p1.toInt(), def.p2 > 0.5f)
            else -> p.addRoundRect(RectF(16f, 16f, SIZE - 16f, SIZE - 16f), 40f, 40f, Path.Direction.CW)
        }
        return p
    }

    // ═══════════ العائلات ═══════════

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

    private fun flower(p: Path, cx: Float, cy: Float, r: Float, petals: Int, depth: Float) {
        val steps = petals * 20
        for (i in 0 until steps) {
            val angle = 2.0 * Math.PI * i / steps
            val petalPhase = Math.cos(petals * angle)
            val radius = r * (1f - depth + depth * petalPhase.toFloat())
            val x = cx + radius * Math.cos(angle).toFloat()
            val y = cy + radius * Math.sin(angle).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun gear(p: Path, cx: Float, cy: Float, r: Float, teeth: Int, depth: Float) {
        val steps = teeth * 4
        for (i in 0 until steps) {
            val angle = 2.0 * Math.PI * i / steps
            val phase = i % 4
            val radius = when (phase) {
                0, 1 -> r
                2, 3 -> r * (1f - depth)
                else -> r
            }
            val x = cx + radius * Math.cos(angle).toFloat()
            val y = cy + radius * Math.sin(angle).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun cross(p: Path, cx: Float, cy: Float, r: Float, thickness: Float) {
        val arm = r * 0.5f * thickness * 2f
        val w = r * thickness
        p.moveTo(cx - w, cy - r)
        p.lineTo(cx + w, cy - r)
        p.lineTo(cx + w, cy - w)
        p.lineTo(cx + r, cy - w)
        p.lineTo(cx + r, cy + w)
        p.lineTo(cx + w, cy + w)
        p.lineTo(cx + w, cy + r)
        p.lineTo(cx - w, cy + r)
        p.lineTo(cx - w, cy + w)
        p.lineTo(cx - r, cy + w)
        p.lineTo(cx - r, cy - w)
        p.lineTo(cx - w, cy - w)
        p.close()
    }

    private fun heart(p: Path, cx: Float, cy: Float, r: Float, sharpness: Float) {
        val topY = cy - r * 0.6f
        val dipY = cy - r * (0.3f - sharpness * 0.3f)
        val bottomY = cy + r

        p.moveTo(cx, dipY)
        p.cubicTo(
            cx - r * 1.2f, cy - r * 1.4f,
            cx - r * 1.2f, cy + r * 0.2f,
            cx, bottomY
        )
        p.cubicTo(
            cx + r * 1.2f, cy + r * 0.2f,
            cx + r * 1.2f, cy - r * 1.4f,
            cx, dipY
        )
        p.close()
    }

    private fun shield(p: Path, cx: Float, cy: Float, r: Float, width: Float, pointiness: Float) {
        val w = r * width
        val top = cy - r
        val bot = cy + r
        p.moveTo(cx - w, top)
        p.lineTo(cx + w, top)
        p.lineTo(cx + w, cy + r * (0.2f + pointiness * 0.3f))
        p.quadTo(cx + w, bot - (1f - pointiness) * r * 0.2f, cx, bot)
        p.quadTo(cx - w, bot - (1f - pointiness) * r * 0.2f, cx - w, cy + r * (0.2f + pointiness * 0.3f))
        p.close()
    }

    private fun arrow(p: Path, cx: Float, cy: Float, r: Float, dir: Int, thickness: Float) {
        val w = r * thickness
        when (dir) {
            0 -> { // →
                p.moveTo(cx - r, cy - w); p.lineTo(cx + r * 0.2f, cy - w)
                p.lineTo(cx + r * 0.2f, cy - w * 2f); p.lineTo(cx + r, cy)
                p.lineTo(cx + r * 0.2f, cy + w * 2f); p.lineTo(cx + r * 0.2f, cy + w)
                p.lineTo(cx - r, cy + w); p.close()
            }
            1 -> { // ←
                p.moveTo(cx + r, cy - w); p.lineTo(cx - r * 0.2f, cy - w)
                p.lineTo(cx - r * 0.2f, cy - w * 2f); p.lineTo(cx - r, cy)
                p.lineTo(cx - r * 0.2f, cy + w * 2f); p.lineTo(cx - r * 0.2f, cy + w)
                p.lineTo(cx + r, cy + w); p.close()
            }
            2 -> { // ↑
                p.moveTo(cx - w, cy + r); p.lineTo(cx - w, cy - r * 0.2f)
                p.lineTo(cx - w * 2f, cy - r * 0.2f); p.lineTo(cx, cy - r)
                p.lineTo(cx + w * 2f, cy - r * 0.2f); p.lineTo(cx + w, cy - r * 0.2f)
                p.lineTo(cx + w, cy + r); p.close()
            }
            3 -> { // ↓
                p.moveTo(cx - w, cy - r); p.lineTo(cx - w, cy + r * 0.2f)
                p.lineTo(cx - w * 2f, cy + r * 0.2f); p.lineTo(cx, cy + r)
                p.lineTo(cx + w * 2f, cy + r * 0.2f); p.lineTo(cx + w, cy + r * 0.2f)
                p.lineTo(cx + w, cy - r); p.close()
            }
        }
    }

    private fun blob(p: Path, cx: Float, cy: Float, r: Float, seed: Int, amp: Float) {
        val steps = 64
        val phase1 = seed * 1.3f
        val phase2 = seed * 2.1f
        for (i in 0 until steps) {
            val a = 2.0 * Math.PI * i / steps
            val noise = (Math.sin(3 * a + phase1).toFloat() * 0.5f +
                         Math.sin(5 * a + phase2).toFloat() * 0.3f +
                         Math.sin(7 * a).toFloat() * 0.2f)
            val radius = r * (1f + amp * noise)
            val x = cx + radius * Math.cos(a).toFloat()
            val y = cy + radius * Math.sin(a).toFloat()
            if (i == 0) p.moveTo(x, y) else p.lineTo(x, y)
        }
        p.close()
    }

    private fun diamond(p: Path, cx: Float, cy: Float, r: Float, roundness: Float) {
        val cr = r * roundness * 0.4f
        p.moveTo(cx, cy - r)
        p.lineTo(cx + r - cr, cy - cr); p.quadTo(cx + r, cy, cx + r, cy + cr)
        p.lineTo(cx + cr, cy + r - cr); p.quadTo(cx, cy + r, cx - cr, cy + r - cr)
        p.lineTo(cx - r + cr, cy + cr); p.quadTo(cx - r, cy, cx - r, cy - cr)
        p.lineTo(cx - cr, cy - r + cr); p.quadTo(cx, cy - r, cx, cy - r)
        p.close()
    }

    private fun teardrop(p: Path, cx: Float, cy: Float, r: Float, pointiness: Float) {
        val topY = cy - r - r * pointiness * 0.8f
        val botY = cy + r
        p.moveTo(cx, topY)
        p.cubicTo(cx + r * 0.9f, cy - r * 0.5f, cx + r, cy + r * 0.5f, cx, botY)
        p.cubicTo(cx - r, cy + r * 0.5f, cx - r * 0.9f, cy - r * 0.5f, cx, topY)
        p.close()
    }

    private fun wave(p: Path, cx: Float, cy: Float, r: Float, amp: Float) {
        val left = 16f
        val right = SIZE - 16f
        val top = cy - r * 0.6f
        val bot = cy + r * 0.8f
        p.moveTo(left, top)
        val steps = 40
        for (i in 0..steps) {
            val x = left + (right - left) * i / steps
            val y = top + amp * r * 0.4f * Math.sin(4.0 * Math.PI * i / steps).toFloat()
            p.lineTo(x, y)
        }
        p.lineTo(right, bot); p.lineTo(left, bot); p.close()
    }

    private fun cloud(p: Path, cx: Float, cy: Float, r: Float, bumps: Int, variant: Boolean) {
        val baseline = cy + r * 0.4f
        val step = (2 * r) / bumps
        if (variant) {
            p.moveTo(cx - r, baseline)
            for (i in 0 until bumps) {
                val x = cx - r + step * (i + 0.5f)
                val rad = step * (0.6f + (i % 3) * 0.15f)
                p.addCircle(x, baseline - rad * 0.5f, rad, Path.Direction.CW)
            }
            p.addRect(cx - r, baseline - step * 0.3f, cx + r, baseline, Path.Direction.CW)
        } else {
            p.moveTo(cx - r, baseline)
            for (i in 0 until bumps) {
                val x = cx - r + step * i
                val rad = step * (0.7f + (i % 2) * 0.2f)
                p.addCircle(x, baseline - rad * 0.4f, rad, Path.Direction.CW)
            }
        }
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

data class ShapeDef(
    val id: String,
    val name: String,
    val emoji: String,
    val family: String,
    val p1: Float = 0f,
    val p2: Float = 0f,
)


/**
 * ذاكرة تخزين مؤقت للصور المصغّرة (Thumbnails) — LRU بسيطة.
 * تحفظ آخر 120 صورة فقط لتقليل استخدام الذاكرة.
 */
object ShapeThumbnailCache {
    private const val MAX_SIZE = 120
    private const val THUMB_SIZE = 96

    private val cache = object : LinkedHashMap<String, android.graphics.Bitmap>(16, 0.75f, true) {
        override fun removeEldestEntry(eldest: Map.Entry<String, android.graphics.Bitmap>): Boolean {
            return size > MAX_SIZE
        }
    }

    @Synchronized
    fun get(
        shapeId: String,
        fillColor: Int,
        rotation: Float,
        sharpness: Float,
        tilt: Float,
    ): android.graphics.Bitmap {
        val key = "$shapeId|$fillColor|$rotation|$sharpness|$tilt"
        cache[key]?.let { return it }

        val bytes = ShapesGenerator.generate3DPNG(
            shapeId = shapeId,
            fillColor = fillColor,
            rotation = rotation,
            sharpness = sharpness,
            tilt = tilt,
            highlightAlpha = 100,
            shadowAlpha = 60,
        )

        val bmp = android.graphics.BitmapFactory.decodeByteArray(bytes, 0, bytes.size)
            ?: return android.graphics.Bitmap.createBitmap(
                THUMB_SIZE, THUMB_SIZE, android.graphics.Bitmap.Config.ARGB_8888
            )

        // تصغير إلى 96×96
        val scaled = android.graphics.Bitmap.createScaledBitmap(bmp, THUMB_SIZE, THUMB_SIZE, true)
        if (scaled != bmp) bmp.recycle()

        cache[key] = scaled
        return scaled
    }

    @Synchronized
    fun clear() = cache.clear()
}
