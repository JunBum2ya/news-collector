package com.midas.newscollector.domain

import jakarta.persistence.Column
import jakarta.persistence.EntityListeners
import jakarta.persistence.MappedSuperclass
import org.hibernate.annotations.Comment
import org.springframework.data.annotation.CreatedDate
import org.springframework.data.annotation.LastModifiedDate
import org.springframework.data.jpa.domain.support.AuditingEntityListener
import java.time.LocalDateTime

/**
 * 생성일시, 수정일시
 */
@MappedSuperclass
@EntityListeners(AuditingEntityListener::class)
abstract class BaseEntity {
    @CreatedDate
    @Column(nullable = false, updatable = false)
    @Comment("생성일시")
    private var createdAt: LocalDateTime? = null
    @LastModifiedDate
    @Comment("수정일시")
    private var updatedAt: LocalDateTime? = null

    fun getCreatedAt(): LocalDateTime? {
        return this.createdAt
    }

    fun getUpdatedAt(): LocalDateTime? {
        return this.updatedAt
    }
}