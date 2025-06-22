rootProject.name = "moneymaker"

pluginManagement {
    val labyGradlePluginVersion = "0.5.9"
    buildscript {
        repositories {
            maven("https://dist.labymod.net/api/v1/maven/release/")
            maven("https://maven.neoforged.net/releases/")
            maven("https://maven.fabricmc.net/")
            gradlePluginPortal()
            mavenCentral()
        }

        dependencies {
            classpath("net.labymod.gradle", "common", labyGradlePluginVersion)
            classpath("io.netty", "netty-all", "4.2.0.Final")
        }
    }
}

plugins.apply("net.labymod.labygradle.settings")

include(":api")
include(":core")