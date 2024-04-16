package com.midas.newscollector.dto

import com.midas.newscollector.domain.Keyword
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.midas.newscollector.domain.Keyword}
 */
data class KeywordDto(
    val keywordId: Long? = null,
    val name: String,
    val active: Boolean = true,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun toEntity(): Keyword {
        return Keyword(id = keywordId, name = name, active = active)
    }

    companion object {
        fun of(keyword: Keyword): KeywordDto {
            return KeywordDto(
                keywordId = keyword.getId(),
                name = keyword.name,
                active = keyword.active,
                createdAt = keyword.getCreatedAt(),
                updatedAt = keyword.getUpdatedAt()
            )
        }
    }
}