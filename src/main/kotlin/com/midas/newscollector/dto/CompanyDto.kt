package com.midas.newscollector.dto

import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.midas.newscollector.domain.Company}
 */
data class CompanyDto(
    val id: CompanyType = CompanyType.MANUAL,
    val active: Boolean = true,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null,
) {
    fun toEntity(): Company {
        return Company(id = id, active = active)
    }

    companion object {
        fun of(company: Company): CompanyDto {
            return CompanyDto(
                id = company.id,
                active = company.active,
                createdAt = company.createdAt,
                updatedAt = company.updatedAt
            )
        }
    }
}