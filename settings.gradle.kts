pluginManagement {
    repositories {
        google {
            mavenContent {
                google()
                mavenCentral()
                gradlePluginPortal()
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
}

rootProject.name = "CircularTimePickerView"
include(":app")
include(":CircularTimePicker")
