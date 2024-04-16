package com.midas.newscollector.crawler

import com.midas.newscollector.domain.Keyword
import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.KeywordRepository
import com.midas.newscollector.repository.NewsRepository
import com.midas.newscollector.service.CompanyService
import com.midas.newscollector.service.NewsService
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.util.stream.Collectors

@Transactional
@Component
class NewsDataCrawlerStrategy(
    private val companyService: CompanyService,
    private val newsDataCrawlerFactory: NewsDataCrawlerFactory,
    private val newsRepository: NewsRepository,
    private val keywordRepository: KeywordRepository
) {
    fun crawlNews(keyword: String): List<NewsDto> {
        val companies = companyService.getAllActiveCompanyList()
            .map { it.companyType }
            .toSet()
        val crawlers = newsDataCrawlerFactory.newInstance(companies)
        val keywordEntity = keywordRepository.getKeywordByName(keyword)
            ?: throw CustomException(ResponseStatus.ACCESS_NOT_EXIST_ENTITY, "Keyword does not exist")
        return crawlers.flatMap { crawlNews(keywordEntity, it) }
    }

    fun crawlNews(): List<NewsDto> {
        val companies = companyService.getAllActiveCompanyList()
            .map { it.companyType }
            .toSet()
        val crawlers = newsDataCrawlerFactory.newInstance(companies)
        val keywords = keywordRepository.searchActiveKeywords()
        return keywords.flatMap { keyword -> crawlers.flatMap { crawler -> crawlNews(keyword, crawler) } }
    }

    /**
     * 크롤링 로직
     */
    fun crawlNews(keyword: Keyword, newsDataCrawler: NewsDataCrawler): List<NewsDto> {
        return newsDataCrawler.searchNewsList(keyword.name).map { saveNews(it, keyword) }
    }

    /**
     * DB 저장 로직
     */
    fun saveNews(newsDto: NewsDto, keyword: Keyword): NewsDto {
        val news = newsRepository.getNewsByUrl(newsDto.url) ?: newsRepository.save(newsDto.toEntity())
        news.addKeyword(keyword)
        return NewsDto.from(news)
    }
}