package com.midas.newscollector.crawler

import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.service.CompanyService
import com.midas.newscollector.service.NewsService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Transactional
@Component
class NewsDataCrawlerStrategy(
    private val companyService: CompanyService,
    private val newsService: NewsService,
    private val newsDataCrawlerFactory: NewsDataCrawlerFactory
) {
    fun crawlNews(keyword: String): List<NewsDto> {
        val companies = companyService.getAllActiveCompanyList()
            .stream()
            .map { it.companyType }
            .collect(Collectors.toSet())
        val crawlers = newsDataCrawlerFactory.newInstance(companies)
        val newsList = crawlers
            .flatMap { it.searchNewsList(keyword) }
            .map { newsService.createNews(it) }
        return newsList
    }

    fun crawlNews(keywords: List<String>): List<NewsDto> {
        val companies = companyService.getAllActiveCompanyList()
            .stream()
            .map { it.companyType }
            .collect(Collectors.toSet())
        val crawlers = newsDataCrawlerFactory.newInstance(companies)
        val newsList = crawlers.flatMap { it.searchNewsList(keywords) }.map { newsService.createNews(it) }
        return newsList
    }
}