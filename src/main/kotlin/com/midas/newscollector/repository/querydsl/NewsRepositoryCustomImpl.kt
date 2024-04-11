package com.midas.newscollector.repository.querydsl

import com.midas.newscollector.domain.News
import com.midas.newscollector.domain.QNews
import com.midas.newscollector.dto.param.NewsSearchParam
import com.querydsl.core.types.Predicate
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.support.QuerydslRepositorySupport
import org.springframework.stereotype.Repository

@Repository
class NewsRepositoryCustomImpl(val jpaQueryFactory: JPAQueryFactory) : QuerydslRepositorySupport(News::class.java),
    NewsRepositoryCustom {

    /**
     * 뉴스 검색
     */
    override fun searchNews(param: NewsSearchParam, pageable: Pageable): Page<News> {
        val news = QNews.news
        val query = jpaQueryFactory.selectFrom(news).where(buildWhereClause(param))
        val countQuery = jpaQueryFactory.select(news.count()).from(news).where(buildWhereClause(param))
        val articles = this.querydsl!!.applyPagination(pageable,query).fetch()
        return PageImpl(articles,pageable,countQuery.fetch().size.toLong())
    }

    private fun buildWhereClause(param: NewsSearchParam): BooleanExpression? {
        val news = QNews.news
        return param.title?.let { news.title.contains(it) }?.and(param.publisher?.let { news.publisher.contains(it) })
    }
}