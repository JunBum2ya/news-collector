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
) : BaseEntity()