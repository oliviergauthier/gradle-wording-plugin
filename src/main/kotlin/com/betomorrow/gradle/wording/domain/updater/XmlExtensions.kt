package com.betomorrow.gradle.wording.domain.updater

import org.w3c.dom.Document
import org.w3c.dom.Element
import org.w3c.dom.Node
import org.w3c.dom.Text
import java.nio.file.Files
import java.nio.file.Path
import javax.xml.parsers.DocumentBuilderFactory
import javax.xml.transform.OutputKeys
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.dom.DOMSource
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

fun loadOrCreateXmlDocument(path: Path): Document {
    val documentBuilderFactory = DocumentBuilderFactory.newInstance()
    val documentBuilder = documentBuilderFactory.newDocumentBuilder()
    return if (Files.isRegularFile(path)) {
        documentBuilder.parse(path.toFile())
    } else {
        documentBuilder.newDocument()
    }
}

fun writeToFile(document: Document, path: Path) {
    val transformer = prettyPrintedTransformer()
    transformer.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, "yes")
    transformer.setOutputProperty(OutputKeys.OMIT_XML_DECLARATION, "yes")

    val domSource = DOMSource(document)
    val streamResult = StreamResult(path.toFile())
    transformer.transform(domSource, streamResult)
}

private fun prettyPrintedTransformer(): Transformer {
    val schema = object {}.javaClass.getResource("/pretty-print-format.xslt")!!
    return TransformerFactory.newInstance().newTransformer(StreamSource(schema.openStream()))
}

/**
 * Document Extensions
 */
fun Document.getElementsIteratorByTagName(name: String): Iterable<Node> {
    val elements = this.getElementsByTagName(name)
    return object : Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return iterator {
                for (i in 0 until elements.length) {
                    yield(elements.item(i))
                }
            }
        }
    }
}

fun Document.firstOrCreateTagName(name: String): Element {
    return this.getElementsByTagName(name).let {
        if (it.length > 0) {
            it.item(0) as Element
        } else {
            val node = this.createElement(XmlWordingUpdater.RESOURCE_TAG_NAME)
            this.appendChild(node)
            node
        }
    }
}

/**
 * Node Extensions
 */
fun Node.getAttribute(name: String): String {
    return this.attributes.getNamedItem(name).nodeValue
}

fun Node.getAttributesIterator(): Iterable<Node> {
    return object : Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return iterator {
                for (i in 0 until attributes.length) {
                    yield(attributes.item(i))
                }
            }
        }
    }
}

fun Node.hasAttribute(name: String, value: String): Boolean {
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
fun Element.getElementsIteratorByTagName(name: String): Iterable<Node> {
    val elements = this.getElementsByTagName(name)
    return object : Iterable<Node> {
        override fun iterator(): Iterator<Node> {
            return iterator {
                for (i in 0 until elements.length) {
                    yield(elements.item(i))
                }
            }
        }
    }
}

fun Element.appendNewChild(name: String, block: Element.() -> Unit = {}): Element {
    val elt = this.ownerDocument.createElement(name)
    block(elt)
    appendChild(elt)
    return elt
}

fun Element.appendTextNode(name: String): Text {
    val elt = this.ownerDocument.createTextNode(name)
    appendChild(elt)
    return elt
}
