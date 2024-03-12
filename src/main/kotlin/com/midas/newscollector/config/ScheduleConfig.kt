package com.midas.newscollector.config

import com.midas.newscollector.crawler.NewsDataCrawlerStrategy
import com.midas.newscollector.dto.NewsDto
import com.midas.newscollector.service.KeywordService
import org.springframework.context.annotation.Configuration
import org.springframework.scheduling.annotation.EnableScheduling
import org.springframework.scheduling.annotation.Scheduled
import java.io.IOException

@EnableScheduling
@Configuration
class ScheduleConfig(
    private val keywordService: KeywordService,
    private val newsDataCrawlerStrategy: NewsDataCrawlerStrategy
) {
    /**
     * 매 정각 마다 크롤링 실행
     * @throws IOException
     */
    @Scheduled(cron = "0 0 * * * *")
    @Throws(IOException::class)
    fun crawlingNewsData(): List<NewsDto> {
        val keywords: List<String> = keywordService.getActiveKeywords().stream().map { it.name }.toList()
        return newsDataCrawlerStrategy.crawlNews(keywords)
    }
}