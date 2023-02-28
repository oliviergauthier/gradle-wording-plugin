package com.betomorrow.gradle.wording.tasks

import com.betomorrow.gradle.wording.domain.Column
import com.betomorrow.gradle.wording.domain.PropertiesUpdater
import com.betomorrow.gradle.wording.domain.WordingCleaner
import com.betomorrow.gradle.wording.domain.XlsxExtractor
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File
import java.lang.Exception

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

    @TaskAction
    fun update() {
        val extractor = XlsxExtractor(source.absolutePath, Column(keysColumn), skipHeaders)
        val updater = PropertiesUpdater(output.absolutePath)
        val cleaner = WordingCleaner()

        val wordings = extractor.extract(Column(column), sheetNames)
        val cleanedWordings = cleaner.clean(wordings)
        val updatedKeys = updater.update(cleanedWordings, addMissingKeys)

        val missingKeys = wordings.keys - updatedKeys
        if (missingKeys.isNotEmpty() && failOnMissingKeys) {
            throw MissingKeyException(missingKeys, output.relativeTo(project.rootDir))
        }
    }
}

class MissingKeyException(keys: Set<String>, file: File) :
    Exception("Missing Keys [${keys.joinToString(", ")}] in file $file")
