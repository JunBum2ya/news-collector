package com.midas.newscollector.domain

import jakarta.persistence.*

@Entity
class Keyword(
    @Column(unique = true, nullable = false) val name: String,
    var active: Boolean = true,
    @OneToMany(mappedBy = "keyword") val newsSet: MutableSet<KeywordNews> = mutableSetOf()
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    private var id: Long? = null
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Keyword

        return name == other.name
    }

    override fun hashCode(): Int {
        return name.hashCode()
    }
}