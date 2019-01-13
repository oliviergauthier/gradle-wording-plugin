package com.betomorrow.gradle.wording.infra

import com.betomorrow.gradle.wording.infra.drive.GoogleDrive
import com.betomorrow.gradle.wording.infra.drive.DriveMimeType
import org.junit.jupiter.api.Test

class GoogleDriveIntTest {

    @Test
    fun testDownloadFiles() {
        val api = GoogleDrive()
        val path =  api.downloadFile("1UznpBPuRddr5gYPnRhNxkP-bcU0NHkhpTqi6B5V0QL0", DriveMimeType.XLSX)
        println(path)
    }

}