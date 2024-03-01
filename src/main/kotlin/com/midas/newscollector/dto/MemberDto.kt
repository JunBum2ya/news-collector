package com.midas.newscollector.dto

import com.midas.newscollector.domain.Member
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.midas.newscollector.domain.Member}
 */
data class MemberDto(
    val memberId: String,
    val email: String? = null,
    val password: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun toEntity(): MemberDto {
        return MemberDto(memberId = memberId, email = email, password = password)
    }

    companion object {
        fun of(member: Member): MemberDto {
            return MemberDto(
                memberId = member.memberId,
                email = member.getEmail(),
                password = member.getPassword(),
                createdAt = member.createdAt,
                updatedAt = member.updatedAt
            )
        }
    }
}