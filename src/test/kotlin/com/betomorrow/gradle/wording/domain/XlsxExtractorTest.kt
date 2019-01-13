package com.betomorrow.gradle.wording.domain

import org.junit.jupiter.api.Test

class XlsxExtractorTest {

    @Test
    fun testParseTestFile() {
        val extractor = XlsxExtractor(
            "src/test/resources/wording.xlsx",
            Column("A"),
            listOf(
                Language("en", "C"),
                Language("fr", "D"),
                Language("es", "E"))
        )

        val result = extractor.extract()
    }
}