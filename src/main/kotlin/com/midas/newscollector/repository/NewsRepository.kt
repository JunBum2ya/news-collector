package com.midas.newscollector.repository;

import com.midas.newscollector.domain.News
import com.midas.newscollector.repository.querydsl.NewsRepositoryCustom
import org.springframework.data.jpa.repository.JpaRepository

interface NewsRepository : JpaRepository<News, Long>,NewsRepositoryCustom {
    fun getNewsByUrl(url: String): News?
}