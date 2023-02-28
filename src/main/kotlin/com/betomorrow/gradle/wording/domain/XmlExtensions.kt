package com.betomorrow.gradle.wording.domain

import java.io.File
import java.io.FileReader
import java.io.FileWriter
import java.nio.charset.Charset
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import java.util.Properties

fun loadOrCreateProperties(path: Path): Properties {
    val prop = Properties()

    if (Files.isRegularFile(path)) {
        val reader = FileReader(path.toFile())
        prop.load(reader)
    }

    return prop
}

fun writeToFile(properties: Properties, path: String) {
    properties.store(FileWriter(File(path)), null)
}
