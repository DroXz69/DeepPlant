// Top-level build file where you can add configuration options common to all sub-projects/modules.
// build.gradle (nivel de proyecto)

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.google.services) apply false
}

// Configurar toolchains por subproyecto (cuando apliquen los plugins correspondientes)
subprojects {
    // Para proyectos Java puros
    plugins.withType<org.gradle.api.plugins.JavaPlugin> {
        extensions.configure<org.gradle.api.plugins.JavaPluginExtension> {
            toolchain {
                languageVersion.set(org.gradle.jvm.toolchain.JavaLanguageVersion.of(17))
            }
        }
    }
    // Para proyectos con Kotlin
    plugins.withType<org.jetbrains.kotlin.gradle.plugin.KotlinBasePluginWrapper> {
        extensions.configure<org.jetbrains.kotlin.gradle.dsl.KotlinProjectExtension> {
            jvmToolchain(17)
        }
    }
}
