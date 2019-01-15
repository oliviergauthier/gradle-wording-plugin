package com.betomorrow.gradle.wording.tasks

import com.betomorrow.gradle.wording.domain.Column
import com.betomorrow.gradle.wording.domain.XlsxExtractor
import com.betomorrow.gradle.wording.domain.XmlUpdater
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class UpdateWordingTask : DefaultTask() {

    @InputFile
    lateinit var source: File

    @OutputFile
    lateinit var output: File

    var skipHeaders : Boolean = true
    lateinit var keysColumn : String
    lateinit var column: String

    @TaskAction
    fun update() {
        val extractor = XlsxExtractor(source.absolutePath, Column(keysColumn), skipHeaders)
        val updater = XmlUpdater(output.absolutePath)
        updater.update(extractor.extract(Column(column)))
    }

}