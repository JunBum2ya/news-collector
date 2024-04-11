package com.midas.newscollector.service

import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.dto.param.NewsSearchParam
import com.midas.newscollector.dto.request.NewsSearchRequest
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.NewsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

/**
 * 뉴스 비즈니스 로직
 */
@Transactional
@Service
class NewsService(private val newsRepository: NewsRepository) {

    /**
     * 뉴스 검색 메소드
     */
    @Transactional(readOnly = true)
    fun searchNews(param: NewsSearchParam, pageable: Pageable): Page<NewsDto> {
        return newsRepository.searchNews(param, pageable).map(NewsDto::of)
    }

    /**
     * 뉴스 생성 메소드
     */
    fun createNews(newsDto: NewsDto): NewsDto {
        val news = newsRepository.getNewsByUrl(newsDto.url) ?: newsRepository.save(newsDto.toEntity())
        news.update(newsDto)
        return NewsDto.of(news)
    }

    /**
     * 뉴스 수정 메소드
     */
    fun updateNews(newsId: Long, newsDto: NewsDto): NewsDto {
        val news = newsRepository.findById(newsId)
            .orElseThrow { CustomException(ResponseStatus.ACCESS_NOT_EXIST_ENTITY) }
        news.update(newsDto)
        return NewsDto.of(news)
    }

    /**
     * 뉴스 삭제 메소드
     */
    fun deleteNews(newsId: Long) {
        return newsRepository.deleteById(newsId)
    }
}