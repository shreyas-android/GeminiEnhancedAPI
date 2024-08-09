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
        maven { url =  uri("https://maven.google.com") } // For Gradle < 4.0
    }
}

rootProject.name = "GeminiAPI"
include(":app")
include(":core:file")
 