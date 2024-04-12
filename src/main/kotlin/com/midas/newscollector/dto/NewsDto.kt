package com.midas.newscollector.dto

import com.midas.newscollector.domain.News
import java.util.stream.Collectors

/**
 * DTO for {@link com.midas.newscollector.domain.News}
 */
data class NewsDto(
    val publisher: String,
    val title: String,
    val thumbnailPath: String? = null,
    val description: String,
    val url: String,
    val keywords: MutableSet<KeywordDto> = mutableSetOf()
) {
    fun toEntity(): News {
        val news = News(
            publisher = publisher,
            title = title,
            thumbnailPath = thumbnailPath,
            description = description,
            url = url
        )
        keywords.stream().map { it.toEntity() }.forEach { news.addKeyword(it) }
        return news
    }

    companion object {
        fun from(news: News): NewsDto {
            return NewsDto(
                publisher = news.getPublisher(),
                title = news.getTitle(),
                thumbnailPath = news.getThumbnail(),
                description = news.getDescription(),
                url = news.getUrl(),
                keywords = news.keywords.stream().map { KeywordDto.of(it.keyword) }.collect(Collectors.toSet())
            )
        }
    }
}