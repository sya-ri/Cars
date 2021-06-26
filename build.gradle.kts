import net.minecrell.pluginyml.bukkit.BukkitPluginDescription

plugins {
    java
    id("net.minecrell.plugin-yml.bukkit") version "0.4.0"
}

repositories {
    mavenCentral()
    maven(url = "https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven(url = "https://oss.sonatype.org/content/groups/public/")
    maven(url = "https://repo.dmulloy2.net/repository/public/")
    maven(url = "https://jitpack.io")
}

dependencies {
    implementation("org.jetbrains:annotations:16.0.2")
    implementation("org.spigotmc:spigot-api:1.12.2-R0.1-SNAPSHOT")
    implementation("com.comphenix.protocol:ProtocolLib:4.6.0")
    implementation("com.github.MilkBowl:VaultAPI:1.7")
}

configure<BukkitPluginDescription> {
    main = "com.celerry.cars.Cars"
    apiVersion = "1.16"
}
