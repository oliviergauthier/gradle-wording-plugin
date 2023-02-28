package com.betomorrow.gradle.wording.extensions

import com.betomorrow.gradle.wording.domain.OutputFormat
import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project
import java.io.File

const val WORDING_EXTENSION_NAME = "wording"

open class WordingPluginExtension(val project: Project) {

    var credentials: String? = null
    var clientId: String? = null
    var clientSecret: String? = null

    lateinit var sheetId: String
    var sheetNames: List<String> = emptyList()

    var skipHeaders: Boolean = true

    var filename: String = "wording.xlsx"

    var keysColumn: String = "A"

    var addMissingKeys: Boolean = false

    var outputFormat: OutputFormat = OutputFormat.ANDROID

    var languages: NamedDomainObjectContainer<WordingLanguageExtension> = project.container(WordingLanguageExtension::class.java) {
        WordingLanguageExtension(it, project)
    }

    fun languages(action: Action<NamedDomainObjectContainer<WordingLanguageExtension>>) {
        action.execute(languages)
    }

    val wordingFile: File
        get() {
            return project.rootDir.resolve(filename)
        }
}
