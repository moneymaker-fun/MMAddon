import net.labymod.labygradle.common.extension.model.labymod.ReleaseChannels

plugins {
    id("net.labymod.labygradle")
    id("net.labymod.labygradle.addon")
}

val addonVersion = "1.7.1"

val versions = providers.gradleProperty("net.labymod.minecraft-versions").get().split(";")

group = "de.timuuuu"
version = providers.environmentVariable("VERSION").getOrElse(addonVersion)

tasks.withType<JavaCompile> {
    options.encoding = "UTF-8"
}

labyMod {
    defaultPackageName = "de.timuuuu.moneymaker" //change this to your main package name (used by all modules)

    minecraft {
        registerVersion(versions.toTypedArray()) {
            runs {
                getByName("client") {
                    // When the property is set to true, you can log in with a Minecraft account
                    // devLogin = true
                }
            }
        }
    }

    addonInfo {
        namespace = "moneymaker"
        displayName = "MoneyMaker"
        author = "Timuuuu, MisterCore"
        description = "Adds some features to the MoneyMaker IdleGame on GommeHD.net"
        minecraftVersion = "*"
        version = System.getenv().getOrDefault("VERSION", addonVersion)
        releaseChannel = ReleaseChannels.SNAPSHOT
    }

}

dependencies {
}

subprojects {
    plugins.apply("net.labymod.labygradle")
    plugins.apply("net.labymod.labygradle.addon")

    group = rootProject.group
    version = rootProject.version
}
