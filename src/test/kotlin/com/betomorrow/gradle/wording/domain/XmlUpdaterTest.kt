package com.betomorrow.gradle.wording.domain

import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths

class XmlUpdaterTest {

    @Test
    fun testUpdater() {
        val lines = Files.readAllLines(Paths.get("src/test/resources/strings.xml"))
        XmlUpdater().update("src/test/resources/strings.xml", MutableWording("fr"))
    }

}