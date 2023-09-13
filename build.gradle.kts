import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTarget
import org.jetbrains.kotlin.gradle.plugin.mpp.KotlinNativeTargetWithHostTests
import org.jetbrains.kotlin.gradle.targets.js.ir.KotlinJsIrTarget
import org.jetbrains.kotlin.gradle.targets.jvm.KotlinJvmTarget

plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.9.20-Beta")
}

kotlin {
    jvm()
    macosArm64()

    targets.all {
        when (this) {
            is KotlinJvmTarget -> {
                withJava()
                testRuns.create("kotlinCodegen").setExecutionSourceFrom(compilations.create("kotlinCodegenTest"))
                //testRuns.create("javaCodegen").setExecutionSourceFrom(compilations.create("javaCodegenTest"))
            }
            is KotlinJsIrTarget -> {
                testRuns.create("kotlinCodegen").setExecutionSourceFrom(compilations.create("kotlinCodegenTest"))
            }
            is KotlinNativeTargetWithHostTests -> {
                val compilation = compilations.create("kotlinCodegenTest")
                binaries.test("kotlinCodegen") {
                    this.compilation = compilation
                }
                testRuns.create("kotlinCodegen").setExecutionSourceFrom(binaries.getTest("kotlinCodegen", DEBUG))
            }
        }
    }

    sourceSets {
        getByName("commonMain") {
            dependencies {
                implementation(kotlin("test"))
            }
            kotlin.srcDir("kotlinCodegen")
        }
        getByName("jvmMain") {
            dependencies {
                implementation("junit:junit:4.13.2")
            }
        }
    }

//    sourceSets {
//        if (System.getProperty("idea.sync.active") != null) {
//            /**
//             * We are in the IDE, create a "fake" sharedTest sourceSet
//             * And use the kotlinCodegen
//             */
//            val sharedTest = create("sharedTest") {
//                kotlin.srcDir("kotlinCodegen")
//
//                dependencies {
//                    implementation(kotlin(("test")))
//                }
//            }
//            targets.all {
//              this.compilations.findByName("test")?.defaultSourceSet?.dependsOn(sharedTest)
//            }
//        } else {
//            /**
//             * Tests using the kotlin codegen
//             */
//            getByName("commonTest") {
//                dependencies {
//                    implementation(kotlin(("test")))
//                }
//                kotlin.apply {
//                    srcDir("sharedTest")
//                    srcDir("kotlinCodegen")
//                }
//            }
//
//            /**
//             * Same tests but using the javaCodegen under the hood
//             */
//            getByName("jvmJavaCodegenTest") {
//                dependencies {
//                    implementation(kotlin(("test")))
//                }
//                kotlin.apply {
//                    srcDir("sharedTest")
//                }
//            }
//            java.sourceSets.getByName("javaCodegenTest").java.srcDir("javaCodegen")
//        }
//    }
}