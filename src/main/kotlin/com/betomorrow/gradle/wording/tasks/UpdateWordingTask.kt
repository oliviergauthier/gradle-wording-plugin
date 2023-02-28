package com.betomorrow.gradle.wording.tasks

import com.betomorrow.gradle.wording.domain.OutputFormat
import com.betomorrow.gradle.wording.domain.updater.WordingUpdaterFactory
import com.betomorrow.gradle.wording.domain.xlsx.Column
import com.betomorrow.gradle.wording.domain.xlsx.XlsxExtractor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.lang.Exception
import java.nio.file.Paths

open class UpdateWordingTask : DefaultTask() {

    @InputFile
    lateinit var source: File

    @OutputFile
    lateinit var output: File

    @Input
    var skipHeaders: Boolean = true

    @Internal
    lateinit var keysColumn: String

    @Internal
    lateinit var column: String

    @Input
    var sheetNames = emptyList<String>()

    @Input
    var failOnMissingKeys = false

    @Input
    var addMissingKeys = false

    @Input
    var outputFormat = OutputFormat.XML

    @TaskAction
    fun update() {
        val extractor = XlsxExtractor(source.absolutePath, Column(keysColumn), skipHeaders)
        val updater = WordingUpdaterFactory().build(outputFormat, Paths.get(output.absolutePath))

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
