package com.betomorrow.gradle.wording.tasks

import com.betomorrow.gradle.wording.domain.Column
import com.betomorrow.gradle.wording.domain.XlsxExtractor
import com.betomorrow.gradle.wording.domain.XmlUpdater
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.lang.Exception

open class UpdateWordingTask : DefaultTask() {

    @InputFile
    lateinit var source: File

    @OutputFile
    lateinit var output: File

    var skipHeaders: Boolean = true
    lateinit var keysColumn: String
    lateinit var column: String

    var sheetNames = emptyList<String>()
    var failOnMissingKeys = false
    var addMissingKeys = false

    @TaskAction
    fun update() {
        val extractor = XlsxExtractor(source.absolutePath, Column(keysColumn), skipHeaders)
        val updater = XmlUpdater(output.absolutePath)
        val wordings = extractor.extract(Column(column), sheetNames)
        val updatedKeys = updater.update(wordings, addMissingKeys)

        val missingKeys = wordings.keys - updatedKeys
        if (missingKeys.isNotEmpty() && failOnMissingKeys) {
            throw MissingKeyException(missingKeys, output.relativeTo(project.rootDir))
        }
    }

}

class MissingKeyException(keys: Set<String>, file: File) :
    Exception("Missing Keys [${keys.joinToString(", ")}] in file $file")