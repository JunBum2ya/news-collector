package com.midas.newscollector.repository;

import com.midas.newscollector.domain.Keyword
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface KeywordRepository : JpaRepository<Keyword, Long> {
    @Query("SELECT k FROM Keyword k WHERE k.active order by k.createdAt DESC")
    fun searchActiveKeywords(): List<Keyword>
    fun getKeywordByName(name: String): Keyword?
}