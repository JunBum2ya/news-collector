package com.midas.newscollector.controller

import com.midas.newscollector.dto.request.NewsSearchRequest
import com.midas.newscollector.dto.response.CommonResponse
import com.midas.newscollector.dto.response.NewsResponse
import com.midas.newscollector.service.NewsService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.domain.Sort
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/news")
@RestController
class NewsRestController(private val newsService: NewsService) {

    /**
     * news 검색 엔드포인트
     */
    @GetMapping
    fun searchNewsPage(
        request: NewsSearchRequest,
        @PageableDefault(size = 10, sort = ["createdAt"], direction = Sort.Direction.DESC) pageable: Pageable
    ): ResponseEntity<CommonResponse<Page<NewsResponse>>> {
        val page = newsService.searchNews(request.toNewsSearchParam(), pageable)
        return ResponseEntity.ok(CommonResponse.of(page.map { NewsResponse.from(it) }))
    }

}