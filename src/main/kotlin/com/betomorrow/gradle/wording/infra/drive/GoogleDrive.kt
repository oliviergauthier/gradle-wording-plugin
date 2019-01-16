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
import com.google.api.services.drive.Drive
import com.google.common.io.Files
import java.io.*

class GoogleDrive() {

    private var credentials : File? = null
    private var clientId: String? = null
    private var clientSecret : String? = null

    constructor(clientId: String?, clientSecret: String?) : this() {
        this.clientId = clientId
        this.clientSecret = clientSecret
    }

    constructor(credentials: File?) : this() {
        this.credentials = credentials
    }

    private fun getAuthorizationCodeFlowBuilder(httpTransport: NetHttpTransport) : GoogleAuthorizationCodeFlow.Builder {
        if (clientId != null && clientSecret != null) {
            return GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientId, clientSecret, SCOPES)
        }
        val credentialStream = if (credentials != null) {
            FileInputStream(credentials)
        } else {
            GoogleDrive::class.java.getResourceAsStream(CREDENTIALS_FILE_PATH)
        }

        val clientSecrets = GoogleClientSecrets.load(jsonFactory, InputStreamReader(credentialStream))

        return GoogleAuthorizationCodeFlow.Builder(httpTransport, jsonFactory, clientSecrets, SCOPES)
    }

    @Throws(IOException::class)
    private fun getCredentials(httpTransport: NetHttpTransport): Credential {
        val flow = getAuthorizationCodeFlowBuilder(httpTransport)
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

    fun downloadFile(fileId : String, mimeType: DriveMimeType, output: File) {
        return downloadFile(fileId, mimeType.value, output)
    }

    fun downloadFile(fileId : String, mimeType: String? = null, output: File) {
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

        Files.createParentDirs(output)
        val out = FileOutputStream(output)
        export.executeAndDownloadTo(out)
        out.flush()
    }

    companion object {
        private const val APPLICATION_NAME = "Gradle Android Wording Plugin"

        private const val TOKENS_DIRECTORY_PATH = "build/wording/tokens"
        private const val CREDENTIALS_FILE_PATH = "/credentials.json"

        private val SCOPES = listOf(DriveScopes.DRIVE)
        private val jsonFactory = JacksonFactory.getDefaultInstance()
    }
}