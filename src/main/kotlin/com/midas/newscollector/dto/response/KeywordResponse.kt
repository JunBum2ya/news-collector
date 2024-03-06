package com.midas.newscollector.dto.response

import com.midas.newscollector.dto.KeywordDto
import com.midas.newscollector.util.DateUtil
import java.time.LocalDateTime

data class KeywordResponse(
    val keyword: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) {
    companion object {
        fun of(keywordDto: KeywordDto): KeywordResponse {
            return KeywordResponse(
                keyword = keywordDto.name,
                isActive = keywordDto.active,
                createdAt = keywordDto.createdAt ?: DateUtil.now(),
                updatedAt = keywordDto.updatedAt ?: DateUtil.now()
            )
        }
    }
}