package com.midas.newscollector.crawler

import org.junit.jupiter.api.Test

class NewsDataCrawlerTest {
    @Test
    fun givenCrawlerTypeAndKeyword_whenCrawlingKeyword_thenReturnsNewsDtoList() {
        //given
        val crawlerType = CrawlerType.NAVER
        val keyword = "선거"
        //when
        val crawler = NewsDataCrawlerFactory().newInstance(crawlerType)
        val newsDtoList = crawler.searchNewsList(keyword)
        //then
        println(newsDtoList.size)
        newsDtoList.forEach { println(it.title) }
    }
}