package com.midas.newscollector.dto.response

import com.midas.newscollector.dto.NewsDto
import java.time.LocalDateTime

data class NewsResponse(
    val newsId: Long?,
    val publisher: String,
    val title: String,
    val thumbnailPath: String?,
    val description: String,
    val url: String,
    val keywords: List<String>
) {
    companion object {
        fun from(newsDto: NewsDto): NewsResponse {
            return NewsResponse(
                newsId = newsDto.newsId,
                publisher = newsDto.publisher,
                title = newsDto.title,
                thumbnailPath = newsDto.thumbnailPath,
                description = newsDto.description,
                url = newsDto.description,
                keywords = newsDto.keywords.map { it.name }.toList()
            )
        }
    }
}
