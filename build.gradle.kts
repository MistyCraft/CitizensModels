plugins {
    java
    `maven-publish`
    id ("com.gradleup.shadow") version "8.3.0"
}

group = "top.mrxiaom.citizensmodels"
version = "1.0.0"
val targetJavaVersion = 17
val shadowGroup = "top.mrxiaom.citizensmodels.libs"

repositories {
    mavenCentral()
    maven("https://repo.codemc.io/repository/maven-public/")
    maven("https://hub.spigotmc.org/nexus/content/repositories/snapshots/")
    maven("https://maven.citizensnpcs.co/repo")
    maven("https://repo.helpch.at/releases/")
    maven("https://jitpack.io")
    maven("https://mvn.lumine.io/repository/maven-public/")
    maven("https://repo.glaremasters.me/repository/public/")
    maven("https://repo.rosewooddev.io/repository/public/")
}

dependencies {
    compileOnly("org.spigotmc:spigot-api:1.20-R0.1-SNAPSHOT")
    // compileOnly("org.spigotmc:spigot:1.20") // NMS

    compileOnly("me.clip:placeholderapi:2.11.6")
    compileOnly("net.citizensnpcs:citizens-main:2.0.33-SNAPSHOT")
    compileOnly("org.jetbrains:annotations:24.0.0")

    implementation("top.mrxiaom:PluginBase:1.5.9")

    for (subproject in project.project(":ModelEngine").subprojects) {
        implementation(subproject)
    }
}
java {
    val javaVersion = JavaVersion.toVersion(targetJavaVersion)
    if (JavaVersion.current() < javaVersion) {
        toolchain.languageVersion.set(JavaLanguageVersion.of(targetJavaVersion))
    }
}
tasks {
    shadowJar {
        mapOf(
            "top.mrxiaom.pluginbase" to "base",
        ).forEach { (original, target) ->
            relocate(original, "$shadowGroup.$target")
        }
    }
    val copyTask = create<Copy>("copyBuildArtifact") {
        dependsOn(shadowJar)
        from(shadowJar.get().outputs)
        rename { "${project.name}-$version.jar" }
        into(rootProject.file("out"))
    }
    build {
        dependsOn(copyTask)
    }
    withType<JavaCompile>().configureEach {
        options.encoding = "UTF-8"
        if (targetJavaVersion >= 10 || JavaVersion.current().isJava10Compatible) {
            options.release.set(targetJavaVersion)
        }
    }
    processResources {
        duplicatesStrategy = DuplicatesStrategy.INCLUDE
        from(sourceSets.main.get().resources.srcDirs) {
            expand(mapOf("version" to version))
            include("plugin.yml")
        }
    }
}
publishing {
    publications {
        create<MavenPublication>("maven") {
            from(components.getByName("java"))
            groupId = project.group.toString()
            artifactId = rootProject.name
            version = project.version.toString()
        }
    }
}
