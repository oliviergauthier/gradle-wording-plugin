package com.betomorrow.gradle.wording.extensions

import com.betomorrow.gradle.wording.domain.OutputFormat
import org.gradle.api.Project
import java.io.File

open class WordingLanguageExtension(val name: String, val project: Project) {

    var output: String? = null

    lateinit var column: String

    val isDefault: Boolean
        get() {
            return name == DEFAULT_NAME
        }

    fun getOutputFile(format: OutputFormat) : File {
        return when (format) {
            OutputFormat.ANDROID -> getAndroidOutputFile()
            else -> getSpringOutputFile()
        }
    }

    private fun getAndroidOutputFile(): File {
        return when {
            output != null -> {
                val file = project.projectDir.resolve(output!!)
                if (file.isDirectory) {
                    file.resolve("strings.xml")
                } else {
                    file
                }
            }

            name == DEFAULT_NAME -> {
                project.projectDir.resolve("src/main/res/values/strings.xml")
            }

            else -> {
                project.projectDir.resolve("src/main/res/values-$name/strings.xml")
            }

        }
    }

    private fun getSpringOutputFile(): File {
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
