plugins {
    id("org.jetbrains.kotlin.multiplatform").version("1.9.20-Beta")
}

kotlin {
    jvm {
        withJava()
        val javaCodegenTestCompilation = compilations.create("javaCodegenTest")

        testRuns.create("javaCodegen").setExecutionSourceFrom(javaCodegenTestCompilation)
    }
    macosArm64()

    sourceSets {
        if (System.getProperty("idea.sync.active") != null) {
            /**
             * We are in the IDE, create a "fake" sharedTest sourceSet
             * And use the kotlinCodegen
             */
            val sharedTest = create("sharedTest") {
                kotlin.srcDir("src/kotlinCodegen")

                dependencies {
                    implementation(kotlin(("test")))
                }
            }
            targets.all {
              this.compilations.findByName("test")?.defaultSourceSet?.dependsOn(sharedTest)
            }
        } else {
            /**
             * Tests using the kotlin codegen
             */
            getByName("commonTest") {
                dependencies {
                    implementation(kotlin(("test")))
                }
                kotlin.apply {
                    srcDir("src/sharedTest")
                    srcDir("src/kotlinCodegen")
                }
            }

            /**
             * Same tests but using the javaCodegen under the hood
             */
            getByName("jvmJavaCodegenTest") {
                dependencies {
                    implementation(kotlin(("test")))
                }
                kotlin.apply {
                    srcDir("src/sharedTest")
                }
            }
            java.sourceSets.getByName("javaCodegenTest").java.srcDir("src/javaCodegen")
        }
    }
}