package com.betomorrow.gradle.wording

import com.betomorrow.gradle.wording.extensions.WORDING_EXTENSION_NAME
import com.betomorrow.gradle.wording.extensions.WordingLanguageExtension
import com.betomorrow.gradle.wording.extensions.WordingPluginExtension
import org.gradle.api.NamedDomainObjectContainer
import org.gradle.api.Plugin
import org.gradle.api.Project

class WordingPlugin : Plugin<Project> {


    override fun apply(project: Project) {

        project.extensions.create(WORDING_EXTENSION_NAME, WordingPluginExtension::class.java, project)

        project.task("downloadWording") {
            it.doLast{
                val wordingExtension = project.extensions.getByType(WordingPluginExtension::class.java)

                println("Url : ${wordingExtension.googleSheetUrl}")
                println("Sheet Name : ${wordingExtension.sheetName}")
                println("Keys : ${wordingExtension.keysColumn}")

                wordingExtension.languages.forEach {
                    println("Locale : ${it.name} file:${it.file} column:${it.column} ");
                }
            }
        }
    }



}