# Add project specific ProGuard rules here.
-keepattributes *Annotation*
-keep class com.videnring.** { *; }
-keepclassmembers class * {
    @kotlinx.serialization.Serializable *;
}
-keep class kotlinx.serialization.** { *; }
-keepclassmembers class kotlinx.serialization.json.** { *; }
