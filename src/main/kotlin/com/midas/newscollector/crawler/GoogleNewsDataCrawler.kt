package com.midas.newscollector.crawler

import com.midas.newscollector.dto.KeywordDto
import com.midas.newscollector.dto.NewsDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.net.URL
import java.util.function.Function

class GoogleNewsDataCrawler : NewsDataCrawler {
    private val BASE_URL = "https://www.google.com/search?q=%s&start=%d&tbs=qdr:h&tbm=nws"

    /**
     * 단일 뉴스 키워드 구현
     */
    override fun searchNewsList(keyword: String): List<NewsDto> {
        val newsDtoList: MutableList<NewsDto> = ArrayList()
        var offset = 0
        do {
            val url = BASE_URL.format(keyword, offset++ * 10) //offset 만큼 넘어감
            val document = Jsoup.connect(url).get()
            val searchElement = document.getElementById("search")
            if (searchElement == null || searchElement.childNodeSize() == 0) {
                break //검색 결과가 없으면 루프 종료
            }
            val newsElements = document.select(".SoaBEf") //Element에서 데이터 추출
            newsDtoList.addAll(newsElements.stream()
                .map { this.extractNewsListData(it, keyword) }.toList()
            ) //리스트에 담는다.
        } while (true)
        return newsDtoList
    }

    /**
     * 키워드 전체 검색
     */
    override fun searchNewsList(keywords: List<String>): List<NewsDto> {
        val newsDtoList = mutableListOf<NewsDto>()
        for (keyword in keywords) {
            newsDtoList.addAll(searchNewsList(keyword))
        }
        return newsDtoList
    }

    /**
     * 구글 뉴스에서 NewsData 추출
     * @param element
     * @return NewsDto
     * @throws NullPointerException
     */
    @Throws(NullPointerException::class)
    private fun extractNewsListData(element: Element, keyword: String): NewsDto {
        val thumbnailElement = element.selectFirst(".uhHOwf img")
        return NewsDto(
            publisher = element.select(".MgUUmf span").text(),
            title = element.select(".n0jPhd").text(),
            description = element.select(".GI74Re").text(),
            thumbnailPath = thumbnailElement?.attr("attr"),
            url = element.selectFirst(".WlydOe").attr("href")
        )
    }

}