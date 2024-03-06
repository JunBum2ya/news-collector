package com.midas.newscollector.service

import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.dto.request.NewsSearchRequest
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.NewsRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class NewsService(private val newsRepository: NewsRepository) {
    @Transactional(readOnly = true)
    fun searchNews(request: NewsSearchRequest, pageable: Pageable): Page<NewsDto> {
        return newsRepository.findAll(pageable).map(NewsDto::of)
    }

    fun createNews(newsDto: NewsDto): NewsDto {
        val news = newsRepository.getNewsByUrl(newsDto.url) ?: newsRepository.save(newsDto.toEntity())
        news.update(newsDto)
        return NewsDto.of(news)
    }

    fun updateNews(newsId: Long, newsDto: NewsDto): NewsDto {
        val news = newsRepository.findById(newsId)
            .orElseThrow { CustomException(ResponseStatus.ACCESS_NOT_EXIST_ENTITY) }
        news.update(newsDto)
        return NewsDto.of(news)
    }

    fun deleteNews(newsId: Long) {
        return newsRepository.deleteById(newsId)
    }
}