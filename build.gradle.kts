
plugins {
  `java-library`
  id("io.papermc.paperweight.userdev") version "1.5.4"
  id("xyz.jpenilla.run-paper") version "2.0.1" // Adds runServer and runMojangMappedServer tasks for testing
  kotlin("jvm") version "1.8.21"
}

group = "com.github.gameoholic.echolib"
version = "0.0.1"
description = "Test"

java {
  // Configure the java toolchain. This allows gradle to auto-provision JDK 17 on systems that only have JDK 8 installed for example.
  toolchain.languageVersion.set(JavaLanguageVersion.of(17))
}

dependencies {
  paperweight.paperDevBundle("1.19.4-R0.1-SNAPSHOT") //the paper dev bundle is a compile-only dependency, paper itself provides it. No need to shade
  // paperweight.foliaDevBundle("1.19.4-R0.1-SNAPSHOT")
  // paperweight.devBundle("com.example.paperfork", "1.19.4-R0.1-SNAPSHOT")
  implementation(kotlin("stdlib-jdk8"))
  //implementation("org.jetbrains.kotlin:kotlin-stdlib:1.8.0")
}


tasks {



  // Configure reobfJar to run when invoking the build task
  assemble {
    dependsOn(reobfJar)
  }

  compileJava {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything

    // Set the release flag. This configures what version bytecode the compiler will emit, as well as what JDK APIs are usable.
    // See https://openjdk.java.net/jeps/247 for more information.
    options.release.set(17)
  }
  javadoc {
    options.encoding = Charsets.UTF_8.name() // We want UTF-8 for everything
  }
  processResources {
    filteringCharset = Charsets.UTF_8.name() // We want UTF-8 for everything
    val props = mapOf(
            "name" to project.name,
            "version" to project.version,
            "description" to project.description,
            "apiVersion" to "1.19"
    )
    inputs.properties(props)
    filesMatching("plugin.yml") {
      expand(props)
    }
  }


  reobfJar {
    outputJar.set(layout.buildDirectory.file("libs/PaperweightTestPlugin-${project.version}.jar"))
  }

}

repositories {
  mavenCentral()
}
