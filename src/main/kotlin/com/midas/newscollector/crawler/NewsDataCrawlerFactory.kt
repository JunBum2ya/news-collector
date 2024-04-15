package com.midas.newscollector.crawler

import com.midas.newscollector.domain.constant.CompanyType
import org.springframework.stereotype.Component

@Component
class NewsDataCrawlerFactory {
    fun newInstance(type: CompanyType): NewsDataCrawler {
        return when (type) {
            CompanyType.GOOGLE -> GoogleNewsDataCrawler()
            CompanyType.NAVER -> NaverNewsDataCrawler()
            CompanyType.DAUM -> DaumNewsDataCrawler()
            CompanyType.MANUAL -> ManualNewsDataCrawler()
        }
    }

    fun newInstance(types: Set<CompanyType>): List<NewsDataCrawler> {
        return types.stream().map(this::newInstance).toList()
    }

}