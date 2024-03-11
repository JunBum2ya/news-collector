package com.midas.newscollector.crawler

import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.exception.CustomException
import java.io.IOException

interface NewsDataCrawler {
    @Throws(CustomException::class)
    fun searchNewsList(keyword: String): List<NewsDto>
    @Throws(CustomException::class)
    fun searchNewsList(keywords: List<String>): List<NewsDto>
}