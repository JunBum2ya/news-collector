package com.midas.newscollector.crawler

class NewsDataCrawlerFactory {
    fun newInstance(type: CrawlerType): NewsDataCrawler {
        return when (type) {
            CrawlerType.GOOGLE -> GoogleNewsDataCrawler()
            CrawlerType.NAVER -> NaverNewsDataCrawler()
            CrawlerType.DAUM -> DaumNewsDataCrawler()
        }
    }

    fun newInstance(types: Set<CrawlerType>): List<NewsDataCrawler> {
        return types.stream().map(this::newInstance).toList()
    }

}