package com.futo.themecreator.data

import org.json.JSONObject
import java.net.HttpURLConnection
import java.net.URL

/**
 * كتالوج خطوط Google Fonts.
 * يجمع:
 *  - قائمة مضمّنة (~40 خطًا عربي/إنجليزي)
 *  - ~1500 خط من Google Fonts Metadata API
 *  - الخطوط المستوردة من الجهاز (filesDir/fonts/)
 */
data class FontInfo(
    val id: String,
    val displayName: String,
    val fileName: String,
    val url: String,
    val language: FontLanguage,
    val category: FontCategory,
    val source: FontSource = FontSource.BUILT_IN,
)

enum class FontLanguage { ARABIC, ENGLISH, OTHER }
enum class FontCategory { SANS, SERIF, DISPLAY, HANDWRITING, MONOSPACE }
enum class FontSource { BUILT_IN, GOOGLE_API, IMPORTED }

object FontCatalog {

    private const val GH = "https://github.com/google/fonts/raw/main"
    private const val GF_API = "https://fonts.google.com/metadata/fonts"

    val BUILT_IN: List<FontInfo> = listOf(
        FontInfo("cairo", "\u0627\u0644\u0642\u0627\u0647\u0631\u0629", "Cairo-Regular.ttf",
            "$GH/ofl/cairo/Cairo%5Bslnt%2Cwght%5D.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("tajawal", "\u062a\u062c\u0648\u0651\u0644", "Tajawal-Regular.ttf",
            "$GH/ofl/tajawal/Tajawal-Regular.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("almarai", "\u0627\u0644\u0645\u0631\u0627\u0639\u064a", "Almarai-Regular.ttf",
            "$GH/ofl/almarai/Almarai-Regular.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("amiri", "\u0623\u0645\u064a\u0631\u064a", "Amiri-Regular.ttf",
            "$GH/ofl/amiri/Amiri-Regular.ttf", FontLanguage.ARABIC, FontCategory.SERIF),
        FontInfo("changa", "\u0634\u0646\u063a\u0647\u0627", "Changa-Regular.ttf",
            "$GH/ofl/changa/Changa-Regular.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("elmessiri", "\u0627\u0644\u0645\u0635\u064a\u0631\u064a", "ElMessiri-Regular.ttf",
            "$GH/ofl/elmessiri/ElMessiri%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("reemkufi", "\u0631\u064a\u0645 \u0643\u0648\u0641\u064a", "ReemKufi-Regular.ttf",
            "$GH/ofl/reemkufi/ReemKufi%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.DISPLAY),
        FontInfo("markazi", "\u0645\u0631\u0643\u0632\u064a", "MarkaziText-Regular.ttf",
            "$GH/ofl/markazitext/MarkaziText%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.SERIF),
        FontInfo("kufam", "\u0643\u0648\u0641\u0627\u0645", "Kufam-Regular.ttf",
            "$GH/ofl/kufam/Kufam%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.DISPLAY),
        FontInfo("lateef", "\u0644\u0637\u064a\u0641", "Lateef-Regular.ttf",
            "$GH/ofl/lateef/Lateef-Regular.ttf", FontLanguage.ARABIC, FontCategory.SERIF),
        FontInfo("scheherazade", "\u0634\u0647\u0631\u0632\u0627\u062f", "ScheherazadeNew-Regular.ttf",
            "$GH/ofl/scheherazadenew/ScheherazadeNew-Regular.ttf", FontLanguage.ARABIC, FontCategory.SERIF),
        FontInfo("notoarabic", "\u0646\u0648\u062a\u0648 \u0639\u0631\u0628\u064a", "NotoNaskhArabic-Regular.ttf",
            "$GH/ofl/notonaskharabic/NotoNaskhArabic%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("ibmplexarabic", "IBM \u0628\u0644\u0643\u0633 \u0639\u0631\u0628\u064a", "IBMPlexSansArabic-Regular.ttf",
            "$GH/ofl/ibmplexsansarabic/IBMPlexSansArabic-Regular.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("vibes", "\u0641\u0627\u064a\u0628\u0633", "Vibes-Regular.ttf",
            "$GH/ofl/vibes/Vibes-Regular.ttf", FontLanguage.ARABIC, FontCategory.DISPLAY),
        FontInfo("lemonada", "\u0644\u064a\u0645\u0648\u0646\u0627\u062f\u0629", "Lemonada-Regular.ttf",
            "$GH/ofl/lemonada/Lemonada%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.DISPLAY),
        FontInfo("mada", "\u0645\u062f\u0649", "Mada-Regular.ttf",
            "$GH/ofl/mada/Mada%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.SANS),
        FontInfo("baloo", "\u0628\u0627\u0644\u0648 \u0628\u0647\u0627\u062c\u0627\u0646", "BalooBhaijaan2-Regular.ttf",
            "$GH/ofl/baloobhaijaan2/BalooBhaijaan2%5Bwght%5D.ttf", FontLanguage.ARABIC, FontCategory.DISPLAY),
        FontInfo("cairoplay", "\u0627\u0644\u0642\u0627\u0647\u0631\u0629 \u0628\u0644\u0627\u064a", "CairoPlay-Regular.ttf",
            "$GH/ofl/cairoplay/CairoPlay%5Bslnt%2Cwght%5D.ttf", FontLanguage.ARABIC, FontCategory.DISPLAY),
        FontInfo("alkalami", "\u0627\u0644\u0642\u0644\u0645\u064a", "Alkalami-Regular.ttf",
            "$GH/ofl/alkalami/Alkalami-Regular.ttf", FontLanguage.ARABIC, FontCategory.SERIF),
        FontInfo("mirza", "\u0645\u064a\u0631\u0632\u0627", "Mirza-Regular.ttf",
            "$GH/ofl/mirza/Mirza-Regular.ttf", FontLanguage.ARABIC, FontCategory.SERIF),

        FontInfo("roboto", "Roboto", "Roboto-Regular.ttf",
            "$GH/apache/roboto/Roboto%5Bwdth%2Cwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("opensans", "Open Sans", "OpenSans-Regular.ttf",
            "$GH/apache/opensans/OpenSans%5Bwdth%2Cwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("lato", "Lato", "Lato-Regular.ttf",
            "$GH/ofl/lato/Lato-Regular.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("montserrat", "Montserrat", "Montserrat-Regular.ttf",
            "$GH/ofl/montserrat/Montserrat%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("poppins", "Poppins", "Poppins-Regular.ttf",
            "$GH/ofl/poppins/Poppins-Regular.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("inter", "Inter", "Inter-Regular.ttf",
            "$GH/ofl/inter/Inter%5Bopsz%2Cwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("rubik", "Rubik", "Rubik-Regular.ttf",
            "$GH/ofl/rubik/Rubik%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("nunito", "Nunito", "Nunito-Regular.ttf",
            "$GH/ofl/nunito/Nunito%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("oswald", "Oswald", "Oswald-Regular.ttf",
            "$GH/ofl/oswald/Oswald%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.DISPLAY),
        FontInfo("raleway", "Raleway", "Raleway-Regular.ttf",
            "$GH/ofl/raleway/Raleway%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SANS),
        FontInfo("merriweather", "Merriweather", "Merriweather-Regular.ttf",
            "$GH/ofl/merriweather/Merriweather%5Bopsz%2Cwdth%2Cwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SERIF),
        FontInfo("playfair", "Playfair Display", "PlayfairDisplay-Regular.ttf",
            "$GH/ofl/playfairdisplay/PlayfairDisplay%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.SERIF),
        FontInfo("dancing", "Dancing Script", "DancingScript-Regular.ttf",
            "$GH/ofl/dancingscript/DancingScript%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.HANDWRITING),
        FontInfo("pacifico", "Pacifico", "Pacifico-Regular.ttf",
            "$GH/ofl/pacifico/Pacifico-Regular.ttf", FontLanguage.ENGLISH, FontCategory.HANDWRITING),
        FontInfo("lobster", "Lobster", "Lobster-Regular.ttf",
            "$GH/ofl/lobster/Lobster-Regular.ttf", FontLanguage.ENGLISH, FontCategory.DISPLAY),
        FontInfo("bebasneue", "Bebas Neue", "BebasNeue-Regular.ttf",
            "$GH/ofl/bebasneue/BebasNeue-Regular.ttf", FontLanguage.ENGLISH, FontCategory.DISPLAY),
        FontInfo("righteous", "Righteous", "Righteous-Regular.ttf",
            "$GH/ofl/righteous/Righteous-Regular.ttf", FontLanguage.ENGLISH, FontCategory.DISPLAY),
        FontInfo("caveat", "Caveat", "Caveat-Regular.ttf",
            "$GH/ofl/caveat/Caveat%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.HANDWRITING),
        FontInfo("comfortaa", "Comfortaa", "Comfortaa-Regular.ttf",
            "$GH/ofl/comfortaa/Comfortaa%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.DISPLAY),
        FontInfo("jetbrainsmono", "JetBrains Mono", "JetBrainsMono-Regular.ttf",
            "$GH/ofl/jetbrainsmono/JetBrainsMono%5Bwght%5D.ttf", FontLanguage.ENGLISH, FontCategory.MONOSPACE),
    )

    private var cached: List<FontInfo>? = null

    suspend fun fetchAll(): List<FontInfo> {
        cached?.let { return it }
        val result = try {
            val conn = URL(GF_API).openConnection() as HttpURLConnection
            conn.connectTimeout = 20_000
            conn.readTimeout = 20_000
            conn.instanceFollowRedirects = true
            conn.connect()
            if (conn.responseCode != 200) { conn.disconnect(); emptyList() }
            else {
                val text = conn.inputStream.bufferedReader().readText()
                conn.disconnect()
                parseApiResponse(text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
            emptyList()
        }
        val combined = (BUILT_IN + result).distinctBy { it.id }
        cached = combined
        return combined
    }

    private fun parseApiResponse(text: String): List<FontInfo> {
        val out = mutableListOf<FontInfo>()
        try {
            val jsonStart = text.indexOf('{')
            if (jsonStart < 0) return emptyList()
            val root = JSONObject(text.substring(jsonStart))
            val items = root.optJSONArray("familyMetadataList") ?: return emptyList()
            for (i in 0 until items.length()) {
                val o = items.optJSONObject(i) ?: continue
                val family = o.optString("family") ?: continue
                if (family.isEmpty()) continue

                val id = family.lowercase().replace(" ", "").replace("-", "")
                val file = family.replace(" ", "") + "-Regular.ttf"
                val url = "$GH/ofl/${id}/${file}"

                val subsets = o.optJSONArray("subsets")
                val hasArabic = (0 until (subsets?.length() ?: 0))
                    .any { subsets?.optString(it) == "arabic" }
                val lang = if (hasArabic) FontLanguage.ARABIC else FontLanguage.ENGLISH
                val cat = when (o.optString("category")) {
                    "serif" -> FontCategory.SERIF
                    "display" -> FontCategory.DISPLAY
                    "handwriting" -> FontCategory.HANDWRITING
                    "monospace" -> FontCategory.MONOSPACE
                    else -> FontCategory.SANS
                }
                out.add(FontInfo(
                    id = id,
                    displayName = family,
                    fileName = file,
                    url = url,
                    language = lang,
                    category = cat,
                    source = FontSource.GOOGLE_API,
                ))
            }
        } catch (e: Exception) { e.printStackTrace() }
        return out
    }

    val ARABIC: List<FontInfo> get() = (cached ?: BUILT_IN).filter { it.language == FontLanguage.ARABIC }
    val ENGLISH: List<FontInfo> get() = (cached ?: BUILT_IN).filter { it.language == FontLanguage.ENGLISH }

    fun byId(id: String): FontInfo? =
        (cached ?: BUILT_IN).find { it.id == id }


/**
 * يحلّ URL الفعلي لخط Google Fonts باستخدام CSS API.
 * يعيد رابط TTF مباشر. يُنفَّذ عند الحاجة فقط.
 *
 * مثال: Cairo → https://fonts.gstatic.com/s/cairo/v28/...ttf
 */
private const val CSS_API = "https://fonts.googleapis.com/css2"

suspend fun resolveGoogleFontUrl(family: String): String? {
    return try {
        kotlinx.coroutines.withContext(kotlinx.coroutines.Dispatchers.IO) {
            val url = "$CSS_API?family=" + family.replace(" ", "+")
            val conn = URL(url).openConnection() as HttpURLConnection
            conn.setRequestProperty("User-Agent", "Mozilla/5.0")
            conn.connectTimeout = 15_000
            conn.readTimeout = 15_000
            conn.connect()
            if (conn.responseCode != 200) { conn.disconnect(); null }
            else {
                val css = conn.inputStream.bufferedReader().readText()
                conn.disconnect()
                // نبحث عن أول url(...)
                val regex = Regex("url\\(([^)]+\\.ttf)\\)")
                regex.find(css)?.groupValues?.get(1)
            }
        }
    } catch (e: Exception) {
        e.printStackTrace()
        null
    }
}
}
