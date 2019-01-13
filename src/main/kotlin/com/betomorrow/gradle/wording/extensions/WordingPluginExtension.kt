package com.betomorrow.gradle.wording.extensions

import org.gradle.api.Action
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Project

const val WORDING_EXTENSION_NAME = "wording"

open class WordingPluginExtension(val project: Project) {

    var googleSheetUrl : String? = null
    var sheetName : String ? = null
    var keysColumn : String ? = "A"

    var languages : NamedDomainObjectContainer<WordingLanguageExtension> = project.container(WordingLanguageExtension::class.java)

    fun languages(action: Action<NamedDomainObjectContainer<WordingLanguageExtension>>) {
        action.execute(languages)
    }
}