import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed plugins {
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    id("maven-publish")
}

group = "com.androidai.shared.gemini.enhanced.model"
version = "1.1.0"

publishing {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/shreyas-android/GeminiEnhancedAPI")
            credentials {
                /**Create github.properties in root project folder file with gpr.usr=GITHUB_USER_ID  & gpr.key=PERSONAL_ACCESS_TOKEN**/
                username = System.getenv("GPR_USER")
                password = System.getenv("GPR_API_KEY")
            }
        }
    }
}

kotlin {

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release")
    }

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "model"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies { //put your multiplatform dependencies here

               implementation(libs.kotlin.coroutines.core)
                implementation("dev.shreyaspatil.generativeai:generativeai-google:0.9.0-1.0.1")
                api(project(":core:file"))
            }

        }
        val commonTest by getting {
            dependencies {
            }
        }

        val androidMain by getting {
            dependencies {
            }
        }

        val iosX64Main by getting
        val iosArm64Main by getting
        val iosSimulatorArm64Main by getting
        val iosMain by creating {
            dependsOn(commonMain)
            iosX64Main.dependsOn(this)
            iosArm64Main.dependsOn(this)
            iosSimulatorArm64Main.dependsOn(this)

            dependencies {
            }
        }
        val iosX64Test by getting
        val iosArm64Test by getting
        val iosSimulatorArm64Test by getting
        val iosTest by creating {
            dependsOn(commonTest)
            iosX64Test.dependsOn(this)
            iosArm64Test.dependsOn(this)
            iosSimulatorArm64Test.dependsOn(this)
        }

    }
}

android {
    namespace = "com.androidai.framework.shared.core.model"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}



task("testClasses").doLast {
    println("This is a dummy testClasses task")
}

