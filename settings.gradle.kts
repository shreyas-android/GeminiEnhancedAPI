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
        maven {
            url = uri("https://maven.pkg.github.com/shreyas-android/GeminiEnhancedAPI")
            credentials {
                /**Create github.properties in root project folder file with gpr.usr=GITHUB_USER_ID  & gpr.key=PERSONAL_ACCESS_TOKEN**/
                username = System.getenv("GPR_USER")
                password = System.getenv("GPR_API_KEY")
            }
        }
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
 