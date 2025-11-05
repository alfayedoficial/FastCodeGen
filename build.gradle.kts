import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
   id("java")
   id("org.jetbrains.kotlin.jvm") version "2.1.0"
   id("org.jetbrains.intellij.platform") version "2.5.0"
}

group = "com.alfayedoficial"
version = "1.0.7"

repositories {
   mavenCentral()
   intellijPlatform {
      defaultRepositories()
   }
}

dependencies {
   intellijPlatform {
      create("IC", "2024.2")
      bundledPlugin("org.jetbrains.kotlin")
      testFramework(org.jetbrains.intellij.platform.gradle.TestFrameworkType.Platform)
   }
}

intellijPlatform {
   pluginConfiguration {
      ideaVersion {
         sinceBuild = "242"
         untilBuild = "252.*"
      }

      changeNotes = """
      Version 1.0.0:
      - Initial release
      - ViewModel State generation with configurable options
      - Repository generation with custom methods
      - Full Feature generation (ViewModel + Repository)
      - K2 Compiler Mode Support
    """.trimIndent()
   }
}

tasks {
   // Set the JVM compatibility versions
   withType<JavaCompile> {
      sourceCompatibility = "21"
      targetCompatibility = "21"
   }
   withType<org.jetbrains.kotlin.gradle.tasks.KotlinCompile> {
      compilerOptions.jvmTarget.set(JvmTarget.JVM_21)
   }
}