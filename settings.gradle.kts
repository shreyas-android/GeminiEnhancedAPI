pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
        maven { url = uri("https://maven.google.com") }
       /* maven {
            url = uri("https://maven.pkg.github.com/shreyas-android/GeminiAPI")
            credentials(PasswordCredentials::class)
            authentication {
                create<BasicAuthentication>("basic")
            }

        }*/
    }
}

rootProject.name = "GeminiAPI"
include(":app")
include(":core:file")
include(":core:model")
 