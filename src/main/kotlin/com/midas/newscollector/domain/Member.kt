package com.midas.newscollector.domain

import jakarta.persistence.*
import org.hibernate.annotations.Comment

@Entity
class Member(
    @Column(length = 50, nullable = false, unique = true) @Comment("사용자 아이디") val memberId: String,
    @Column(length = 50) @Comment("이메일") private var email: String?,
    @Column(nullable = false) @Comment("비밀번호") private val password: String
) : BaseEntity() {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "id", nullable = false)
    @Comment("대체키")
    private var id: Long? = null
}