package com.midas.newscollector.domain

import com.midas.newscollector.dto.KeywordDto
import com.midas.newscollector.dto.NewsDto
import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class News(
    @Column(nullable = false, length = 20) @Comment("신문사") private var publisher: String,
    @Column(nullable = false) @Comment("기사 제목") private var title: String,
    @Column(length = 1000) @Comment("썸네일 이미지 경로") private var thumbnailPath: String? = null,
    @Column(nullable = false) @Comment("기사 내용") private var description: String,
    @Column(nullable = false, unique = true, length = 500) @Comment("기사 페이지 링크 주소") private var url: String,
    @OneToMany(mappedBy = "news") val keywords: MutableSet<KeywordNews> = mutableSetOf()
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private var id: Long? = null

    fun getId(): Long? {
        return this.id
    }

    fun getPublisher(): String {
        return this.publisher
    }

    fun getTitle(): String {
        return this.title
    }

    fun getThumbnail(): String? {
        return this.thumbnailPath
    }

    fun getDescription(): String {
        return this.description
    }

    fun getUrl(): String {
        return this.url
    }

    fun update(newsDto: NewsDto) {
        this.publisher = newsDto.publisher
        this.title = newsDto.title
        this.thumbnailPath = newsDto.thumbnailPath
        this.description = newsDto.description
        newsDto.keywords.forEach { this.addKeyword(it.toEntity()) }
    }

    fun addKeyword(keyword: Keyword) {
        val keywordNews = KeywordNews(keyword = keyword, news = this)
        this.keywords.add(keywordNews)
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as News

        return id == other.id
    }

    override fun hashCode(): Int {
        return id?.hashCode() ?: 0
    }

}