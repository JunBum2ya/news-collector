package com.midas.newscollector.repository

import com.midas.newscollector.config.JpaConfig
import com.midas.newscollector.domain.Keyword
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - Keyword")
@DataJpaTest
@Import(JpaConfig::class)
@ActiveProfiles("testdb")
class KeywordRepositoryTest(@Autowired private val keywordRepository: KeywordRepository) {

    @DisplayName("Keyword를 저장하면 keyword가 반환된다.")
    @Test
    fun givenKeyword_whenSaveKeyword_thenReturnsKeyword() {
        //given
        val keyword = Keyword(name = "코로나")
        //when
        val savedKeyword: Keyword = keywordRepository.save(keyword)
        //then
        assertThat(savedKeyword).isNotNull()
        assertThat(savedKeyword.name).isEqualTo("코로나")
        assertThat(savedKeyword.createdAt).isNotNull()
        assertThat(savedKeyword.updatedAt).isNotNull()
    }

    @DisplayName("활성화된 키워드를 조회하면 키워드 리스트가 반환된다.")
    @Test
    fun givenSaveKeywords_whenSearchActiveKeywords_thenReturnsKeywordList() {
        //given
        createKeyword("코로나", true)
        createKeyword("선거", false)
        //when
        val keywords = keywordRepository.searchActiveKeywords()
        //then
        assertThat(keywords).isNotEmpty()
        assertThat(keywords.size).isEqualTo(1)
    }

    @DisplayName("키워드를 조회하면 keyword가 반환된다.")
    @Test
    fun givenKeywordName_whenFindKeyword_thenReturnsKeyword() {
        //given
        createKeyword("코로나", true)
        //when
        val keyword = keywordRepository.getKeywordByName("코로나")
        //then
        assertThat(keyword).isNotNull
        assertThat(keyword?.name).isEqualTo("코로나")
        assertThat(keyword?.active).isTrue()
    }

    private fun createKeyword(name: String, isActive: Boolean = true): Keyword {
        val keyword = Keyword(name = name, active = isActive)
        return keywordRepository.save(keyword)
    }

}