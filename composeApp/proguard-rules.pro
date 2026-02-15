# ProGuard rules for WordsMultiPlatform Desktop application

# Keep main class
-keep class vm.words.ua.MainKt {
    public static void main(java.lang.String[]);
}

# Suppress warnings for missing BouncyCastle classes (optional dependency for PDFBox encryption)
-dontwarn org.bouncycastle.**

# Suppress warnings for optional Log4j classes
-dontwarn org.apache.log4j.**

# Suppress warnings for optional Android classes in cross-platform libraries
-dontwarn android.util.Log

# Suppress warnings for optional Sun/Oracle JDK internal classes
-dontwarn sun.java2d.cmm.kcms.KcmsServiceProvider
-dontwarn javax.xml.bind.DatatypeConverter
-dontwarn com.ibm.uvm.tools.DebugSupport

# Suppress warnings for optional Ktor internal classes
-dontwarn io.ktor.utils.io.jvm.javaio.PollersKt

# Keep JNA reflection usage
-keepclassmembers class * extends com.sun.jna.Structure {
    <init>();
}

-keepclassmembers class com.sun.jna.** {
    <init>();
}

# Keep dynamic method invocations
-keepclassmembers class * {
    *** getPeer();
    *** setAlpha(float);
    *** getVisual();
    *** values();
    *** getValue();
    *** isParkingAllowed();
    *** getPath();
    *** suite();
    *** getContextClassLoader();
    *** decode(java.lang.String);
    *** parseBase64Binary(java.lang.String);
    *** value();
    *** log(org.slf4j.event.LoggingEvent);
}

# Don't warn about library classes depending on program classes
-dontwarn **

# Keep all class members accessed via reflection
-keepclassmembers class * {
    <init>();
    <init>(int);
    <init>(java.lang.String);
}

# Preserve metadata for Kotlin
-keep class kotlin.Metadata { *; }

# Keep Compose runtime classes
-keep class androidx.compose.** { *; }
-keep class org.jetbrains.compose.** { *; }

# Keep resource files
-keepclassmembers class **.R$* {
    public static <fields>;
}

# Don't obfuscate - makes debugging easier
-dontobfuscate

# Optimize
-optimizationpasses 5
-allowaccessmodification

# Ignore duplicate resource files
-ignorewarnings

