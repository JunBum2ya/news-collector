package com.midas.newscollector.crawler

import com.midas.newscollector.domain.Keyword
import com.midas.newscollector.domain.News
import com.midas.newscollector.domain.QKeyword.keyword
import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.repository.KeywordRepository
import com.midas.newscollector.repository.NewsRepository
import com.midas.newscollector.service.CompanyService
import com.midas.newscollector.service.NewsService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class NewsDataCrawlerStrategyTest : BehaviorSpec({
    val companyService = mockk<CompanyService>()
    val newsDataCrawlerFactory = mockk<NewsDataCrawlerFactory>()
    val newsRepository = mockk<NewsRepository>()
    val keywordRepository = mockk<KeywordRepository>()
    val newsDataCrawlerStrategy = NewsDataCrawlerStrategy(companyService, newsDataCrawlerFactory, newsRepository, keywordRepository)

    Given("단일 키워드를 입력하면") {
        val keyword = "선거"
        every { companyService.getAllActiveCompanyList() }
            .returns(listOf(CompanyDto(CompanyType.NAVER),CompanyDto(CompanyType.DAUM)))
        every { newsDataCrawlerFactory.newInstance(setOf(CompanyType.NAVER,CompanyType.DAUM)) }.returns(listOf(TestNewsDataCrawler()))
        every { keywordRepository.getKeywordByName(any(String::class)) }.returns(Keyword(id = 1L, name = keyword))
        every { newsRepository.getNewsByUrl(any(String::class)) }.returns(null)
        every { newsRepository.save(any(News::class)) }.returns(createNews())
        When("뉴스를 크롤링하고") {
            val newsList = newsDataCrawlerStrategy.crawlNews(keyword)
            Then("NewsDto 리스트를 반환한다.") {
                newsList shouldHaveSize 1
                newsList[0].keywords shouldHaveSize 1
                verify { companyService.getAllActiveCompanyList() }
                verify { newsDataCrawlerFactory.newInstance(setOf(CompanyType.NAVER,CompanyType.DAUM)) }
                verify { keywordRepository.getKeywordByName(any(String::class)) }
                verify { newsRepository.getNewsByUrl(any(String::class)) }
                verify { newsRepository.save(any(News::class)) }
            }
        }
    }
    Given("여러개의 키워드를 입력하면") {
        val keywords = listOf("선거", "코로나")
        every { companyService.getAllActiveCompanyList() }.returns(listOf(CompanyDto(CompanyType.NAVER),CompanyDto(CompanyType.DAUM)))
        every { newsDataCrawlerFactory.newInstance(setOf(CompanyType.DAUM,CompanyType.NAVER)) }.returns(listOf(TestNewsDataCrawler()))
        every { keywordRepository.searchActiveKeywords() }.returns(keywords.map { Keyword(name = it) })
        every { newsRepository.getNewsByUrl(any(String::class)) }.returns(null)
        every { newsRepository.save(any(News::class)) }.returns(createNews())
        When("뉴스를 크롤링 하고") {
            val newsList = newsDataCrawlerStrategy.crawlNews()
            Then("NewsDto 리스트를 반환한다.") {
                newsList shouldHaveSize 2
                newsList[0].keywords shouldHaveSize 1
                verify { companyService.getAllActiveCompanyList() }
                verify { newsDataCrawlerFactory.newInstance(setOf(CompanyType.NAVER,CompanyType.DAUM)) }
                verify { keywordRepository.getKeywordByName(any(String::class)) }
                verify { newsRepository.getNewsByUrl(any(String::class)) }
                verify { newsRepository.save(any(News::class)) }
            }
        }
    }

}) {

    companion object {
        fun createNewsDto(): NewsDto {
            return NewsDto(title = "코로나", description = "코로나로 인해...", publisher = "test", url = "test.com")
        }

        fun createNews(): News {
            return News(title = "코로나", description = "코로나로 인해...", publisher = "test", url = "test.com")
        }
    }

}