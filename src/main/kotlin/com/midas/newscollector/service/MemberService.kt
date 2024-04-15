package com.midas.newscollector.service

import com.midas.newscollector.dto.MemberDto
import com.midas.newscollector.repository.MemberRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Transactional
@Service
class MemberService(private val memberRepository: MemberRepository) {

    @Transactional(readOnly = true)
    fun getMember(memberId: String): MemberDto? {
        return memberRepository.getMemberByMemberId(memberId)?.let { MemberDto.from(it) }
    }

    fun registerMember(memberDto: MemberDto, passwordEncoder: PasswordEncoder): MemberDto {
        val member = memberRepository.save(memberDto.toEntity(passwordEncoder))
        return MemberDto.from(member)
    }

}