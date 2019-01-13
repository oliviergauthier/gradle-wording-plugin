package com.betomorrow.gradle.wording.tasks

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

class DownloadWordingTask : DefaultTask() {

    var url : String? = null

    var outDir = project.buildDir

    @TaskAction
    fun download() {

    }


}