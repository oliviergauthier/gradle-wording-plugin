package com.betomorrow.gradle.wording.domain

class Language(val name: String, column: String) {

    val index: Int = Column(column).index
}
