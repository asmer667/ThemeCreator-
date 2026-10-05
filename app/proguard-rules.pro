# Keep serialization classes
-keepattributes *Annotation*, InnerClasses
-dontnote kotlinx.serialization.AnnotationsKt
-keepclassmembers class kotlinx.serialization.json.** {
    *** Companion;
}
-keepclasseswithmembers class kotlinx.serialization.json.** {
    kotlinx.serialization.KSerializer serializer(...);
}
-keep,includedescriptorclasses class com.futo.themecreator.**$$serializer { *; }
-keepclassmembers class com.futo.themecreator.** {
    *** Companion;
}
-keepclasseswithmembers class com.futo.themecreator.** {
    kotlinx.serialization.KSerializer serializer(...);
}
