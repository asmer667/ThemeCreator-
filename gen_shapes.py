#!/usr/bin/env python3
# -*- coding: utf-8 -*-
"""
مولّد أشكال 3D احترافية لأزرار الكيبورد
"""
from PIL import Image, ImageDraw, ImageFilter
import os, json, math, colorsys

OUT_DIR = "app/src/main/assets/shapes_3d"
SIZE = 256
os.makedirs(OUT_DIR, exist_ok=True)

def hex_to_rgb(h):
    h = h.lstrip('#')
    return tuple(int(h[i:i+2], 16) for i in (0, 2, 4))

def rgb_to_hex(rgb):
    return '#{:02X}{:02X}{:02X}'.format(*[max(0,min(255,int(c))) for c in rgb])

def lighten(rgb, f):
    return tuple(int(c + (255-c)*f) for c in rgb)

def darken(rgb, f):
    return tuple(int(c * (1-f)) for c in rgb)

def lerp(c1, c2, t):
    return tuple(int(c1[i] + (c2[i]-c1[i])*t) for i in range(3))

def gradient_image(size, c1, c2, vertical=True):
    """صورة تدرج بين لونين"""
    img = Image.new('RGB', (size, size))
    px = img.load()
    for y in range(size):
        for x in range(size):
            t = y/size if vertical else x/size
            px[x,y] = lerp(c1, c2, t)
    return img

def rounded_rect_mask(size, radius):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    d.rounded_rectangle([0,0,size-1,size-1], radius=radius, fill=255)
    return m

def circle_mask(size):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    d.ellipse([0,0,size-1,size-1], fill=255)
    return m

def polygon_mask(size, sides, rotation=0):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    cx, cy = size/2, size/2
    r = size/2 - 4
    pts = []
    for i in range(sides):
        a = math.radians(-90 + rotation + 360*i/sides)
        pts.append((cx + r*math.cos(a), cy + r*math.sin(a)))
    d.polygon(pts, fill=255)
    return m

def star_mask(size, points=5, inner_ratio=0.45):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    cx, cy = size/2, size/2
    ro = size/2 - 4
    ri = ro * inner_ratio
    pts = []
    for i in range(points*2):
        r = ro if i%2==0 else ri
        a = math.radians(-90 + 180*i/points)
        pts.append((cx + r*math.cos(a), cy + r*math.sin(a)))
    d.polygon(pts, fill=255)
    return m

def diamond_mask(size):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    d.polygon([(size/2,4),(size-4,size/2),(size/2,size-4),(4,size/2)], fill=255)
    return m

def triangle_mask(size):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    d.polygon([(size/2,4),(size-4,size-4),(4,size-4)], fill=255)
    return m

def heart_mask(size):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    # معادلة القلب
    pts = []
    for i in range(360):
        t = math.radians(i)
        x = 16 * math.sin(t)**3
        y = 13*math.cos(t) - 5*math.cos(2*t) - 2*math.cos(3*t) - math.cos(4*t)
        pts.append((size/2 + x*size/40, size/2 - y*size/40))
    d.polygon(pts, fill=255)
    return m

def capsule_mask(size):
    m = Image.new('L', (size, size), 0)
    d = ImageDraw.Draw(m)
    r = size//4
    d.rounded_rectangle([4, size//2 - r, size-4, size//2 + r], radius=r, fill=255)
    return m

def make_3d(mask, base_color):
    """إنشاء شكل 3D مع إضاءة من أعلى + ظل + لمعة"""
    img = Image.new('RGBA', (SIZE, SIZE), (0,0,0,0))

    # 1. الظل (خارجي)
    shadow = Image.new('RGBA', (SIZE, SIZE), (0,0,0,0))
    shadow_draw = ImageDraw.Draw(shadow)
    shadow_draw.bitmap((0,0), mask, fill=(0,0,0,180))
    shadow = shadow.filter(ImageFilter.GaussianBlur(8))
    # إزاحة للأسفل
    shifted = Image.new('RGBA', (SIZE, SIZE), (0,0,0,0))
    shifted.paste(shadow, (0, 6), shadow)
    img = Image.alpha_composite(img, shifted)

    # 2. الجسم (تدرج خطي)
    top = lighten(base_color, 0.45)
    mid = base_color
    bottom = darken(base_color, 0.30)
    body = Image.new('RGBA', (SIZE, SIZE), (0,0,0,0))
    for y in range(SIZE):
        if y < SIZE/2:
            t = y/(SIZE/2)
            c = lerp(top, mid, t)
        else:
            t = (y - SIZE/2)/(SIZE/2)
            c = lerp(mid, bottom, t)
        row = Image.new('RGB', (SIZE, 1), c)
        body.paste(row, (0, y))
    body.putalpha(mask)
    img = Image.alpha_composite(img, body)

    # 3. اللمعة العلوية (highlight)
    highlight = Image.new('RGBA', (SIZE, SIZE), (0,0,0,0))
    hd = ImageDraw.Draw(highlight)
    hd.bitmap((0,0), mask, fill=(255,255,255,100))
    # نص القناع (نصف علوي فقط)
    h_mask = Image.new('L', (SIZE, SIZE), 0)
    hd2 = ImageDraw.Draw(h_mask)
    hd2.rectangle([0, 0, SIZE, SIZE//2], fill=255)
    highlight.putalpha(Image.composite(highlight.split()[3], Image.new('L',(SIZE,SIZE),0), h_mask))
    highlight = highlight.filter(ImageFilter.GaussianBlur(4))
    img = Image.alpha_composite(img, highlight)

    # 4. الحدود الخارجية
    border = Image.new('RGBA', (SIZE, SIZE), (0,0,0,0))
    border_draw = ImageDraw.Draw(border)
    border_draw.bitmap((0,0), mask, fill=darken(base_color, 0.55) + (255,))
    # اطرح الداخل
    inner_mask = mask.filter(ImageFilter.MinFilter(5))
    border.putalpha(Image.composite(border.split()[3], Image.new('L',(SIZE,SIZE),0), inner_mask))
    img = Image.alpha_composite(img, border)

    return img


# ═══════════ قائمة الأشكال ═══════════
SHAPES = [
    # (id, display, category, mask_func, color)
    ("square_red",      "مربع أحمر",   "مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (220, 50, 50)),
    ("square_blue",     "مربع أزرق",   "مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (50, 120, 220)),
    ("square_green",    "مربع أخضر",   "مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (50, 180, 100)),
    ("square_yellow",   "مربع أصفر",   "مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (230, 190, 50)),
    ("square_purple",   "مربع بنفسجي", "مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (140, 70, 200)),
    ("square_orange",   "مربع برتقالي","مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (240, 130, 50)),
    ("square_gray",     "مربع رمادي",  "مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (120, 120, 130)),
    ("square_pink",     "مربع وردي",   "مربعات",   lambda: rounded_rect_mask(SIZE, 30),   (230, 100, 160)),

    ("circle_red",      "دائرة حمراء", "دوائر",   lambda: circle_mask(SIZE),                          (220, 50, 50)),
    ("circle_blue",     "دائرة زرقاء", "دوائر",   lambda: circle_mask(SIZE),                          (50, 120, 220)),
    ("circle_green",    "دائرة خضراء", "دوائر",   lambda: circle_mask(SIZE),                          (50, 180, 100)),
    ("circle_yellow",   "دائرة صفراء", "دوائر",   lambda: circle_mask(SIZE),                          (230, 190, 50)),
    ("circle_purple",   "دائرة بنفسجية","دوائر",  lambda: circle_mask(SIZE),                          (140, 70, 200)),
    ("circle_orange",   "دائرة برتقالية","دوائر", lambda: circle_mask(SIZE),                          (240, 130, 50)),
    ("circle_cyan",     "دائرة سماوية", "دوائر",  lambda: circle_mask(SIZE),                          (50, 190, 220)),
    ("circle_pink",     "دائرة وردية", "دوائر",   lambda: circle_mask(SIZE),                          (230, 100, 160)),

    ("triangle_red",    "مثلث أحمر",   "مثلثات",  lambda: triangle_mask(SIZE),                        (220, 50, 50)),
    ("triangle_blue",   "مثلث أزرق",   "مثلثات",  lambda: triangle_mask(SIZE),                        (50, 120, 220)),
    ("triangle_green",  "مثلث أخضر",   "مثلثات",  lambda: triangle_mask(SIZE),                        (50, 180, 100)),
    ("triangle_yellow", "مثلث أصفر",   "مثلثات",  lambda: triangle_mask(SIZE),                        (230, 190, 50)),
    ("triangle_purple", "مثلث بنفسجي", "مثلثات",  lambda: triangle_mask(SIZE),                        (140, 70, 200)),
    ("triangle_orange", "مثلث برتقالي", "مثلثات", lambda: triangle_mask(SIZE),                        (240, 130, 50)),

    ("diamond_red",     "معيّن أحمر",  "معيّنات", lambda: diamond_mask(SIZE),                         (220, 50, 50)),
    ("diamond_blue",    "معيّن أزرق",  "معيّنات", lambda: diamond_mask(SIZE),                         (50, 120, 220)),
    ("diamond_green",   "معيّن أخضر",  "معيّنات", lambda: diamond_mask(SIZE),                         (50, 180, 100)),
    ("diamond_purple",  "معيّن بنفسجي","معيّنات", lambda: diamond_mask(SIZE),                         (140, 70, 200)),
    ("diamond_cyan",    "معيّن سماوي", "معيّنات", lambda: diamond_mask(SIZE),                         (50, 190, 220)),

    ("star_gold",       "نجمة ذهبية",  "نجوم",   lambda: star_mask(SIZE, 5, 0.45),     (230, 190, 50)),
    ("star_red",        "نجمة حمراء",  "نجوم",   lambda: star_mask(SIZE, 5, 0.45),     (220, 50, 50)),
    ("star_blue",       "نجمة زرقاء",  "نجوم",   lambda: star_mask(SIZE, 5, 0.45),     (50, 120, 220)),
    ("star_cyan",       "نجمة سماوية", "نجوم",   lambda: star_mask(SIZE, 5, 0.45),     (50, 190, 220)),
    ("star_purple",     "نجمة بنفسجية","نجوم",   lambda: star_mask(SIZE, 5, 0.45),     (140, 70, 200)),

    ("hex_red",         "سداسي أحمر",  "مسدسات", lambda: polygon_mask(SIZE, 6),        (220, 50, 50)),
    ("hex_blue",        "سداسي أزرق",  "مسدسات", lambda: polygon_mask(SIZE, 6),        (50, 120, 220)),
    ("hex_green",       "سداسي أخضر",  "مسدسات", lambda: polygon_mask(SIZE, 6),        (50, 180, 100)),
    ("hex_purple",      "سداسي بنفسجي","مسدسات", lambda: polygon_mask(SIZE, 6),        (140, 70, 200)),
    ("hex_gold",        "سداسي ذهبي",  "مسدسات", lambda: polygon_mask(SIZE, 6),        (230, 190, 50)),

    ("pent_red",        "خماسي أحمر",  "خماسيات", lambda: polygon_mask(SIZE, 5),       (220, 50, 50)),
    ("pent_blue",       "خماسي أزرق",  "خماسيات", lambda: polygon_mask(SIZE, 5),       (50, 120, 220)),
    ("pent_green",      "خماسي أخضر",  "خماسيات", lambda: polygon_mask(SIZE, 5),       (50, 180, 100)),

    ("heart_red",       "قلب أحمر",    "قلوب",   lambda: heart_mask(SIZE),                           (230, 40, 80)),
    ("heart_pink",      "قلب وردي",    "قلوب",   lambda: heart_mask(SIZE),                           (240, 100, 180)),
    ("heart_purple",    "قلب بنفسجي",  "قلوب",   lambda: heart_mask(SIZE),                           (180, 60, 180)),

    ("capsule_blue",    "كبسولة زرقاء","كبسولات", lambda: capsule_mask(SIZE),                         (50, 120, 220)),
    ("capsule_gray",    "كبسولة رمادية","كبسولات",lambda: capsule_mask(SIZE),                         (120, 120, 130)),
    ("capsule_green",   "كبسولة خضراء", "كبسولات",lambda: capsule_mask(SIZE),                         (50, 180, 100)),

    ("octagon_red",     "ثماني أحمر",  "ثمانيات", lambda: polygon_mask(SIZE, 8, 22.5),  (220, 50, 50)),
    ("octagon_blue",    "ثماني أزرق",  "ثمانيات", lambda: polygon_mask(SIZE, 8, 22.5),  (50, 120, 220)),
]

print(f"🎨 توليد {len(SHAPES)} شكل...\n")

index = []
for i, (sid, display, cat, mask_fn, color) in enumerate(SHAPES, 1):
    mask = mask_fn()
    img = make_3d(mask, color)
    fname = f"{sid}.png"
    img.save(os.path.join(OUT_DIR, fname), "PNG", optimize=True)
    index.append({"file": fname, "name": display, "category": cat})
    print(f"  [{i}/{len(SHAPES)}] ✅ {display}")

# الفهرس
with open(os.path.join(OUT_DIR, "index.json"), "w", encoding="utf-8") as f:
    json.dump(index, f, ensure_ascii=False, indent=2)

print(f"\n🎉 تم توليد {len(SHAPES)} شكل")
print(f"📁 المجلد: {OUT_DIR}")
