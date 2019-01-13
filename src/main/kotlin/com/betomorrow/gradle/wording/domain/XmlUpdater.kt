package com.betomorrow.gradle.wording.domain

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import java.io.File

class XmlUpdater {

    fun update(path: String, wording: Wording) {
        val mapper = XmlMapper()
        val tree = mapper.readTree(File(path))
        tree.forEach {
            it.setVal
        }
        println(tree)
    }

}