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


-keep class com.smnotes.domain.model.** { *; }

# Remote DTOs — field names must not be renamed (they map to JSON keys sent over the wire)
-keep class com.smnotes.data.remote.dto.** { *; }

# kotlinx.serialization — preserve the $$serializer companion objects the plugin generates
-keepattributes *Annotation*, InnerClasses
-keep,includedescriptorclasses class com.smnotes.data.remote.dto.**$$serializer { *; }
-keepclassmembers class com.smnotes.data.remote.dto.** {
    *** Companion;
}
-keepclasseswithmembers class com.smnotes.data.remote.dto.** {
    kotlinx.serialization.KSerializer serializer(...);
}