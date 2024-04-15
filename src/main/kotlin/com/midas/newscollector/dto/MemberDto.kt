package com.midas.newscollector.dto

import com.midas.newscollector.domain.Member
import org.springframework.security.crypto.password.PasswordEncoder
import java.io.Serializable
import java.time.LocalDateTime

/**
 * DTO for {@link com.midas.newscollector.domain.Member}
 */
data class MemberDto(
    val memberId: String,
    val email: String,
    val password: String,
    val createdAt: LocalDateTime? = null,
    val updatedAt: LocalDateTime? = null
) {
    fun toEntity(passwordEncoder: PasswordEncoder): Member {
        return Member(memberId = memberId, email = email, password = passwordEncoder.encode(password))
    }

    companion object {
        fun from(member: Member): MemberDto {
            return MemberDto(
                memberId = member.memberId,
                email = member.getEmail(),
                password = member.getPassword(),
                createdAt = member.getCreatedAt(),
                updatedAt = member.getUpdatedAt()
            )
        }
    }
}