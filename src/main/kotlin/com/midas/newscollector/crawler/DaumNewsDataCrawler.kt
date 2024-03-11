package com.midas.newscollector.crawler

import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.util.DateUtil
import org.jsoup.Jsoup
import org.jsoup.nodes.Element
import java.io.IOException
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class DaumNewsDataCrawler : NewsDataCrawler {
    private val BASE_URL = "https://search.daum.net/search?w=news&DA=PGD&spacing=0&q=%s&sd=%s&ed=%s&period=d&p=%d"
    override fun searchNewsList(keyword: String): List<NewsDto> {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("YYYYMMddHHmmss") //날짜 형식 지정
        val currentDateTime: LocalDateTime = DateUtil.now()
        val startDateTime = currentDateTime.minusHours(1).format(dateTimeFormatter) //시작 시간
        val endDateTime = currentDateTime.format(dateTimeFormatter) //검색 범위 마지막
        return searchNewsList(keyword, startDateTime, endDateTime)
    }

    override fun searchNewsList(keywords: List<String>): List<NewsDto> {
        val dateTimeFormatter = DateTimeFormatter.ofPattern("YYYYMMddHHmmss")
        val currentDateTime = DateUtil.now()
        val startDateTime = currentDateTime.minusHours(1).format(dateTimeFormatter)
        val endDateTime = currentDateTime.format(dateTimeFormatter)
        val newsDataList: MutableList<NewsDto> = java.util.ArrayList()
        for (keyword in keywords) {
            //키워드 마다 크롤링 실행
            newsDataList.addAll(searchNewsList(keyword!!, startDateTime, endDateTime))
        }
        return newsDataList
    }

    /**
     * 1시간 전 뉴스 크롤링
     * @param keyword 키워드
     * @param startDateTime 검색 시작 지점
     * @param endDateTime 검색 종료 지점
     * @return List: NewsDto
     * @throws IOException
     */
    @Throws(IOException::class)
    private fun searchNewsList(keyword: String, startDateTime: String, endDateTime: String): List<NewsDto> {
        val newsDtoList: MutableList<NewsDto> = ArrayList()
        var nextButton: Element? = null
        var page = 1
        do {
            val url = BASE_URL.format(keyword, startDateTime, endDateTime, page++)
            val document = Jsoup.connect(url).get()
            val elements = document.select("ul.c-list-basic > li")
            newsDtoList.addAll(elements.stream().map(this::extractNewsListData).toList())
            nextButton = document.selectFirst(".btn_next")
        } while (nextButton != null && !nextButton.hasAttr("disable"))
        return newsDtoList
    }


    /**
     * 뉴스 데이터 추출 메소드
     * @param element 뉴스 단건 엘리먼트
     * @return NewsDto
     * @throws NullPointerException
     */
    @Throws(NullPointerException::class)
    private fun extractNewsListData(element: Element): NewsDto {
        val cTitDoc = element.selectFirst(".c-tit-doc")
        val cItemContent = element.selectFirst(".c-item-content")
        val thumbnailElement = cItemContent.selectFirst(".thumb_bf img")
        return NewsDto(
            publisher = cTitDoc.selectFirst(".txt_info").text(),
            thumbnailPath = thumbnailElement?.attr("src"),
            title = cItemContent.selectFirst(".item-title .tit-g a").text(),
            description = cItemContent.selectFirst(".item-contents .conts-desc a").text(),
            url = cItemContent.selectFirst(".item-contents .conts-desc a").attr("href")
        )
    }

}