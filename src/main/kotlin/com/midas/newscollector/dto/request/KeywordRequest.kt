package com.midas.newscollector.dto.request

import jakarta.validation.constraints.NotEmpty

class KeywordRequest(@NotEmpty(message = "키워드를 입력하세요") var keyword: String?)