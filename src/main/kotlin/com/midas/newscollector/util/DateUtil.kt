package com.midas.newscollector.util

import java.time.LocalDateTime
import java.time.ZoneId

class DateUtil {
    companion object {
        private val zoneId = ZoneId.of("Asia/Seoul")
        fun now(): LocalDateTime {
            return LocalDateTime.now(zoneId)
        }
    }
}