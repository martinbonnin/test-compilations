import org.jetbrains.kotlin.cli.jvm.main
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.9.20-Beta")
}

kotlin {
    val jvmCommon = sourceSets.create("jvmCommon")
    jvmCommon.dependsOn(sourceSets.getByName("commonMain"))

    jvm {
        listOf("prod", "dev").forEach {buildType ->
            val compilation = compilations.create(buildType) {
                associateWith(compilations.getByName("main"))
            }
            compilation.defaultSourceSet.dependsOn(jvmCommon)

            val runTask = tasks.register("run$buildType", JavaExec::class.java) {
                classpath(configurations.getByName(compilation.runtimeDependencyConfigurationName))
                classpath(compilation.output.classesDirs)

                mainClass.set("com.example.HelloKt")
            }
        }
    }
}