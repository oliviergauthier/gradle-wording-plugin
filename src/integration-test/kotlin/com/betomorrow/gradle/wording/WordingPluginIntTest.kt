package com.betomorrow.gradle.wording

import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.gradle.testkit.runner.GradleRunner
import org.junit.jupiter.api.Test

class WordingPluginIntTest {

    @Rule val testProjectDir = TemporaryFolder()

    @Test
    fun testDownloadWording() {
        testProjectDir.create()
        val buildFile = testProjectDir.newFile("build.gradle")

        buildFile.appendText("""
plugins {
    id 'com.betomorrow.gradle.wording'
}

wording {
    googleSheetUrl = "http://www.google.fr"
    sheetName = "My App"

    languages {
        fr {
            file = "values-fr/strings.xml"
            column = "B"
        }
        en {
            file = "values/strings.xml"
            column = "c"
        }
    }
}
""".trimIndent())

        val result = GradleRunner.create()
            .withProjectDir(testProjectDir.root)
            .withArguments("downloadWording", "--stacktrace")
            .withPluginClasspath()
            .withDebug(true)
            .build()

        println(result.getOutput())

    }

}