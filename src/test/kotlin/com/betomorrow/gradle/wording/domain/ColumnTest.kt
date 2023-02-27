package com.betomorrow.gradle.wording.domain

import org.assertj.core.api.Assertions
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows

class ColumnTest {

    @Test
    fun testWithColumnA() {
        val col = Column("a")
        Assertions.assertThat(col.index).isEqualTo(0)
    }

    @Test
    fun testWithColumnB() {
        val col = Column("b")
        Assertions.assertThat(col.index).isEqualTo(1)
    }

    @Test
    fun testWithColumnZ() {
        val col = Column("z")
        Assertions.assertThat(col.index).isEqualTo(25)
    }

    @Test
    fun testWithColumnAA() {
        val col = Column("aa")
        Assertions.assertThat(col.index).isEqualTo(26)
    }

    @Test
    fun testWithColumnAB() {
        val col = Column("ab")
        Assertions.assertThat(col.index).isEqualTo(27)
    }

    @Test
    fun testWithColumnBA() {
        val col = Column("ba")
        Assertions.assertThat(col.index).isEqualTo(52)
    }

    @Test
    fun testWithColumnABCD() {
        val col = Column("abcd")
        Assertions.assertThat(col.index).isEqualTo(19009)
    }

    @Test
    fun testConstructWithInvalidCharacters() {
        assertThrows<AssertionError> {
            Column("0123456789")
        }
    }
}
