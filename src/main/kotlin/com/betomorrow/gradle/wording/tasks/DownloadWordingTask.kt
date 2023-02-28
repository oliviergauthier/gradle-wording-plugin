package com.betomorrow.gradle.wording.tasks

import com.betomorrow.gradle.wording.infra.drive.DriveMimeType
import com.betomorrow.gradle.wording.infra.drive.GoogleDrive
import org.gradle.api.DefaultTask
import org.gradle.api.tasks.Input
import org.gradle.api.tasks.InputFile
import org.gradle.api.tasks.Internal
import org.gradle.api.tasks.Optional
import org.gradle.api.tasks.OutputFile
import org.gradle.api.tasks.TaskAction
import java.io.File

open class DownloadWordingTask : DefaultTask() {

    @Optional
    @Input
    var clientId: String? = null

    @Optional
    @Input
    var clientSecret: String? = null

    @Optional
    @InputFile
    var credentials: File? = null

    @Internal
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

    val tokenDirectory: String
        @Internal
        get() {
            return project.projectDir
                .resolve(".gradle")
                .resolve("wording-plugin")
                .resolve("tokens")
                .toString()
        }
}
