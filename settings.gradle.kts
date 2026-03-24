pluginManagement {
    repositories {
        maven {
            name = "CaffeineMC"
            url = uri("https://maven.caffeinemc.net/releases")
        }
        gradlePluginPortal()
        mavenCentral()
        maven {
            name = "Fabric"
            url = uri("https://maven.fabricmc.net/")
        }
    }
}

plugins {
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "iseelava"
