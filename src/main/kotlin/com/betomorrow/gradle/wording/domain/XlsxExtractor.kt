package com.betomorrow.gradle.wording.domain

import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.File

class XlsxExtractor(private val path: String,
                    private val keysColumn: Column,
                    private val skipHeader : Boolean = true) {

    fun extract(valuesColumn: Column) : Map<String, String> {
        val result = HashMap<String, String>()

        val workbook = WorkbookFactory.create(File(path))

        val sheetIterator = workbook.sheetIterator()
        while(sheetIterator.hasNext()) {
            val sheet = sheetIterator.next()

            val rowIterator = sheet.rowIterator()
            if (skipHeader) {
                rowIterator.next()
            }
            while(rowIterator.hasNext()) {
                val row = rowIterator.next()
                val key = row.getCell(keysColumn.index).stringCellValue
                val value = row.getCell(valuesColumn.index).stringCellValue
                result.put(key, value)
            }
        }

        return result
    }

}