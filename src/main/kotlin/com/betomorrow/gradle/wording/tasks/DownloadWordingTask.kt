package com.betomorrow.gradle.wording.tasks

import com.betomorrow.gradle.wording.infra.drive.DriveMimeType
import com.betomorrow.gradle.wording.infra.drive.GoogleDrive
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.*
import java.io.File
import java.nio.file.Paths

open class DownloadWordingTask : DefaultTask() {

    @Input
    @Optional
    var clientId: String? = null

    @Input
    @Optional
    var clientSecret: String? = null

    @InputFile
    @Optional
    var credentials: File? = null

    @Input
    lateinit var fileId: String

    @OutputFile
    lateinit var output: File

    @TaskAction
    fun download() {
        val googleDrive = when {
            clientId != null && clientSecret != null -> GoogleDrive(clientId, clientSecret, tokenDirectory)
            credentials != null -> GoogleDrive(credentials, tokenDirectory)
            else -> GoogleDrive(tokenDirectory)
        }

        logger.info("download $fileId to $output")
        googleDrive.downloadFile(fileId, DriveMimeType.XLSX, output)
    }

    @get:Internal
    val tokenDirectory: String
        get() {
            return project.projectDir
                .resolve(".gradle")
                .resolve("wording-plugin")
                .resolve("tokens")
                .toString()
        }


}