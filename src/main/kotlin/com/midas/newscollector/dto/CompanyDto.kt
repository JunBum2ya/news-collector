package com.midas.newscollector.dto

import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.midas.newscollector.domain.Company}
 */
data class CompanyDto(
    val companyType: CompanyType = CompanyType.MANUAL,
    val active: Boolean = true,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    fun toEntity(): Company {
        return Company(id = companyType, active = active)
    }

    companion object {
        fun from(company: Company): CompanyDto {
            return CompanyDto(
                companyType = company.id,
                active = company.active,
                createdAt = company.getCreatedAt(),
                updatedAt = company.getUpdatedAt()
            )
        }
    }
}