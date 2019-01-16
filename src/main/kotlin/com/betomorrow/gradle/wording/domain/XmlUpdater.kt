package com.betomorrow.gradle.wording.domain

import org.w3c.dom.Element
import java.io.File
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

class XmlUpdater(val path: String) {

    /**
     * Update file with wording and returns updated keys
     */
    fun update(wording: Map<String, String>) : Set<String> {

        val outputKeys = HashSet<String>()

        val documentBuilderFactory = DocumentBuilderFactory.newInstance()
        val documentBuilder = documentBuilderFactory.newDocumentBuilder()
        val document = documentBuilder.parse(File(path))

        val strings = document.getElementsByTagName("string")
        for (i in 0 until strings.length) {
            val item = strings.item(i)
            val key = item.attributes.getNamedItem("name").nodeValue
            if (wording.containsKey(key)) {
                item.textContent = wording[key]
                outputKeys.add(key)
            }
        }

        val stringArrays = document.getElementsByTagName("string-array")
        for(i in 0 until stringArrays.length) {
            val item = stringArrays.item(i)
            val key = item.attributes.getNamedItem("name").nodeValue
            val childs = (item as Element).getElementsByTagName("item")
            for(c in 0 until childs.length) {
                val subKey  = "$key[$c]"
                if (wording.containsKey(subKey)) {
                    childs.item(c).textContent = wording[subKey]
                    outputKeys.add(subKey)
                }
            }
        }

        val transformerFactory = TransformerFactory.newInstance()
        val transformer = transformerFactory.newTransformer()
        val domSource = DOMSource(document)
        val streamResult = StreamResult(File(path))
        transformer.transform(domSource, streamResult)

        return outputKeys
    }

}