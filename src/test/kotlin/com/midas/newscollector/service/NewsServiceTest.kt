package com.midas.newscollector.service

import com.midas.newscollector.domain.News
import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.dto.param.NewsSearchParam
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.NewsRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.findByIdOrNull

class NewsServiceTest : BehaviorSpec({

    val newsRepository = mockk<NewsRepository>()
    val newsService = NewsService(newsRepository)

    Given("파라미터와 페이지네이션으로") {
        val param = NewsSearchParam(title = "Test", publisher = "Test")
        val pageable = PageRequest.of(0, 10)
        every { newsRepository.searchNews(any(NewsSearchParam::class), any(Pageable::class)) }
            .returns(PageImpl(listOf(createNews()), pageable, 1))
        When("뉴스를 조회하면") {
            val page = newsService.searchNews(param, pageable)
            Then("뉴스 데이터의 페이지가 반환된다.") {
                page.totalPages shouldBe 1
                page.size shouldBe 10
                page.totalElements shouldBe 1
                verify { newsRepository.searchNews(any(NewsSearchParam::class), any(Pageable::class)) }
            }
        }
    }
    Given("NewsDto가 주어지면") {
        val newsDto = NewsDto(publisher = "new", title = "new", description = "new", url = "new.com")
        every { newsRepository.save(any(News::class)) }.returns(newsDto.toEntity())
        When("중복된 url이 없이 저장된다면") {
            every { newsRepository.getNewsByUrl(any(String::class)) }.returns(null)
            val news = newsService.createNews(newsDto)
            Then("저장 후 NewsDto를 반환한다.") {
                news.title shouldBe newsDto.title
                news.publisher shouldBe newsDto.publisher
                news.description shouldBe newsDto.description
                news.url shouldBe newsDto.url
                verify { newsRepository.save(any(News::class)) }
                verify { newsRepository.getNewsByUrl(any(String::class)) }
            }
        }
        When("중복된 URL을 저장한다면") {
            every { newsRepository.getNewsByUrl(any(String::class)) }.returns(createNews())
            val news = newsService.createNews(newsDto)
            Then("NewsDto를 반환한다.") {
                news.title shouldNotBe newsDto.title
                news.publisher shouldNotBe newsDto.publisher
                news.description shouldNotBe newsDto.description
                news.url shouldNotBe newsDto.url
                verify { newsRepository.getNewsByUrl(any(String::class)) }
            }
        }
    }
    Given("아이디와 NewsDto가 주어지면") {
        val newsId = 1L
        val newsDto = NewsDto(publisher = "new", title = "new", description = "new", url = "new.com")
        When("아이디가 이미 저장되어 있다면 수정을 한다.") {
            every { newsRepository.findByIdOrNull(any(Long::class)) }.returns(createNews())
            val news = newsService.updateNews(newsId,newsDto)
            Then("수정 후 NewsDto를 반환한다.") {
                news.title shouldBe newsDto.title
                news.publisher shouldBe newsDto.publisher
                news.description shouldBe newsDto.description
                news.url shouldBe "test.com"
                verify { newsRepository.findByIdOrNull(any(Long::class)) }
            }
        }
        When("저장되지 않았다면") {
            every { newsRepository.findByIdOrNull(any(Long::class)) }.returns(null)
            Then("예외가 발생한다.") {
                val exception = shouldThrow<CustomException> { newsService.updateNews(newsId,newsDto) }
                exception.code shouldBe ResponseStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception shouldHaveMessage CustomException(ResponseStatus.ACCESS_NOT_EXIST_ENTITY).message!!
                verify { newsRepository.findByIdOrNull(any(Long::class)) }
            }
        }
    }
    Given("아이디가 주어졌을 때") {
        val newsId = 1L
        every { newsRepository.deleteById(any(Long::class)) }.returns(Unit)
        When("삭제를 한다면") {
            val news = newsService.deleteNews(newsId)
            Then("아무것도 하지 않는다.") {
                verify { newsRepository.deleteById(any(Long::class)) }
            }
        }
    }
}) {
    companion object {
        fun createNews(): News {
            return News(publisher = "Test", title = "Test", description = "Test", url = "test.com")
        }
    }
}