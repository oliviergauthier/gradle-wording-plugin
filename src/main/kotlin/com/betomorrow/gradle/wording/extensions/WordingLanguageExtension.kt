package com.betomorrow.gradle.wording.extensions

import org.gradle.api.Project
import java.io.File

open class WordingLanguageExtension(val name: String, val project: Project) {

    var output: String? = null

    lateinit var column: String

    val outputFile: File
        get() {
            return if (output != null) {
                val file = project.projectDir.resolve(output!!)
                if (file.isDirectory) {
                    file.resolve("strings.xml")
                } else {
                    file
                }
            } else {
                project.projectDir.resolve("src/main/res/values-$name/strings.xml")
            }
        }

}