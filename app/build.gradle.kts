import java.time.LocalDateTime
import java.time.ZoneOffset
import java.time.format.DateTimeFormatter

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "es.rafagale.biometricbypass"
    compileSdk = 34

    defaultConfig {
        applicationId = "es.rafagale.biometricbypass"
        minSdk = 33
        targetSdk = 34

        versionCode = project.findProperty("versionCode")?.toString()?.toInt() ?: generateVersionCode()
        versionName = project.findProperty("versionName")?.toString() ?: generateVersionName()
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            signingConfig = signingConfigs["debug"]
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
}

dependencies {
    compileOnly("androidx.annotation:annotation:1.8.2")
    compileOnly(files("lib/xposed-api-82.jar"))
}

fun generateVersionCode(): Int {
    val now = LocalDateTime.now(ZoneOffset.UTC)
    return now.format(DateTimeFormatter.ofPattern("yyMMdd")).toInt()
}

fun generateVersionName(): String {
    val now = LocalDateTime.now(ZoneOffset.UTC)
    return now.format(DateTimeFormatter.ofPattern("dd.MM.yy"))
}
