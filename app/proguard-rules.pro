# Keep Xposed classes and methods
-keep class * implements de.robv.android.xposed.IXposedHookLoadPackage {
    *;
}

-keepclassmembers class * implements de.robv.android.xposed.IXposedHookLoadPackage {
    public *;
}

-keepclasseswithmembers class * {
    public void handleLoadPackage(de.robv.android.xposed.callbacks.XC_LoadPackage$LoadPackageParam);
}

# Optionally, keep line numbers for easier debugging
-keepattributes SourceFile,LineNumberTable

# Optionally, keep WebView JavaScript interface methods
#-keepclassmembers class fqcn.of.javascript.interface.for.webview {
#   public *;
#}

# Optionally, disable ProGuard optimization
#-dontoptimize
