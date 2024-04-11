package com.midas.newscollector.repository

import com.midas.newscollector.config.JpaConfig
import com.midas.newscollector.domain.Keyword
import com.midas.newscollector.domain.News
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
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
}