package com.midas.newscollector.crawler

import com.midas.newscollector.dto.KeywordDto
import com.midas.newscollector.dto.NewsDto
import org.jsoup.Jsoup
import org.jsoup.nodes.Element

class NaverNewsDataCrawler : NewsDataCrawler {
    private val BASE_URL = "https://search.naver.com/search.naver?where=news&query=%s&pd=7&start=%d"

    override fun searchNewsList(keyword: String): List<NewsDto> {
        val newsList = mutableListOf<NewsDto>()
        var offset = 0
        do {
            val document = Jsoup.connect(BASE_URL.format(keyword, offset++ * 10 + 1)).get()
            if (document.selectFirst(".not_found02") != null) {
                break
            }
            newsList.addAll(document.select(".news_wrap")
                .stream()
                .map { extractNewsListData(it, keyword) }
                .toList()
            )
            try {
                Thread.sleep(500) //0.5초 대기 => 크롤링 방지 우회
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        } while (true)
        return newsList
    }

    override fun searchNewsList(keywords: List<String>): List<NewsDto> {
        val newsList = ArrayList<NewsDto>()
        for (keyword in keywords) {
            //키워드 마다 크롤링 실행
            newsList.addAll(searchNewsList(keyword))
        }
        return newsList
    }

    private fun extractNewsListData(element: Element, keyword: String): NewsDto {
        val thumbnailElement = element.selectFirst(".news_contents .dsc_thumb img")
        val titleElement = element.selectFirst(".news_contents .news_tit")
        return NewsDto(
            publisher = element.selectFirst(".news_info .thumb_box").text(),
            thumbnailPath = thumbnailElement?.attr("src"),
            title = if (titleElement != null) titleElement.text() else "",
            description = element.selectFirst(".news_contents .news_dsc .dsc_wrap a").text(),
            url = if (titleElement != null) titleElement.attr("href") else ""
        )
    }

}