package com.betomorrow.gradle.wording

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test
import java.io.File

/**
 * Before running integration tests you should :
 * - update samples "build.gradle" files to match your plugin version
 * - run `./gradlew publishToMavenLocal` each time you update code
 */
class WordingPluginIntTest {

    @Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testApplyPluginOnAndroidConfig() {
        val result = GradleRunner.create()
            .withProjectDir(File("src/integration-test/resources/sample-android"))
            .withArguments(
                "updateWordingFr",
                "--stacktrace"
            )
            .withPluginClasspath()
            .withDebug(true)
            .build()

        println(result.output)
    }

    @Test
    fun testApplyPluginOnSpringConfig() {
        val result = GradleRunner.create()
            .withProjectDir(File("src/integration-test/resources/sample-spring"))
            .withArguments(
                "updateWordingFr",
                "--stacktrace"
            )
            .withPluginClasspath()
            .withDebug(true)
            .build()

        println(result.output)
    }

    @Test
    fun testDownloadWording() {
        testProjectDir.create()
        val buildFile = testProjectDir.newFile("build.gradle")

        buildFile.appendText(
            """
            plugins {
                id 'com.betomorrow.gradle.wording'
            }

            wording {
                credentials = "~/.credentials.json"
                clientId = ""
                clientSecret = ""

                sheetId = "qwertyuiop"
                sheetNames = ["commons", "app"]
                filename = "wording.xlsx"
                skipHeaders = true
                keysColumn = "A"
                outputFormat = "spring"

                languages {
                    'default' {
                        column = "C"
                    }
                    'fr' {
                        column = "D"
                    }
                    'es' {
                        column = "E"
                    }
                }
            }
            """.trimIndent()
        )

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("tasks", "--stacktrace", "--all")
            .withPluginClasspath()
            .withDebug(true)
            .build()

        println(result.output)
    }
}
