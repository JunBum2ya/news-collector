package com.midas.newscollector.crawler

import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.service.CompanyService
import com.midas.newscollector.service.NewsService
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class NewsDataCrawlerStrategyTest : BehaviorSpec({
    val newsService = mockk<NewsService>()
    val companyService = mockk<CompanyService>()
    val newsDataCrawlerFactory = mockk<NewsDataCrawlerFactory>()
    val newsDataCrawlerStrategy = NewsDataCrawlerStrategy(companyService, newsService, newsDataCrawlerFactory)

    Given("단일 키워드를 입력하면") {
        val keyword = "선거"
        every { companyService.getAllActiveCompanyList() }
            .returns(listOf(CompanyDto(CompanyType.NAVER),CompanyDto(CompanyType.DAUM)))
        every { newsService.createNews(any(NewsDto::class)) }.returns(createNewsDto())
        every { newsDataCrawlerFactory.newInstance(setOf(CompanyType.NAVER,CompanyType.DAUM)) }.returns(listOf(TestNewsDataCrawler()))
        When("뉴스를 크롤링하고") {
            val newsList = newsDataCrawlerStrategy.crawlNews(keyword)
            Then("NewsDto 리스트를 반환한다.") {
                newsList shouldHaveSize 1
                verify { companyService.getAllActiveCompanyList() }
                verify { newsService.createNews(any(NewsDto::class)) }
                verify { newsDataCrawlerFactory.newInstance(setOf(CompanyType.NAVER,CompanyType.DAUM)) }
            }
        }
    }
    Given("여러개의 키워드를 입력하면") {
        val keywords = listOf("선거", "코로나")
        every { companyService.getAllActiveCompanyList() }.returns(listOf(CompanyDto(CompanyType.NAVER),CompanyDto(CompanyType.DAUM)))
        every { newsService.createNews(any(NewsDto::class)) }.returns(createNewsDto())
        every { newsDataCrawlerFactory.newInstance(setOf(CompanyType.DAUM,CompanyType.NAVER)) }.returns(listOf(TestNewsDataCrawler()))
        When("뉴스를 크롤링 하고") {
            val newsList = newsDataCrawlerStrategy.crawlNews(keywords)
            Then("NewsDto 리스트를 반환한다.") {
                newsList shouldHaveSize 2
                verify { companyService.getAllActiveCompanyList() }
                verify { newsService.createNews(any(NewsDto::class)) }
                verify { newsDataCrawlerFactory.newInstance(setOf(CompanyType.NAVER,CompanyType.DAUM)) }
            }
        }
    }

}) {

    companion object {
        fun createNewsDto(): NewsDto {
            return NewsDto(title = "코로나", description = "코로나로 인해...", publisher = "test", url = "test.com")
        }
    }

}