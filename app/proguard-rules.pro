# Add project specific ProGuard rules here.
# By default, the flags in this file are appended to flags specified
# in D:\COMPETITION\AndroidStudio\SDK/tools/proguard/proguard-android.txt
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
-dontwarn okhttp3.internal.platform.*
-keep class org.apache.http.** { *; }
-dontwarn org.apache.http.**
-dontwarn android.net.**
-dontwarn com.squareup.picasso.**
-dontwarn javax.annotation.**
-dontwarn okio.**
-dontwarn retrofit2.Platform$Java8
-keepclassmembers class com.paytm.pgsdk.PaytmWebView$PaytmJavaScriptInterface {
   public *;
}
-dontwarn io.realm.**
-dontwarn javax.xml.stream.events.**