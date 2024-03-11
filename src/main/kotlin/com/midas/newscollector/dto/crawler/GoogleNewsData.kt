package com.midas.newscollector.dto.crawler

import org.w3c.dom.Document
import org.w3c.dom.Element

class GoogleNewsData(val channel: Channel) {
    constructor(document: Document) : this(
        channel = Channel.of(document.getElementsByTagName("channel").item(0) as Element)
    )

    class Channel(
        val generator: String,
        val title: String,
        val link: String,
        val language: String,
        val webMaster: String,
        val copyright: String,
        val lastBuildDate: String,
        val description: String,
        val items: List<Item>
    ) {
        companion object {
            fun of(element: Element): Channel {
                val nodeList = element.getElementsByTagName("item")
                val items = mutableListOf<Item>()
                for (i in 0..<nodeList.length) {
                    items.add(Item.of(nodeList.item(i) as Element))
                }
                return Channel(
                    generator = element.getElementsByTagName("generator").item(0).textContent,
                    title = element.getElementsByTagName("title").item(0).textContent,
                    link = element.getElementsByTagName("link").item(0).textContent,
                    language = element.getElementsByTagName("language").item(0).textContent,
                    webMaster = element.getElementsByTagName("webMaster").item(0).textContent,
                    copyright = element.getElementsByTagName("copyright").item(0).textContent,
                    lastBuildDate = element.getElementsByTagName("lastBuildDate").item(0).textContent,
                    description = element.getElementsByTagName("description").item(0).textContent,
                    items = items
                )
            }
        }
    }

    class Item(val title: String) {
        companion object {
            fun of(element: Element): Item {
                return Item(title = element.getElementsByTagName("title").item(0).textContent)
            }
        }
    }
}