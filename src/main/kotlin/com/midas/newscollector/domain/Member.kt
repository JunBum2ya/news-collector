package com.midas.newscollector.domain

import com.midas.newscollector.dto.MemberDto
import jakarta.persistence.*
import org.hibernate.annotations.Comment
import org.springframework.security.crypto.password.PasswordEncoder

@Entity
class Member(
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    @Comment("대체키")
    private var id: Long? = null,
    @Column(length = 50, nullable = false, unique = true)
    @Comment("사용자 아이디")
    val memberId: String,
    @Column(length = 50)
    @Comment("이메일")
    private var email: String,
    @Column(nullable = false)
    @Comment("비밀번호")
    private var password: String
) : BaseEntity() {
    /**
     * Member update
     */
    fun update(data: MemberDto, passwordEncoder: PasswordEncoder? = null) {
        data.email.let { email = it }
        passwordEncoder?.let { password = it.encode(data.password) }
    }

    fun getEmail(): String {
        return this.email
    }

    fun getPassword(): String {
        return this.password
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Member

        return memberId == other.memberId
    }

    override fun hashCode(): Int {
        return memberId.hashCode()
    }

}