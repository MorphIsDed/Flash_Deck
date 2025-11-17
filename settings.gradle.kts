pluginManagement {
    repositories {
        google() {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()
    }
    // NOTE: We removed the manual 'versionCatalogs' block here
    // because Android Studio loads gradle/libs.versions.toml automatically.
}

rootProject.name = "FlashDeck" // Or "My Application"
include(":app")