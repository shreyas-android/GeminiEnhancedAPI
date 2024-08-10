plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.jetbrains.kotlin.android)
}

android {
    namespace = "com.androidai.module.geminiapi"
    compileSdk = 34

    val keystoreFile = project.rootProject.file("cred.properties")
    val properties = org.jetbrains.kotlin.konan.properties.Properties()
    properties.load(keystoreFile.inputStream())

    defaultConfig {
        applicationId = "com.androidai.module.geminiapi"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        // Set API keys in BuildConfig
        buildConfigField(
            "String", "android_ai_gemini_api_key",
            properties.getProperty("GEMINI_API_KEY"))
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"), "proguard-rules.pro")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.4"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
}

dependencies {

    implementation(libs.core.ktx)
    implementation(libs.lifeCycleRuntime)
    implementation(libs.compose.activity)
    implementation(libs.compose.foundation)
    implementation(libs.compose.material)
    implementation(libs.compose.material3)
    testImplementation(libs.junit)
    implementation(libs.compose.viewmodel)

    implementation(libs.compose.constraintlayout)
    implementation(libs.activity)
    implementation(libs.lifecycle.extensions) // implementation(libs.lifeCycleExtension)
    implementation(libs.lifeCycleRuntime)
    implementation(libs.lifeCycleProcess)
    implementation(project(":core:model"))
}