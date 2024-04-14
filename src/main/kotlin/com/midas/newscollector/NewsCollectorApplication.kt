package com.midas.newscollector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@ConfigurationPropertiesScan
@SpringBootApplication
class NewsCollectorApplication

fun main(args: Array<String>) {
    runApplication<NewsCollectorApplication>(*args)
}
