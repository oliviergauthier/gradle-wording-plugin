package com.betomorrow.gradle.wording

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test

class WordingPluginIntTest {

    @Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testDownloadWording() {
        testProjectDir.create()
        val buildFile = testProjectDir.newFile("build.gradle")

        buildFile.appendText("""
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

                languages {
                    'default' {
                        output = "src/main/res/values/strings.xml"
                        column = "C"
                    }
                    'fr' {
//                        output = "src/main/res/values-fr/strings.xml"
                        column = "D"
                    }
                    'es' {
//                        output = "src/main/res/values-es/strings.xml"
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

        println(result.getOutput())

    }

}