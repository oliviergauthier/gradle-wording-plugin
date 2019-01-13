package com.betomorrow.gradle.wording.domain

import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File

class XlsxExtractor(private val path: String,
                    private val keyColumn: Column,
                    private val valuesColumn: List<Language>,
                    private val skipHeader : Boolean = true) {

    fun extract() : Wordings {
        val wordings = MutableWordings()

        val workbook = WorkbookFactory.create(File(path))

        println("Found ${workbook.numberOfSheets} sheets")

        val sheetIterator = workbook.sheetIterator()
        while(sheetIterator.hasNext()) {
            val sheet = sheetIterator.next()

            val rowIterator = sheet.rowIterator()
            if (skipHeader) {
                rowIterator.next()
            }
            while(rowIterator.hasNext()) {
                val row = rowIterator.next()
                val key = row.getCell(keyColumn.index).stringCellValue
                valuesColumn.forEach {
                    val value = row.getCell(it.index).stringCellValue
                    wordings.getOrPut(it.name).add(key, value)
                    println("language=${it.name}, key=$key, value=$value")
                }

            }
        }

        return wordings
    }

}