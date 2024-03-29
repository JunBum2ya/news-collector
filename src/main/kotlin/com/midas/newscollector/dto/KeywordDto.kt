package com.midas.newscollector.dto

import com.midas.newscollector.domain.Keyword
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.midas.newscollector.domain.Keyword}
 */
data class KeywordDto(
    val name: String,
    val active: Boolean = true,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun toEntity(): Keyword {
        return Keyword(name = name, active = active)
    }

    companion object {
        fun of(keyword: Keyword): KeywordDto {
            return KeywordDto(
                name = keyword.name,
                active = keyword.active,
                createdAt = keyword.createdAt,
                updatedAt = keyword.updatedAt
            )
        }
    }
}