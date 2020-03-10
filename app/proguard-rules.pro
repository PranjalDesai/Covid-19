-keepattributes Signature
-optimizationpasses 3
-allowaccessmodification
-dontskipnonpubliclibraryclasses
-dontusemixedcaseclassnames
-optimizations !code/simplification/arithmetic, !code/simplification/cast, !field/*,!class/merging/*, !method/inlining/*
-overloadaggressively
-verbose

-keepattributes *Annotation*,EnclosingMethod,Exceptions,InnerClasses,LineNumberTable,Signature,SourceFile

-dontwarn com.google.**
-dontwarn com.google.android.gms.**
-dontwarn com.jayway.jsonpath.spi.json.**
-dontwarn com.jayway.jsonpath.spi.mapper.**
-dontwarn com.squareup.picasso.**
-dontwarn io.fabric.sdk.android.services.common.**
-dontwarn javax.annotation.**
-dontwarn kotlin.internal.**
-dontwarn kotlinx.coroutines.flow.**
-dontwarn okhttp3.**
-dontwarn okio.**
-dontwarn org.conscrypt.**
-dontwarn org.slf4j.*
-dontwarn org.xmlpull.**
-dontwarn sun.misc.**

-keep class com.pranjaldesai.coronavirustracker.data.models.** { *; }
-keep class !android.support.v7.internal.view.menu.**, android.support.** { *; }
-keep class * extends android.app.Activity
-keep class * extends android.app.Application
-keep class * extends android.app.backup.BackupAgentHelper
-keep class * extends android.app.Service
-keep class * extends android.content.BroadcastReceiver
-keep class * extends android.content.ContentProvider
-keep class * extends android.preference.Preference
-keep class * extends android.support.multidex.MultiDexApplication
-keep class * extends android.view.View
-keep class * extends java.util.ListResourceBundle { protected java.lang.Object[][] getContents(); }
-keep class * implements android.os.Parcelable { public static final android.os.Parcelable$Creator *; }
-keep class * implements java.io.Serializable { *; }
-keep class **.R$* { *; }
-keep class android.support.** { *; }
-keep class androidx.** { *; }
-keep class com.fastmodelsports.authentication.model.** { *; }
-keep class com.fastmodelsports.core.model.** { *; }
-keep class com.google.**{ *; }
-keep class com.google.gson.** { *; }
-keep class com.google.gson.examples.android.model.** { *; }
-keep class com.google.gson.stream.** { *; }
-keep class com.google.inject.** { *; }
-keep class com.squareup.okhttp3.** { *; }
-keep class com.squareup.picasso.** { *; }
-keep class org.apache.http.**
-keep class retrofit2.** { *; }
-keep class sun.misc.Unsafe { *; }
-keep enum ** { public *; }
-keep interface ** { *; }
-keep public class * extends android.view.View{ *** get*(); void set*(***); public <init>(android.content.Context); public <init>(android.content.Context, android.util.AttributeSet); public <init>(android.content.Context, android.util.AttributeSet, int);}

-keepclasseswithmembernames class * { native <methods>; }

-keepclasseswithmembers class * { @retrofit2.http.* <methods>; }
-keepclasseswithmembers class * { public <init>(android.content.Context, android.util.AttributeSet); public <init>(android.content.Context, android.util.AttributeSet, int); }
-keepclasseswithmembers class ** { public *; }

-keepclassmembernames class kotlinx.** { volatile <fields>; }

-keepclassmembers class * extends android.app.Activity { public void *(android.view.View); }
-keepclassmembers class * extends android.webkit.WebViewClient { public void *(android.webkit.WebView, java.lang.String); public void *(android.webkit.WebView, java.lang.String, android.graphics.Bitmap); public boolean *(android.webkit.WebView, java.lang.String);}
-keepclassmembers class * implements java.io.Serializable { static final long serialVersionUID; private static final java.io.ObjectStreamField[] serialPersistentFields; private void writeObject(java.io.ObjectOutputStream); private void readObject(java.io.ObjectInputStream); java.lang.Object writeReplace(); java.lang.Object readResolve(); }
-keepclassmembers class * { @com.google.android.gms.common.annotation.KeepName *; }
-keepclassmembers,allowshrinking,allowobfuscation interface * {@retrofit2.http.* <methods>; }

-keepnames @com.google.android.gms.common.annotation.KeepName class *
-keepnames class * implements android.os.Parcelable { public static final ** CREATOR; }
-keepnames class ** { public *; }
-keepnames class com.google.android.material.** { public *; }
-keepnames class kotlinx.coroutines.android.AndroidDispatcherFactory {}
-keepnames class kotlinx.coroutines.android.AndroidExceptionPreHandler {}
-keepnames class kotlinx.coroutines.CoroutineExceptionHandler {}
-keepnames class kotlinx.coroutines.internal.MainDispatcherFactory {}
-keepnames class okhttp3.internal.publicsuffix.PublicSuffixDatabase


-keepclassmembers class com.pranjaldesai.coronavirustracker.data.models.models.** {
      *;
    }