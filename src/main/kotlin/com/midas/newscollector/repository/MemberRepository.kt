package com.midas.newscollector.repository;

import com.midas.newscollector.domain.Member
import org.springframework.data.jpa.repository.JpaRepository

interface MemberRepository : JpaRepository<Member, Long> {
    fun getMemberByMemberId(memberId: String): Member?
}