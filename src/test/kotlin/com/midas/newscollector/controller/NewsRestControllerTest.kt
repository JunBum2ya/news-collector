package com.midas.newscollector.controller

import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.dto.param.NewsSearchParam
import com.midas.newscollector.dto.request.NewsSearchRequest
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.service.NewsService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableHandlerMethodArgumentResolver
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class NewsRestControllerTest : DescribeSpec({
    val newsService = mockk<NewsService>()
    val newsRestController = NewsRestController(newsService)

    val mvc = MockMvcBuilders.standaloneSetup(newsRestController)
        .setCustomArgumentResolvers(PageableHandlerMethodArgumentResolver())
        .build()

    describe("[view][GET] news 검색") {
        val request = NewsSearchRequest(title = "test", publisher = "test")
        val pageable = PageRequest.of(0, 10)
        every { newsService.searchNews(any(NewsSearchParam::class), any(Pageable::class)) }
            .returns(PageImpl(listOf(createNews()),pageable,1))
        it("정상 호출") {
            mvc.perform(get("/news")
                .param("title",request.title)
                .param("publisher", request.publisher))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.data.content").isArray)
            verify { newsService.searchNews(any(NewsSearchParam::class),any(Pageable::class)) }
        }
    }
    describe("[view][DELETE] news 삭제") {
        val newsId = 1L
        every { newsService.deleteNews(any(Long::class)) }.returns(Unit)
        it("정상 호출") {
            mvc.perform(delete("/news/${newsId}"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
            verify { newsService.deleteNews(newsId) }
        }
    }
}) {
    companion object {
        fun createNews(): NewsDto {
            return NewsDto(newsId = 1, title = "test", publisher = "test", description = "test", url = "test")
        }
    }
}