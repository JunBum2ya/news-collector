package com.midas.newscollector.crawler

import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.TestInstance
import org.junit.jupiter.params.ParameterizedTest
import org.junit.jupiter.params.provider.Arguments
import org.junit.jupiter.params.provider.MethodSource
import java.io.IOException
import java.util.stream.Stream

@DisplayName("Crawling Test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class NewsDataCrawlerTest {
    @DisplayName("CompanyCode와 키워드로 크롤링을 하면 NewsDto List를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideSingleCrawlingParameter")
    fun givenCrawlerTypeAndKeyword_whenCrawlingKeyword_thenReturnsNewsDtoList(crawlerType: CrawlerType) {
        //given
        val keyword = "선거"
        //when
        val crawler = NewsDataCrawlerFactory().newInstance(crawlerType)
        val newsDtoList = crawler.searchNewsList(keyword)
        //then
        newsDtoList.forEach { println(it.title) }
    }

    private fun provideSingleCrawlingParameter(): Stream<Arguments> {
        return Stream.of<Arguments>(
            Arguments.of(CrawlerType.GOOGLE, "코로나"),
            Arguments.of(CrawlerType.DAUM, "코로나"),
            Arguments.of(CrawlerType.NAVER, "코로나")
        )
    }

    @DisplayName("CompanyCode와 키워드 리스트로 크롤링을 하면 NewsDto List를 반환한다.")
    @ParameterizedTest
    @MethodSource("provideManyKeywordCrawlingParameter")
    @Throws(IOException::class)
    fun givenCompanyCodeAndKeywordList_whenCrawlingNews_thenNewsDtoList(
        companyType: CrawlerType,
        keywords: List<String>
    ) {
        //given
        val newsCrawlerFactory = NewsDataCrawlerFactory()
        val newsCrawler = newsCrawlerFactory.newInstance(companyType)
        //when
        val newsDtoList = newsCrawler.searchNewsList(keywords)
        //then
        newsDtoList.forEach { println(it.title) }
//        assertThat(newsDtoList).isNotEmpty();
//        assertThat(newsDtoList.size()).isGreaterThan(0);
    }

    private fun provideManyKeywordCrawlingParameter(): Stream<Arguments> {
        return Stream.of<Arguments>(
            Arguments.of(CrawlerType.GOOGLE, listOf("코로나", "신약")),
            Arguments.of(CrawlerType.DAUM, listOf("마을", "용인")),
            Arguments.of(CrawlerType.NAVER, listOf("무역", "코딩"))
        )
    }


}