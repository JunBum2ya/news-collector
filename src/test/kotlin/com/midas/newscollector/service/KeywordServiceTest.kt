package com.midas.newscollector.service

import com.midas.newscollector.crawler.NewsDataCrawlerStrategy
import com.midas.newscollector.domain.Keyword
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.KeywordRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers
import org.mockito.BDDMockito.*
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.jupiter.MockitoExtension

@DisplayName("비즈니스 로직 - 키워드")
@ExtendWith(MockitoExtension::class)
class KeywordServiceTest {
    @InjectMocks
    private lateinit var keywordService: KeywordService

    @Mock
    private lateinit var keywordRepository: KeywordRepository
    @Mock
    private lateinit var crawlerStrategy: NewsDataCrawlerStrategy

    @DisplayName("활성화된 키워드를 조회하면 키워드 리스트가 반환된다.")
    @Test
    fun givenNothing_whenSearchActiveKeyword_thenReturnsKeywordList() {
        //given
        given(keywordRepository.searchActiveKeywords()).willReturn(listOf(Keyword(name = "선거"), Keyword(name = "코로나")))
        //when
        val keywords = keywordService.getActiveKeywords()
        //then
        then(keywordRepository).should().searchActiveKeywords()
        assertThat(keywords).isNotEmpty
        assertThat(keywords.size).isEqualTo(2)
    }

    @DisplayName("이미 저장되어 있는 키워드를 활성화 하면 keywordDto가 반환된다.")
    @Test
    fun givenExistKeywordStr_whenActivateKeyword_thenReturnsKeywordDto() {
        //given
        given(keywordRepository.getKeywordByName(any())).willReturn(Keyword("코로나", active = false))
        //when
        val keyword = keywordService.activateKeyword("코로나")
        //then
        then(keywordRepository).should().getKeywordByName(any())
        then(crawlerStrategy).should().crawlNews("코로나")
        assertThat(keyword).isNotNull
        assertThat(keyword.active).isTrue()
    }

    @DisplayName("저장되어 있지 않은 키워드를 활성화 하면 키워드가 저장후 keywordDto가 반환된다.")
    @Test
    fun givenNotExistKeywordStr_whenActivateKeyword_thenReturnsKeywordDto() {
        //given
        given(keywordRepository.getKeywordByName(any())).willReturn(null)
        given(keywordRepository.save(any())).willReturn(Keyword("코로나"))
        //when
        val keyword = keywordService.activateKeyword("코로나")
        //then
        then(keywordRepository).should().getKeywordByName(any())
        then(keywordRepository).should().save(any())
        then(crawlerStrategy).should().crawlNews("코로나")
        assertThat(keyword).isNotNull
        assertThat(keyword.active).isTrue()
    }

    @DisplayName("저장된 키워드를 비활성화 하면 keyowrdDto가 반환된다.")
    @Test
    fun givenExistKeywordStr_whenDeactivateKeyword_thenReturnsKeywordDto() {
        //given
        given(keywordRepository.getKeywordByName(any())).willReturn(Keyword(name = "코로나"))
        //when
        val keyword = keywordService.deactivateKeyword("코로나")
        //then
        then(keywordRepository).should().getKeywordByName(any())
        assertThat(keyword).isNotNull
        assertThat(keyword.active).isFalse()
    }

    @DisplayName("저장지 않은 키워드를 비활성화 하면 에러가 발생한다.")
    @Test
    fun givenNotExistKeywordStr_whenDeactivateKeyword_thenReturnsKeywordDto() {
        //given
        given(keywordRepository.getKeywordByName(any())).willReturn(null)
        //when
        val exception = assertThrows<CustomException> { keywordService.deactivateKeyword("코로나") }
        //then
        then(keywordRepository).should().getKeywordByName(any())
        assertThat(exception.code).isEqualTo(ResponseStatus.ACCESS_NOT_EXIST_ENTITY.code)
    }

    // add
    private fun <T> any(): T {
        Mockito.any<T>()
        return null as T
    }

}