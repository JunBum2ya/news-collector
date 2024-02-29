package com.midas.newscollector.domain

import jakarta.persistence.*

@Entity
class Keyword(@Column(unique = true, nullable = false) val keyword: String, var active: Boolean = true) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    var id: Long? = null
        private set
}