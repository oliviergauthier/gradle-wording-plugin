package com.betomorrow.gradle.wording.domain.updater.xml

import com.betomorrow.gradle.wording.domain.updater.WordingUpdater
import org.w3c.dom.Document
import org.w3c.dom.Element
import java.nio.file.Path

class XmlWordingUpdater(private val path: Path) : WordingUpdater {

    /**
     * Update file with wording and returns updated keys
     */
    override fun update(wording: Map<String, String>, addMissingWording: Boolean): Set<String> {

        val outputKeys = HashSet<String>()

        val document = loadOrCreateXmlDocument(path)

        updateStrings(document, wording, outputKeys)
        updateStringArrays(document, wording, outputKeys)

        val missingWordings = wording.keys - outputKeys
        if (addMissingWording) {
            val resource = document.firstOrCreateTagName(RESOURCE_TAG_NAME)
            addMissingStrings(
                resource,
                missingWordings.filter { !sectionPattern.matches(it) },
                wording
            )

            addMissingStringArrays(
                resource,
                missingWordings.filter { sectionPattern.matches(it) },
                wording
            )
            outputKeys.addAll(wording.keys)
        }

        writeToFile(document, path)

        return outputKeys
    }

    private fun updateStrings(document: Document, wording: Map<String, String>, outputKeys: HashSet<String>) {
        document.getElementsIteratorByTagName(STRING_TAG_NAME).forEach { node ->
            val key = node.getAttribute(NAME_ATTR)
            if (wording.containsKey(key)) {
                node.textContent = wording[key]
                outputKeys.add(key)
            }
        }
    }

    private fun updateStringArrays(document: Document, wording: Map<String, String>, outputKeys: HashSet<String>) {
        document.getElementsIteratorByTagName(STRING_ARRAY_TAG_NAME).forEach { node ->
            val key = node.getAttribute(NAME_ATTR)
            val childs = (node as Element).getElementsByTagName(ITEM_TAG_NAME)
            for (c in 0 until childs.length) {
                val subKey = "$key[$c]"
                if (wording.containsKey(subKey)) {
                    childs.item(c).textContent = wording[subKey]
                    outputKeys.add(subKey)
                }
            }
        }
    }

    private fun addMissingStrings(resources: Element, keys: List<String>, wording: Map<String, String>) {
        keys.forEach { key ->
            resources.appendNewChild(STRING_TAG_NAME) {
                setAttribute(NAME_ATTR, key)
                wording[key]?.let {
                    appendTextNode(it)
                }
            }
        }
    }

    private fun addMissingStringArrays(resources: Element, keys: List<String>, wording: Map<String, String>) {
        val arraysSizes = computeStringArraysSize(keys)

        arraysSizes.forEach { key, value ->
            val node = resources
                .getElementsIteratorByTagName(STRING_ARRAY_TAG_NAME)
                .firstOrNull { it.hasAttribute(NAME_ATTR, key) }
                ?: resources.appendNewChild(STRING_ARRAY_TAG_NAME) {
                    setAttribute(NAME_ATTR, key)
                }

            val elt = node as Element
            val missingNodeCount = value - elt.getElementsByTagName(ITEM_TAG_NAME).length
            for (i in 0 until missingNodeCount) {
                elt.appendNewChild(ITEM_TAG_NAME)
            }
        }

        updateStringArrays(resources.ownerDocument, wording, HashSet())
    }

    private fun computeStringArraysSize(keys: List<String>): Map<String, Int> {
        val values = HashMap<String, Int>()
        keys.forEach {
            val result = sectionPattern.find(it)
            if (result != null) {
                val key = result.groupValues[1]
                val index = result.groupValues[2]
                values[key] = Math.max(values[key] ?: 1, index.toInt() + 1)
            }
        }
        return values
    }

    companion object {
        val sectionPattern = Regex("(.*)\\[([0-9]*)\\]")
        const val RESOURCE_TAG_NAME = "resources"
        const val STRING_ARRAY_TAG_NAME = "string-array"
        const val STRING_TAG_NAME = "string"
        const val ITEM_TAG_NAME = "item"
        const val NAME_ATTR = "name"
    }
}
