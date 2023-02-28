package com.betomorrow.gradle.wording.extensions

import org.gradle.api.Project
import java.io.File

open class WordingLanguageExtension(val name: String, val project: Project) {

    var output: String? = null

    lateinit var column: String

    val isDefault: Boolean
        get() {
            return name == DEFAULT_NAME
        }

    val outputFile: File
        get() {
            return when {
                output != null -> {
                    val file = project.projectDir.resolve(output!!)
                    if (file.isDirectory) {
                        file.resolve("messages.properties")
                    } else {
                        file
                    }
                }

                name == DEFAULT_NAME -> {
                    project.projectDir.resolve("src/main/resources/messages.properties")
                }

                else -> {
                    project.projectDir.resolve("src/main/resources/messages_$name.properties")
                }
            }
        }

    companion object {
        const val DEFAULT_NAME = "default"
    }
}
