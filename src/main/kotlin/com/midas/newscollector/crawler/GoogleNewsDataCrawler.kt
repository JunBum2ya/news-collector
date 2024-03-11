package com.midas.newscollector.crawler

import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.dto.crawler.GoogleNewsData
import org.w3c.dom.Element
import javax.xml.parsers.DocumentBuilderFactory

class GoogleNewsDataCrawler: NewsDataCrawler {
    private val BASE_URL = "https://news.google.com/rss/search?q=%s+when:1h&hl=ko&gl=KR&ceid=KR:ko"
    private val factory = DocumentBuilderFactory.newInstance()

    /**
     * 단일 뉴스 키워드 구현
     */
    override fun searchNewsList(keyword: String): List<NewsDto> {
        val newsDataList = mutableListOf<NewsDto>()
        val url = BASE_URL.format(keyword)
        val document = factory.newDocumentBuilder().parse(url)
        val newsData = GoogleNewsData(document)
        return newsDataList
    }

    /**
     * 키워드 전체 검색
     */
    override fun searchNewsList(keywords: List<String>): List<NewsDto> {
        val newsDtoList = mutableListOf<NewsDto>()
        for(keyword in keywords) {
            newsDtoList.addAll(searchNewsList(keyword))
        }
        return newsDtoList
    }

    fun parseXml(item: Element): NewsDto {
        val title = item.getElementsByTagName("title").item(0).textContent
        val publisher = item.getElementsByTagName("source").item(0).textContent
        val link = item.getElementsByTagName("link").item(0).textContent
        val description = item.getElementsByTagName("description").item(0).childNodes.item(0).textContent
        return NewsDto(publisher = publisher,title = title, url = link, description = description)
    }

}