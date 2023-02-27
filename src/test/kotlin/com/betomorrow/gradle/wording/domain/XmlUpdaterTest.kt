package com.betomorrow.gradle.wording.domain

import org.assertj.core.api.Assertions.assertThat
import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class XmlUpdaterTest {

    @Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testUpdateExistingWording() {

        val source = "src/test/resources/strings.xml"
        val expected = "src/test/resources/strings-expected.xml"

        testProjectDir.create()
        val copy = Paths.get(testProjectDir.root.absolutePath, "testUpdateExistingWording.xml")
        Files.copy(Paths.get(source), copy, StandardCopyOption.REPLACE_EXISTING)

        val updater = XmlUpdater(copy.toString())

        updater.update(
            HashMap<String, String>().apply {
                put("key1", "another value 1")
                put("key2", "another value 2")
                put("key3", "another value 3")
                put("key3", "another value 3")
                put("sections[0]", "Section de recherche")
                put("sections[1]", "Section retrouvée")
            },
            false
        )

        assertThat(copy).hasSameContentAs(Paths.get(expected))
    }

    @Test
    fun testCreateWording() {
        val expected = "src/test/resources/new-strings-expected.xml"

        testProjectDir.create()
        val dest = Paths.get(testProjectDir.root.absolutePath, "testCreateWording.xml")

        val updater = XmlUpdater(dest.toString())

        updater.update(
            HashMap<String, String>().apply {
                put("key1", "another value 1")
                put("key2", "another value 2")
                put("key3", "another value 3")
                put("key3", "another value 3")
                put("sections[0]", "Section de recherche")
                put("sections[1]", "Section retrouvée")
            },
            true
        )

        assertThat(dest).hasSameContentAs(Paths.get(expected))
    }

    @Test
    fun testPartialUpdateWording() {
        val source = "src/test/resources/partial-strings.xml"
        val expected = "src/test/resources/partial-strings-expected.xml"

        testProjectDir.create()
        val copy = Paths.get(testProjectDir.root.absolutePath, "testPartialUpdateWording.xml")
        Files.copy(Paths.get(source), copy, StandardCopyOption.REPLACE_EXISTING)

        val updater = XmlUpdater(copy.toString())

        updater.update(
            HashMap<String, String>().apply {
                put("key1", "another value 1")
                put("key2", "another value 2")
                put("key3", "another value 3")
                put("key3", "another value 3")
                put("sections[0]", "Section de recherche")
                put("sections[1]", "Section retrouvée")
            },
            true
        )

        assertThat(copy).hasSameContentAs(Paths.get(expected))
    }
}
