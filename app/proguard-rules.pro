# PermiTrack release rules
# Room schemas and Kotlin metadata are kept by their libraries.
-keep class com.permitrack.app.data.** { *; }
-keepclassmembers class * { @androidx.room.* <fields>; }
