# Add project specific ProGuard rules here.
# You can control the set of applied configuration files using the
# proguardFiles setting in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Uncomment this to preserve the line number information for
# debugging stack traces.
#-keepattributes SourceFile,LineNumberTable

# If you keep the line number information, uncomment this to
# hide the original source file name.
#-renamesourcefileattribute SourceFile

# Keep the classes related to Moshi and Kotlin reflection
-keep class com.squareup.moshi.** { *; }
-keep class kotlin.reflect.** { *; }

# Keep the classes that define your model objects and enums
-keep class com.p4r4d0x.domain.bo.** { *; }
-keep class com.p4r4d0x.data.dto.** { *; }


# Keep the members of your model objects (fields, methods, etc.)
-keepclassmembers class com.p4r4d0x.domain.bo.** { *; }
-keepclassmembers class com.p4r4d0x.data.dto.** { *; }

# Keep the generated Moshi JsonAdapter classes
-keepclasseswithmembers class * {
    @com.squareup.moshi.* <methods>;
}

# Keep the methods in your model objects annotated with @Json
-keepclassmembers class com.p4r4d0x.domain.bo.** {
    @com.squareup.moshi.Json* <methods>;
}
-keepclassmembers class com.p4r4d0x.data.dto.** {
    @com.squareup.moshi.Json* <methods>;
}