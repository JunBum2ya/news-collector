package com.midas.newscollector

import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("testdb")
class NewsCollectorApplicationTests {

    @Test
    fun contextLoads() {
    }

}
