package com.midas.newscollector.service

import com.midas.newscollector.crawler.NewsDataCrawlerStrategy
import com.midas.newscollector.domain.Keyword
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.KeywordRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify

class KeywordServiceTest : BehaviorSpec({
    val keywordRepository = mockk<KeywordRepository>()
    val crawlerStrategy = mockk<NewsDataCrawlerStrategy>()
    val keywordService = KeywordService(keywordRepository, crawlerStrategy)

    Given("아무것도 주어지지 않았을 때") {
        every { keywordRepository.searchActiveKeywords() }.returns(listOf(Keyword("선거"), Keyword(name = "코로나")))
        When("활성화된 키워드를 조회하면") {
            val keywords = keywordService.getActiveKeywords()
            Then("키워드 리스트가 반환된다.") {
                keywords shouldHaveSize 2
                verify { keywordRepository.searchActiveKeywords() }
            }
        }
    }
    Given("키워드 이름으로") {
        every { crawlerStrategy.crawlNews(any(String::class)) }.returns(listOf())
        every { keywordRepository.save(any(Keyword::class)) }.returns(Keyword("코로나", false))
        When("이미 저장되어 있는 키워드를 활성화 하면") {
            every { keywordRepository.getKeywordByName(any(String::class)) }.returns(Keyword("코로나", active = false))
            val keyword = keywordService.activateKeyword("코로나")
            Then("수정 후 KeywordDto가 반환된다.") {
                keyword shouldNotBe null
                keyword.active shouldBe true
                verify { keywordRepository.getKeywordByName(any(String::class)) }
                verify { crawlerStrategy.crawlNews(any(String::class)) }
            }
        }
        When("저장되지 않은 키워드를 활성화 하면") {
            every { keywordRepository.getKeywordByName(any(String::class)) }.returns(null)
            val keyword = keywordService.activateKeyword("코로나")
            Then("저장 후 KeywordDto가 반환된다.") {
                keyword shouldNotBe null
                keyword.active shouldBe true
                verify { keywordRepository.getKeywordByName(any(String::class)) }
                verify { keywordRepository.save(any(Keyword::class)) }
                verify { crawlerStrategy.crawlNews(any(String::class)) }
            }
        }
    }
    Given("키워드 이름으로") {
        val keywordName = "코로나"
        When("이미 저장된 키워드를 비활성화 하면") {
            every { keywordRepository.getKeywordByName(any(String::class)) }.returns(Keyword(keywordName, true))
            val keyword = keywordService.deactivateKeyword(keywordName)
            Then("수정 후 KeywordDto가 반환된다.") {
                keyword shouldNotBe null
                keyword.active shouldBe false
                verify { keywordRepository.getKeywordByName(any(String::class)) }
            }
        }
        When("저장되지 않은 키워드를 비활성화 하면") {
            every { keywordRepository.getKeywordByName(any(String::class)) }.returns(null)
            Then("예외가 발생한다.") {
                val exception = shouldThrow<CustomException> { keywordService.deactivateKeyword(keywordName) }
                exception.code shouldBe ResponseStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception shouldHaveMessage ResponseStatus.ACCESS_NOT_EXIST_ENTITY.message
                verify { keywordRepository.getKeywordByName(any(String::class)) }
            }
        }
    }
})