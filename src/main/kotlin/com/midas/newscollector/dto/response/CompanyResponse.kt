package com.midas.newscollector.dto.response

import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.util.DateUtil
import java.io.Serializable
import java.time.LocalDateTime

data class CompanyResponse(
    val companyName: String,
    val isActive: Boolean,
    val createdAt: LocalDateTime,
    val updatedAt: LocalDateTime
) : Serializable {
    companion object {
        fun of(companyDto: CompanyDto): CompanyResponse {
            return CompanyResponse(
                companyName = companyDto.companyType.name,
                isActive = companyDto.active,
                createdAt = companyDto.createdAt ?: DateUtil.now(),
                updatedAt = companyDto.updatedAt ?: DateUtil.now()
            )
        }
    }
}
