package com.midas.newscollector.crawler

import com.midas.newscollector.dto.NewsDto

/**
 * 테스트용 뉴스크롤링 클래스
 */
class TestNewsDataCrawler: NewsDataCrawler {
    override fun searchNewsList(keyword: String): List<NewsDto> {
        return listOf(createNewsDto())
    }

    override fun searchNewsList(keywords: List<String>): List<NewsDto> {
        return listOf(createNewsDto(),createNewsDto())
    }

    private fun createNewsDto(): NewsDto {
        return NewsDto(title = "코로나", description = "코로나로 인해...", publisher = "test", url = "test.com")
    }

}