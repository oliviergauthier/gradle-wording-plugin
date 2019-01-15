package com.betomorrow.gradle.wording.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.io.File

const val WORDING_EXTENSION_NAME = "wording"

open class WordingPluginExtension(val project: Project) {

    lateinit var sheetId : String

    var skipHeaders : Boolean = true

    var filename: String = "wording.xlsx"

    var keysColumn : String = "A"

    var languages : NamedDomainObjectContainer<WordingLanguageExtension> = project.container(WordingLanguageExtension::class.java) {
        WordingLanguageExtension(it, project)
    }

    fun languages(action: Action<NamedDomainObjectContainer<WordingLanguageExtension>>) {
        action.execute(languages)
    }

    val wordingFile : File
     get() {
         return project.rootDir.resolve(filename)
     }
}