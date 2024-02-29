package com.midas.newscollector

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class NewsCollectorApplication

fun main(args: Array<String>) {
    runApplication<NewsCollectorApplication>(*args)
}
