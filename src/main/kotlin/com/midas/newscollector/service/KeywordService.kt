package com.midas.newscollector.service

import com.midas.newscollector.crawler.NewsDataCrawlerStrategy
import com.midas.newscollector.domain.Keyword
import com.midas.newscollector.dto.KeywordDto
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.KeywordRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.Throws

@Transactional
@Service
class KeywordService(private val keywordRepository: KeywordRepository, private val newsCrawlerStrategy: NewsDataCrawlerStrategy) {

    /**
     * 활성화된 키워드 반환
     */
    @Transactional(readOnly = true)
    fun getActiveKeywords(): List<KeywordDto> {
        return keywordRepository.searchActiveKeywords().stream().map(KeywordDto::of).toList()
    }

    /**
     * 키워드 활성화
     */
    fun activateKeyword(keywordStr: String): KeywordDto {
        val keyword = keywordRepository.getKeywordByName(keywordStr)
            ?: keywordRepository.save(Keyword(name = keywordStr))
        keyword.active = true //활성화
        newsCrawlerStrategy.crawlNews(keywordStr) //크롤링 실행
        return KeywordDto.of(keyword)
    }

    /**
     * 키워드 비활성화
     */
    @Throws(CustomException::class)
    fun deactivateKeyword(keywordStr: String): KeywordDto {
        val keyword = keywordRepository.getKeywordByName(keywordStr)
            ?: throw CustomException(ResponseStatus.ACCESS_NOT_EXIST_ENTITY)
        keyword.active = false
        return KeywordDto.of(keyword)
    }

}