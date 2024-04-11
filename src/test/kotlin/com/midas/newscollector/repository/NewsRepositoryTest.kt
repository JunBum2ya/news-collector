package com.midas.newscollector.repository

import com.midas.newscollector.config.JpaConfig
import com.midas.newscollector.domain.Keyword
import com.midas.newscollector.domain.News
import com.midas.newscollector.dto.param.NewsSearchParam
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.data.domain.PageRequest
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - Member")
@DataJpaTest
@Import(JpaConfig::class)
@ActiveProfiles("testdb")
class NewsRepositoryTest(@Autowired private val newsRepository: NewsRepository) {

    @DisplayName("news를 저장하면 news가 반환된다.")
    @Test
    fun givenNews_whenSaveNews_thenReturnsNews() {
        //given
        val news = News(publisher = "동아일보", title = "코로나 증가", description = "코로나가 연속 증가합니다.", url = "corona.com")
        news.addKeyword(keyword = Keyword(name = "코로나"))
        news.addKeyword(keyword = Keyword(name = "보건"))
        //when
        val savedNews: News = newsRepository.save(news)
        //then
        assertThat(savedNews).isNotNull()
        assertThat(savedNews.getTitle()).isEqualTo("코로나 증가")
        assertThat(savedNews.keywords.size).isEqualTo(2)
        assertThat(savedNews.getCreatedAt()).isNotNull()
        assertThat(savedNews.getUpdatedAt()).isNotNull()
    }

    @DisplayName("제목과 신문사 파라미터와 페이지네이션으로 뉴스를 조회하면 뉴스 페이지가 반환된다.")
    @Test
    fun given_when_then() {
        //given
        val pageable = PageRequest.of(0, 10)
        val newsSearchParam = NewsSearchParam(title = "코로나 증가", publisher = "동아일보")
        saveNews()
        //when
        val page = newsRepository.searchNews(newsSearchParam,pageable)
        //then
        assertThat(page).isNotEmpty
        assertThat(page.totalPages).isEqualTo(1)
        assertThat(page.totalElements).isEqualTo(1)
    }

    private fun saveNews() {
        newsRepository.saveAndFlush(News(publisher = "동아일보", title = "코로나 증가", description = "코로나가 연속 증가합니다.", url = "corona.com"))
    }

}