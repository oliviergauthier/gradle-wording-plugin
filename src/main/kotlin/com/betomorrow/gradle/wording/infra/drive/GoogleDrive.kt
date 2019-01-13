package com.betomorrow.gradle.wording.infra.drive

import com.google.api.client.auth.oauth2.Credential
import com.google.api.client.json.jackson2.JacksonFactory
import com.google.api.services.drive.DriveScopes
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport
import com.google.api.client.http.javanet.NetHttpTransport
import com.google.api.client.util.store.FileDataStoreFactory
import java.io.File
import java.io.InputStreamReader
import java.io.IOException
import com.google.api.services.drive.Drive
import java.io.FileOutputStream

class GoogleDrive(private val credentialPath: String = "/credentials.json") {

    @Throws(IOException::class)
    private fun getCredentials(httpTransport: NetHttpTransport): Credential {
        // Load client secrets.
        val credentialInputStream = GoogleDrive::class.java.getResourceAsStream(
            CREDENTIALS_FILE_PATH
        )
        val clientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(credentialInputStream))

        // Build flow and trigger user authorization request.
        val flow = GoogleAuthorizationCodeFlow
            .Builder(httpTransport,
                jsonFactory, clientSecrets,
                SCOPES
            )
            .setDataStoreFactory(FileDataStoreFactory(File(TOKENS_DIRECTORY_PATH)))
            .setAccessType("offline")
            .build()

        val receiver = LocalServerReceiver
            .Builder()
            .setPort(8888)
            .build()

        return AuthorizationCodeInstalledApp(flow, receiver)
            .authorize("user")
    }

    fun downloadFile(fileId : String, mimeType: DriveMimeType) : String {
        return downloadFile(fileId, mimeType.value)
    }

    fun downloadFile(fileId : String, mimeType: String? = null) : String {
        val httpTransport = GoogleNetHttpTransport.newTrustedTransport()

        val service = Drive
            .Builder(httpTransport,
                jsonFactory, getCredentials(httpTransport))
            .setApplicationName(APPLICATION_NAME)
            .build()

        val export = if (mimeType != null) {
            service.files().export(fileId, mimeType)
        } else {
            service.files().get(fileId)
        }

        val outFilePath = "build/wording/$fileId.xlsx"
        val out = FileOutputStream(outFilePath)
        export.executeAndDownloadTo(out)
        out.flush()
        return outFilePath
    }

    companion object {
        private const val APPLICATION_NAME = "Gradle Android Wording Plugin"

        private const val TOKENS_DIRECTORY_PATH = "build/wording/tokens"
        private const val CREDENTIALS_FILE_PATH = "/credentials.json"

        private val SCOPES = listOf(DriveScopes.DRIVE)
        private val jsonFactory = JacksonFactory.getDefaultInstance()
    }
}