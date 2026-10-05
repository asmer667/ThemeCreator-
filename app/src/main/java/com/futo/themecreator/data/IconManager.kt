package com.futo.themecreator.data

/**
 * الأيقونات المدعومة (من نظام matchrules في FUTO)
 */
object IconManager {

    val supportedIcons: List<IconSpec> = listOf(
        IconSpec(
            fileName = "button.png",
            displayName = "زر عادي",
            description = "الشكل الأساسي لجميع الأزرار",
            selector = "normal"
        ),
        IconSpec(
            fileName = "pressed_button.png",
            displayName = "زر مضغوط",
            description = "يظهر عند الضغط على الزر",
            selector = "pressed"
        ),
        IconSpec(
            fileName = "functional_button.png",
            displayName = "زر وظيفي",
            description = "زر Shift، Ctrl، إلخ",
            selector = "functional"
        ),
        IconSpec(
            fileName = "action_button.png",
            displayName = "زر الإجراء (Enter)",
            description = "زر Enter / Send / Search",
            selector = "action"
        ),
        IconSpec(
            fileName = "blank.png",
            displayName = "خلفية فارغة",
            description = "لون خلفية بدون شكل",
            selector = "blank"
        ),
    )

    /** الحصول على IconSpec بالاسم */
    fun getByName(fileName: String): IconSpec? =
        supportedIcons.firstOrNull { it.fileName == fileName }
}

data class IconSpec(
    val fileName: String,
    val displayName: String,
    val description: String,
    val selector: String,
)
