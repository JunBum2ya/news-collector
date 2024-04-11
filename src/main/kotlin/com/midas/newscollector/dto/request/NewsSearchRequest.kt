package com.midas.newscollector.dto.request

import com.midas.newscollector.dto.param.NewsSearchParam

class NewsSearchRequest(var title: String?, var publisher: String?) {
    fun toNewsSearchParam(): NewsSearchParam {
        return NewsSearchParam(title = title, publisher = publisher)
    }
}