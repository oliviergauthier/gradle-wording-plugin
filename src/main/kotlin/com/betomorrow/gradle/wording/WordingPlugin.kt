package com.betomorrow.gradle.wording

import com.betomorrow.gradle.wording.extensions.WORDING_EXTENSION_NAME
import com.betomorrow.gradle.wording.extensions.WordingLanguageExtension
import com.betomorrow.gradle.wording.extensions.WordingPluginExtension
import com.betomorrow.gradle.wording.tasks.DownloadWordingTask
import com.betomorrow.gradle.wording.tasks.UpdateWordingTask
import org.gradle.api.Plugin
import org.gradle.api.Project

class WordingPlugin : Plugin<Project> {

    override fun apply(project: Project) {

        with(project) {
            extensions.create(WORDING_EXTENSION_NAME, WordingPluginExtension::class.java, project)

            afterEvaluate { p ->

                val wordingExtension = extensions.getByType(WordingPluginExtension::class.java)

                val downloadWordingTask = tasks.register("downloadWording", DownloadWordingTask::class.java) { t ->
                    t.group = GROUP
                    t.description =
                        "Download translations to ${wordingExtension.wordingFile.relativeTo(project.projectDir)}"
                    t.credentials = wordingExtension.credentials?.let { project.rootDir.resolve(it) }
                    t.clientId = wordingExtension.clientId
                    t.clientSecret = wordingExtension.clientSecret

                    t.fileId = wordingExtension.sheetId
                    t.output = wordingExtension.wordingFile
                    t.outputs.upToDateWhen { false }
                }.get()

                val updateWordingTask = tasks.register("updateWording") { t ->
                    t.group = GROUP
                    t.description = "Update all wording files"
                }.get()
                updateWordingTask.mustRunAfter(downloadWordingTask)

                wordingExtension.languages.forEach { language ->
                    val outputFile = language.getOutputFile(wordingExtension.outputFormat)
                    val task = p.tasks.register(
                        "updateWording${language.name.replaceFirstChar { if (it.isLowerCase()) it.titlecase() else it.toString() }}",
                        UpdateWordingTask::class.java
                    ) { t ->
                        t.group = GROUP
                        t.description = "Update wording file ${outputFile.relativeTo(project.projectDir)}"
                        t.skipHeaders = wordingExtension.skipHeaders
                        t.source = wordingExtension.wordingFile
                        t.output = outputFile
                        t.outputFormat = wordingExtension.outputFormat
                        t.keysColumn = wordingExtension.keysColumn
                        t.column = language.column
                        t.sheetNames = wordingExtension.sheetNames
                        t.failOnMissingKeys = language.name == WordingLanguageExtension.DEFAULT_NAME
                        t.addMissingKeys = wordingExtension.addMissingKeys
                    }
                    task.get().mustRunAfter(downloadWordingTask)
                    updateWordingTask.dependsOn(task)
                }

                tasks.register("upgradeWording") { t ->
                    t.group = GROUP
                    t.description = "Download and update all wording files"
                    t.dependsOn(downloadWordingTask, updateWordingTask)
                }
            }
        }
    }

    companion object {
        const val GROUP = "Wording"
    }
}
