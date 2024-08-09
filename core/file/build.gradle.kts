import org.jetbrains.kotlin.js.translate.context.Namer.kotlin

@Suppress("DSL_SCOPE_VIOLATION") // TODO: Remove once KTIJ-19369 is fixed plugins {
plugins {
    kotlin("multiplatform")
    id("com.android.library")
    kotlin("plugin.serialization")
    id("maven-publish")
}

group = "com.androidai.framework.shared.core.file"
version = "1.0.0-alpha01"

/*publishing {
    afterEvaluate {
        extensions.getByType(PublishingExtension::class.java).apply {
            publications {
                create("release", MavenPublication::class.java) {
                    groupId = "com.androidai.framework.shared.core.fiile"
                    artifactId = "gemini-file"
                    version = "1.0.0-alpha01"

                    afterEvaluate {
                        from(components["release"])
                    }
                }
            }
        }
    }
}*/

kotlin {

    android {
        compilations.all {
            kotlinOptions {
                jvmTarget = "1.8"
            }
        }
        publishLibraryVariants("release", "debug")
        /**/
    }

    listOf(
        iosX64(), iosArm64(), iosSimulatorArm64()).forEach {
        it.binaries.framework {
            baseName = "chat"
        }
    }

    sourceSets {
        val commonMain by getting {
            dependencies { //put your multiplatform dependencies here
                implementation(libs.atomicfu)
                implementation(libs.kotlin.coroutines.core)
                implementation(libs.bundles.ktor.common)
            }

        }
        val commonTest by getting {
            dependencies { // implementation(libs.kotlin.test) }
            }
        }

        val androidMain by getting {
            dependencies { // implementation(libs.sqlDelight.android.driver)
                implementation(libs.bundles.ktor.android)
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
                implementation(libs.bundles.ktor.ios)
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
    namespace = "com.androidai.framework.shared.core.file"
    compileSdk = 34
    defaultConfig {
        minSdk = 24
    }
}



task("testClasses").doLast {
    println("This is a dummy testClasses task")
}
