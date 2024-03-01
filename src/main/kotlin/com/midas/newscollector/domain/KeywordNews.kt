package com.midas.newscollector.domain

import jakarta.persistence.*
import java.io.Serializable

@Entity
@IdClass(KeywordNews.KeywordNewsId::class)
class KeywordNews(
    @Id @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "keyword_id", nullable = false) private val keyword: Keyword,
    @Id @ManyToOne(fetch = FetchType.LAZY) @JoinColumn(name = "news_id", nullable = false) private val news: News
): BaseEntity() {
    data class KeywordNewsId(
        val keyword: Long,
        val news: Long
    ) : Serializable

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as KeywordNews

        if (keyword != other.keyword) return false
        if (news != other.news) return false

        return true
    }

    override fun hashCode(): Int {
        var result = keyword.hashCode()
        result = 31 * result + news.hashCode()
        return result
    }
}