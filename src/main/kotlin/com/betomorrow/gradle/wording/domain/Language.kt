package com.betomorrow.gradle.wording.domain

import com.betomorrow.gradle.wording.domain.xlsx.Column

class Language(val name: String, column: String) {

    val index: Int = Column(column).index
}
