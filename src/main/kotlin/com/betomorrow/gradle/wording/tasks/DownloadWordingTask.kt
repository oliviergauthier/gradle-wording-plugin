package com.betomorrow.gradle.wording.tasks

import com.betomorrow.gradle.wording.infra.drive.DriveMimeType
import com.betomorrow.gradle.wording.infra.drive.GoogleDrive
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class DownloadWordingTask : DefaultTask() {

    @Optional
    var clientId : String? = null

    @Optional
    var clientSecret : String? = null

    @Optional
    var credentials : File? = null

    lateinit var fileId : String

    @OutputFile
    lateinit var output : File

    @TaskAction
    fun download() {
        val googleDrive = when {
            clientId != null && clientSecret != null -> GoogleDrive(clientId, clientSecret)
            credentials != null -> GoogleDrive(credentials)
            else -> GoogleDrive()
        }

        logger.info("download $fileId to $output")
        googleDrive.downloadFile(fileId, DriveMimeType.XLSX, output)
    }

}