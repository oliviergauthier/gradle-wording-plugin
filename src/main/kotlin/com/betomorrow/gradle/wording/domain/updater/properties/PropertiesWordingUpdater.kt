package com.betomorrow.gradle.wording.domain.updater.properties

import com.betomorrow.gradle.wording.domain.updater.WordingUpdater
import java.io.FileReader
import java.io.FileWriter
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties

class PropertiesWordingUpdater(private val path: Path) : WordingUpdater {

    /**
     * Update file with wording and returns updated keys
     */
    override fun update(wording: Map<String, String>, addMissingWording: Boolean): Set<String> {

        val outputKeys = HashSet<String>()

        val properties = loadOrCreateProperties(path)

        updateStrings(properties, wording, outputKeys)

        val missingWordings = wording.keys - outputKeys
        if (addMissingWording) {
            addMissingStrings(
                properties,
                missingWordings,
                wording,
            )

            outputKeys.addAll(wording.keys)
        }

        writeToFile(properties, path)

        return outputKeys
    }

    private fun updateStrings(properties: Properties, wording: Map<String, String>, outputKeys: HashSet<String>) {
        properties.stringPropertyNames().forEach { key ->
            if (wording.containsKey(key)) {
                properties.setProperty(key, wording[key])
                outputKeys.add(key)
            }
        }
    }

    private fun addMissingStrings(properties: Properties, keys: Set<String>, wording: Map<String, String>) {
        keys.forEach { key ->
            properties.setProperty(key, wording[key])
        }
    }
}

fun loadOrCreateProperties(path: Path): Properties {
    val prop = Properties()

    if (Files.isRegularFile(path)) {
        val reader = FileReader(path.toFile())
        prop.load(reader)
    }

    return prop
}

fun writeToFile(properties: Properties, path: Path) {
    properties.store(FileWriter(path.toFile()), null)
}
