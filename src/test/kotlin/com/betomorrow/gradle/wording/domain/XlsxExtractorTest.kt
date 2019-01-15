package com.betomorrow.gradle.wording.domain

import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.Test

class XlsxExtractorTest {

    @Test
    fun testParseTestFile() {

        val extractor = XlsxExtractor("src/test/resources/wording.xlsx", Column("A"))
        
        val en = extractor.extract(Column("C"))
        assertThat(en.get("key1")).isEqualTo("en value 1")
        assertThat(en.get("key2")).isEqualTo("en value 2")
        assertThat(en.get("key3")).isEqualTo("en value 3")
        assertThat(en.get("sections[0]")).isEqualTo("One")
        assertThat(en.get("sections[1]")).isEqualTo("Two")

        val fr =  extractor.extract(Column("D"))
        assertThat(fr.get("key1")).isEqualTo("fr value 1")
        assertThat(fr.get("key2")).isEqualTo("fr value 2")
        assertThat(fr.get("key3")).isEqualTo("fr value 3")
        assertThat(fr.get("sections[0]")).isEqualTo("Une")
        assertThat(fr.get("sections[1]")).isEqualTo("Deux")

        val es =  extractor.extract(Column("E"))
        assertThat(es.get("key1")).isEqualTo("es value 1")
        assertThat(es.get("key2")).isEqualTo("es value 2")
        assertThat(es.get("key3")).isEqualTo("es value 3")
        assertThat(es.get("sections[0]")).isEqualTo("Uno")
        assertThat(es.get("sections[1]")).isEqualTo("Duo")
    }
}