package com.midas.newscollector.domain

import com.midas.newscollector.domain.constant.CompanyType
import jakarta.persistence.*

/**
 * 뉴스 플랫폼 사이트
 */
@Entity
class Company(
    @Id @Column(name = "id", nullable = false) @Enumerated(EnumType.STRING) val id: CompanyType = CompanyType.MANUAL,
    var active: Boolean = true
) : BaseEntity() {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Company

        return id == other.id
    }

    override fun hashCode(): Int {
        return id.hashCode()
    }
}