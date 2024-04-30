# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\Copy\Intuition Softwares\1_Android app development\2_Android workspace\Android Studio Workspace\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

-dontwarn java.awt.**
-dontwarn java.beans.**
-dontwarn com.RT_Printer.**
-dontwarn javax.security.**
-dontwarn android.util.**
-dontwarn com.parse.**

-keep class com.epson.** { *; }
-keep class au.com.bytecode.opencsv** { *; }
-keep class com.RT_Printer.** { *; }
-keep class com.squareup.** { *; }
-keep class okio.** { *; }

-keep class com.amazonaws.** { *; }
-keep class com.amazon.** { *; }

-dontwarn com.amazonaws.util.json.**
-dontwarn com.amazonaws.http.**
-dontwarn com.amazonaws.metrics.**

#new
-keepnames class com.fasterxml.jackson.databind.** { *; }
-dontwarn com.fasterxml.jackson.databind.**

-keepattributes InnerClassesokio.**
-keep class java.beans.** {*;}
-keep public class !testAppH23.** { *;}
-dontwarn com.dspread.xpos.**
-dontwarn okio.**
-dontwarn com.bbpos.emvswipe.**
-dontwarn com.bbpos.wisepad.**
-dontwarn org.apache.commons.**
-dontwarn org.codehaus.jackson.map.**
-dontwarn org.codehaus.jackson.annotate.**
-dontwarn org.apache.commons.lang.**
-dontwarn org.apache.commons.io.**
-dontwarn org.codehaus.jackson.**
-dontwarn org.apache.commons.lang.ArrayUtils.**
#-dontwarn com.bbpos.simplyprint.**

-ignorewarnings

-keep class * {
    public private *;
}

-keep class com.crashlytics.** { *; }
-dontwarn com.crashlytics.**

# Preserve annotations, line numbers, and source file names
-keepattributes *Annotation*,SourceFile,LineNumberTable
# rename the source files to something meaningless, but it must be retained
-renamesourcefileattribute ''

# If your project uses WebView with JS, uncomment the following
# and specify the fully qualified class name to the JavaScript interface
# class:
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}
