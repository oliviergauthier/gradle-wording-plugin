package com.betomorrow.gradle.wording.domain

import java.lang.Exception

interface Wordings {
    fun get(language: String) : Wording
}

interface Wording {
    val language: String
    fun get(key: String) : String?
}

class UnknownLanguageException(language: String) : Exception("Language $language is unknown")

class MutableWordings : Wordings {

    private val wordings : MutableMap<String, MutableWording> = HashMap()

    override fun get(language: String) : Wording {
        return wordings.getOrElse(language) { throw UnknownLanguageException(language)}
    }

    fun getOrPut(language: String) : MutableWording {
        return wordings.getOrPut(language) { MutableWording(language) }
    }

}

class MutableWording(override val language: String) : Wording {

    private val wordings : MutableMap<String, String> = HashMap()

    override fun get(key : String) : String? {
        return wordings.getOrDefault(key, null)
    }

    fun add(key: String, value: String) {
        wordings[key] = value
    }
}