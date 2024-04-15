package com.midas.newscollector.dto

import org.springframework.security.core.GrantedAuthority
import org.springframework.security.core.userdetails.UserDetails
import java.time.LocalDateTime

data class MemberPrincipal(
    val memberId: String,
    private val password: String,
    private val authorities: MutableList<GrantedAuthority>,
    val email: String,
    val createdAt: LocalDateTime?,
    val updatedAt: LocalDateTime?
) : UserDetails {

    override fun getAuthorities(): MutableCollection<out GrantedAuthority> {
        return this.authorities
    }

    override fun getPassword(): String {
        return this.password
    }

    override fun getUsername(): String {
        return this.password
    }

    override fun isAccountNonExpired(): Boolean {
        return true
    }

    override fun isAccountNonLocked(): Boolean {
        return true
    }

    override fun isCredentialsNonExpired(): Boolean {
        return true
    }

    override fun isEnabled(): Boolean {
        return true
    }

    companion object {
        fun from(memberDto: MemberDto): MemberPrincipal {
            return MemberPrincipal(
                memberId = memberDto.memberId,
                password = memberDto.password,
                email = memberDto.email,
                authorities = mutableListOf(),
                createdAt = memberDto.createdAt,
                updatedAt = memberDto.updatedAt
            )
        }
    }

}
