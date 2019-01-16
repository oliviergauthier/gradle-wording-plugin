package com.betomorrow.gradle.wording.domain

import com.sun.org.apache.xml.internal.serializer.OutputPropertiesFactory
import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import java.io.File
import java.nio.file.Files
import java.nio.file.Paths
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult

fun loadOrCreateXmlDocument(path : String) : Document {
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
    return if (Files.isRegularFile(Paths.get(path))) {
        documentBuilder.parse(File(path))
    } else {
        documentBuilder.newDocument()
    }
}

fun writeToFile(document : Document, path: String) {
    val transformerFactory = TransformerFactory.newInstance()
    transformerFactory.setAttribute("indent-number", 4)

    val transformer = transformerFactory.newTransformer()
    transformer.setOutputProperty(OutputKeys.INDENT, "yes")
    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC,"yes");
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes");
    transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_INDENT_AMOUNT, "4")
    transformer.setOutputProperty(OutputPropertiesFactory.S_KEY_LINE_SEPARATOR, "4")

    val domSource = DOMSource(document)
    val streamResult = StreamResult(File(path))
    transformer.transform(domSource, streamResult)
}

/**
 * Document Extensions
 */
fun Document.getElementsIteratorByTagName(name: String) : Iterable<Node>{
    val elements = this.getElementsByTagName(name)
    return object: Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return iterator {
                for (i in 0 until elements.length) {
                    yield(elements.item(i))
                }
            }
        }
    }
}

fun Document.firstOrCreateTagName(name: String) : Element {
    return this.getElementsByTagName(name).let {
        if (it.length > 0) {
            it.item(0) as Element
        } else {
            val node = this.createElement(XmlUpdater.RESOURCE_TAG_NAME)
            this.appendChild(node)
            node
        }
    }
}

/**
 * Node Extensions
 */
fun Node.getAttribute(name: String) : String {
    return this.attributes.getNamedItem(name).nodeValue
}

fun Node.getAttributesIterator() : Iterable<Node> {
    return object: Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return iterator {
                for (i in 0 until attributes.length) {
                    yield(attributes.item(i))
                }
            }
        }
    }
}

fun Node.hasAttribute(name: String, value: String) : Boolean {
    this.getAttributesIterator().forEach {
        if (it.nodeName == name && it.nodeValue == value) {
            return true
        }
    }
    return false
}


/**
 * Element Extensions
 */
fun Element.getElementsIteratorByTagName(name: String) : Iterable<Node>{
    val elements = this.getElementsByTagName(name)
    return object: Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return iterator {
                for (i in 0 until elements.length) {
                    yield(elements.item(i))
                }
            }
        }
    }
}

fun Element.firstOrCreateTagName(name: String) : Element {
    return this.getElementsByTagName(name).let {
        if (it.length > 0) {
            it.item(0) as Element
        } else {
            val node = this.ownerDocument.createElement(name)
            this.appendChild(node)
            node
        }
    }
}

fun Element.appendNewChild(name: String, block: Element.() -> Unit = {}) : Element {
    val elt = this.ownerDocument.createElement(name)
    block(elt)
    appendChild(elt)
    return elt
}


fun Element.appendTextNode(name: String) : Text {
    val elt = this.ownerDocument.createTextNode(name)
    appendChild(elt)
    return elt
}

