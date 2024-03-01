package com.midas.newscollector.repository;

import com.midas.newscollector.domain.News
import org.springframework.data.jpa.repository.JpaRepository

interface NewsRepository : JpaRepository<News, Long> {
}