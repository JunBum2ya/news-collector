package com.midas.newscollector.crawler

import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.service.CompanyService
import com.midas.newscollector.service.NewsService
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@DisplayName("비즈니스 로직 - 뉴스 크롤링")
@ExtendWith(MockitoExtension::class)
class NewsDataCrawlerStrategyTest {
    @InjectMocks
    private lateinit var newsDataCrawlerStrategy: NewsDataCrawlerStrategy
    @Mock
    private lateinit var newsService: NewsService
    @Mock
    private lateinit var companyService: CompanyService

    @DisplayName("단일 키워드를 입력하면 news를 크롤링 하고 news 리스트 반환")
    @Test
    fun givenKeyword_whenCrawlNews_thenReturnsNewsDtoList() {
        //given
        given(companyService.getAllActiveCompanyList())
            .willReturn(listOf(CompanyDto(CompanyType.GOOGLE), CompanyDto(CompanyType.NAVER)))
        given(newsService.createNews(any())).willReturn(createNewsDto())
        //when
        val newsList = newsDataCrawlerStrategy.crawlNews("코로나")
        //then
        then(companyService).should().getAllActiveCompanyList()
        verify(newsService, times(newsList.size)).createNews(any())
        assertThat(newsList).isNotNull
    }

    @DisplayName("여러개의 키워드를 입력하면 news를 크롤링 하고 news 리스트 반환")
    @Test
    fun givenKeywords_whenCrawlNews_thenReturnsNewsDtoList() {
        //given
        given(companyService.getAllActiveCompanyList())
            .willReturn(listOf(CompanyDto(CompanyType.GOOGLE), CompanyDto(CompanyType.NAVER)))
        given(newsService.createNews(any())).willReturn(createNewsDto())
        //when
        val newsList = newsDataCrawlerStrategy.crawlNews(listOf("코로나", "선거"))
        //then
        then(companyService).should().getAllActiveCompanyList()
        verify(newsService, times(newsList.size)).createNews(any())
        assertThat(newsList).isNotNull
    }

    private fun createNewsDto(): NewsDto {
        return NewsDto(title = "코로나", description = "코로나로 인해...", publisher = "test", url = "test.com")
    }

    // add
    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

}