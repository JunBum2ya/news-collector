package com.midas.newscollector.domain

import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class News(
    @Column(nullable = false, length = 20) @Comment("신문사") private var publisher: String,
    @Column(nullable = false) @Comment("기사 제목") private var title: String,
    @Column(length = 1000) @Comment("썸네일 이미지 경로") var thumbnailPath: String? = null,
    @Column(nullable = false) @Comment("기사 내용") var description: String,
    @Column(nullable = false, unique = true, length = 500) @Comment("기사 페이지 링크 주소") var url: String,
    @OneToMany(mappedBy = "news") val keywords: MutableSet<KeywordNews> = mutableSetOf()
): BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private var id: Long? = null

    fun getPublisher(): String {
        return this.publisher
    }
    fun getTitle(): String {
        return this.title
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