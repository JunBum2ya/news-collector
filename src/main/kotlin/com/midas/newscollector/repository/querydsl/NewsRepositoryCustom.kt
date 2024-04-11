package com.midas.newscollector.repository.querydsl

import com.midas.newscollector.domain.News
import com.midas.newscollector.dto.param.NewsSearchParam
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable

interface NewsRepositoryCustom {
    fun searchNews(param: NewsSearchParam, pageable: Pageable): Page<News>
}