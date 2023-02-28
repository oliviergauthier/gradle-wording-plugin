package com.betomorrow.gradle.wording.domain

import com.betomorrow.gradle.wording.domain.updater.PropertiesWordingUpdater
import com.betomorrow.gradle.wording.domain.updater.loadOrCreateProperties
import org.assertj.core.api.Assertions.assertThat
import org.gradle.internal.impldep.org.junit.Rule
import org.gradle.internal.impldep.org.junit.rules.TemporaryFolder
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.nio.file.StandardCopyOption

class PropertiesWordingUpdaterTest {

    @Rule
    val testProjectDir = TemporaryFolder()

    @Test
    fun testUpdateExistingWording() {
        val source = "src/test/resources/update-messages.properties"
        val expected = "src/test/resources/update-messages-expected.properties"

        testProjectDir.create()
        val copy = Paths.get(testProjectDir.root.absolutePath, "testUpdateExistingWording.properties")
        Files.copy(Paths.get(source), copy, StandardCopyOption.REPLACE_EXISTING)

        val updater = PropertiesWordingUpdater(copy.toString())

        updater.update(
            HashMap<String, String>().apply {
                put("key1", "another value 1")
                put("key2", "another value 2")
                put("key3", "another value 3")
                put("key3", "another value 3")
                put("sections", "Section de recherche, Section retrouvée")
            },
            false
        )

        assertHaveSameProperties(copy, Paths.get(expected))
    }

    @Test
    fun testCreateWording() {
        val expected = "src/test/resources/new-messages-expected.properties"

        testProjectDir.create()
        val dest = Paths.get(testProjectDir.root.absolutePath, "testCreateWording.properties")

        val updater = PropertiesWordingUpdater(dest.toString())

        updater.update(
            HashMap<String, String>().apply {
                put("key1", "another value 1")
                put("key2", "another value 2")
                put("key3", "another value 3")
                put("key3", "another value 3")
                put("sections", "Section de recherche, Section retrouvée")
            },
            true
        )

        assertHaveSameProperties(dest, Paths.get(expected))
    }

    @Test
    fun testPartialUpdateWording() {
        val source = "src/test/resources/partial-messages.properties"
        val expected = "src/test/resources/partial-messages-expected.properties"

        testProjectDir.create()
        val copy = Paths.get(testProjectDir.root.absolutePath, "testPartialUpdateWording.properties")
        Files.copy(Paths.get(source), copy, StandardCopyOption.REPLACE_EXISTING)

        val updater = PropertiesWordingUpdater(copy.toString())

        updater.update(
            HashMap<String, String>().apply {
                put("key1", "another value 1")
                put("key2", "another value 2")
                put("key3", "another value 3")
                put("key3", "another value 3")
                put("sections", "Section de recherche, Section retrouvée")
            },
            true
        )

        assertHaveSameProperties(copy, Paths.get(expected))
    }
}

private fun assertHaveSameProperties(source: Path, expected: Path) {
    val sourceProp = loadOrCreateProperties(source)
    val expectedProp = loadOrCreateProperties(expected)
    assertThat(sourceProp).isEqualTo(expectedProp)
}
