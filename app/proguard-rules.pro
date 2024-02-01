# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in C:\Users\81700905\AppData\Local\Android\Sdk/tools/proguard/proguard-android.txt
# You can edit the include path and order by changing the proguardFiles
# directive in build.gradle.
#
# For more details, see
#   http://developer.android.com/guide/developing/tools/proguard.html

# Add any project specific keep options here:

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
-keepattributes InnerClasses
-dontoptimize
#로그 봄
#-verbose
# 압축 하지 않음 그냥 하지말자..
-dontoptimize
#사용하지 않는 메소드를 유지하라
-dontshrink

-dontwarn org.apache.**
#-dontwarn (Warnig이 나온 클래스).**

# 빌드시 can’t find superclass or interface // can’t find referenced class 등의  Warnig 이 나올경우
#클래스 Warnig 을 무시 한다
#라이브러리 추가
#-libraryjars libs/android-support-v4.jar
#-libraryjars libs/json-simple-1.1.1.jar

-keep public class * { public protected *; }
-keep class androidx.core.app.CoreComponentFactory{*;}
#public class 와   protected class 의 경우 를 난독화 하지 않는다.
#public class 를 난독화 할경우 메소드 호출중 문제가 될수 있음….

#org.apache.http.하위 클래스를 전부 난독화 하지 않음
-keep class org.apache.http.**
#org.apache.http.  하위 인터페이스를 난독화 하지 않는다
-keep interface org.apache.http.**

-keep class  org.apache.http.** {
 public *;
 }

# AndroidUtilCode
-keep class com.blankj.utilcode.** { *; }
-keepclassmembers class com.blankj.utilcode.** { *; }
-dontwarn com.blankj.utilcode.**

# butterknife
-keep class butterknife.** { *; }
-dontwarn butterknife.internal.**
-keep class **$$ViewBinder { *; }

-keepclasseswithmembernames class * {
    @butterknife.* <fields>;
}

-keepclasseswithmembernames class * {
    @butterknife.* <methods>;
}

# Proguard rules that are applied to your test apk/code.
-ignorewarnings

-keepattributes *Annotation*

-dontnote junit.framework.**
-dontnote junit.runner.**

-dontwarn android.test.**
-dontwarn android.support.test.**
-dontwarn org.junit.**
-dontwarn org.hamcrest.**
-dontwarn com.squareup.javawriter.JavaWriter
# Uncomment this if you use Mockito
#-dontwarn org.mockito.**

# 백신
#-keep class com.secureland.smartmedic.** { *; }
#-keepclassmembers class com.secureland.smartmedic.** { *; }
#-dontwarn com.secureland.smartmedic.**
#
#-dontwarn com.TouchEn.mVaccine.**


# 백신
-dontwarn org.apache.**
-dontwarn com.TouchEn.mVaccine.b2b2c.activity.**
-dontwarn com.secureland.smartmedic.core.**
-dontwarn com.bitdefender.antimalware.BDAVScanner

-keep class com.bitdefender.antimalware.BDAVScanner { *; }
-keepclassmembers class com.bitdefender.antimalware.BDAVScanner { *; }

-keepclasseswithmembernames class com.bitdefender.antimalware.BDAVScanner {

    private java.lang.String m_threadName;
    private int m_scanResult;
    private int m_threatType;
}
-keepclasseswithmembernames class * {
    native <methods>;
}
-keepclasseswithmembernames class * {
    *** *Callback(...);
}
-keepclasseswithmembernames class * {
    int callbackEvent*(...);
}
-keep class com.dongbu.dsm.R$styleable {
    *;
}

#MDM
-keep class com.raonsecure.touchen_mguard_4_0.** { *; }
-keepclassmembers class com.raonsecure.touchen_mguard_4_0.** { *; }
-dontwarn com.raonsecure.touchen_mguard_4_0.**

#미네르바
-keep class com.intsig.nativelib.** { *; }
-keepclassmembers class com.intsig.nativelib.** { *; }
-dontwarn com.intsig.nativelib.**

-keep class com.minerva.magicbcreaderlib.** { *; }
-keepclassmembers class com.minerva.magicbcreaderlib.** { *; }
-dontwarn com.minerva.magicbcreaderlib.**

-keep class kr.co.abrain.image.** { *; }
-keepclassmembers class kr.co.abrain.image.** { *; }
-dontwarn kr.co.abrain.image.**




#카카오톡
-keep class com.kakao.** { *; }
-keepattributes Signature
-keepclassmembers class * {
  public static <fields>;
  public *;
}

-keepclassmembers class com.dongbu.dsm.common.CommonData {
  public static <fields>;
  public *;
}
-dontwarn android.support.v4.**,org.slf4j.**,com.google.android.gms.**

##---------------Begin: proguard configuration for Gson  ----------
# Gson uses generic type information stored in a class file when working with fields. Proguard
# removes such information by default, so configure it to keep all of it.
-keepattributes Signature

# For using GSON @Expose annotation
-keepattributes *Annotation*

# Gson specific classes
-dontwarn sun.misc.**
#-keep class com.google.gson.stream.** { *; }

# Application classes that will be serialized/deserialized over Gson
-keep class com.google.gson.examples.android.model.** { *; }

# Prevent proguard from stripping interface information from TypeAdapterFactory,
# JsonSerializer, JsonDeserializer instances (so they can be used in @JsonAdapter)
-keep class * implements com.google.gson.TypeAdapterFactory
-keep class * implements com.google.gson.JsonSerializer
-keep class * implements com.google.gson.JsonDeserializer

-dontnote sun.misc.Unsafe
-dontnote com.google.gson.internal.UnsafeAllocator

##---------------End: proguard configuration for Gson  ----------
