package com.midas.newscollector.dto.request

import com.midas.newscollector.domain.constant.CompanyType
import jakarta.validation.constraints.NotEmpty

class CompanyRequest(@NotEmpty(message = "뉴스 플랫폼을 입력하세요") var companyName: String?) {
    fun getCompanyType(): CompanyType {
        return CompanyType.valueOf(companyName ?: throw Exception("필수값 미입력"))
    }
}