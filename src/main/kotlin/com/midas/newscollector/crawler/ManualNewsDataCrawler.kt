package com.midas.newscollector.crawler

import com.midas.newscollector.dto.NewsDto

/**
 * 수동 입력 데이터일 경우 null처리
 */
class ManualNewsDataCrawler: NewsDataCrawler {
    override fun searchNewsList(keyword: String): List<NewsDto> {
        return listOf()
    }

    override fun searchNewsList(keywords: List<String>): List<NewsDto> {
        return listOf()
    }
}