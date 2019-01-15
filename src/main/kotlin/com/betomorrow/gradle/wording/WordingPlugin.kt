package com.betomorrow.gradle.wording

import com.betomorrow.gradle.wording.extensions.WORDING_EXTENSION_NAME
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
                    t.fileId = wordingExtension.sheetId
                    t.output = wordingExtension.wordingFile
                }.get()

                val updateWordingTask = tasks.register("updateWording") { t ->
                    t.group = GROUP
                    t.description = "Update all wording files"
                }.get()
                updateWordingTask.mustRunAfter(downloadWordingTask)

                wordingExtension.languages.forEach { language ->
                    val task = p.tasks.register(
                        "updateWording${language.name.capitalize()}",
                        UpdateWordingTask::class.java
                    ) { t ->
                        t.group = GROUP
                        t.description = "Update wording file ${language.outputFile.relativeTo(project.projectDir)}"
                        t.skipHeaders = wordingExtension.skipHeaders
                        t.source = wordingExtension.wordingFile
                        t.output = language.outputFile
                        t.keysColumn = wordingExtension.keysColumn
                        t.column = language.column
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
