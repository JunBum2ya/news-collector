package com.midas.newscollector.service

import com.midas.newscollector.domain.Member
import com.midas.newscollector.dto.MemberDto
import com.midas.newscollector.repository.MemberRepository
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.crypto.password.PasswordEncoder

class MemberServiceTest: BehaviorSpec({

    val memberRepository = mockk<MemberRepository>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val memberService = MemberService(memberRepository)

    Given("memberId가 주어지면") {
        val member = Member(id = 1L, memberId = "test", password = "test", email = "test@test.com")
        every { memberRepository.getMemberByMemberId(any(String::class)) }.returns(member)
        When("member를 조회하면") {
            val memberDto = memberService.getMember("test")
            Then("MemberDto가 반환된다.") {
                memberDto shouldNotBe null
                memberDto?.memberId shouldBe "test"
                memberDto?.password shouldBe "test"
                memberDto?.email shouldBe "test@test.com"
                verify { memberRepository.getMemberByMemberId(any(String::class)) }
            }
        }
    }
    Given("MemberDto와 PasswordEncoder가 주어지면") {
        val memberDto = MemberDto(memberId = "test", password = "test", email = "test@test.com")
        every { memberRepository.save(any(Member::class)) }.returns(Member(id = 1L, memberId = "test", password = "test", email = "test@test.com"))
        every { passwordEncoder.encode(any(CharSequence::class))}.returns("new-password")
        When("member를 저장하면") {
            val newMember = memberService.registerMember(memberDto,passwordEncoder)
            Then("MemberDto가 반환된다.") {
                newMember.memberId shouldBe "test"
                newMember.password shouldBe "test"
                newMember.email shouldBe "test@test.com"
                verify { memberRepository.save(any(Member::class)) }
                verify { passwordEncoder.encode(any(CharSequence::class)) }
            }
        }
    }

}) {
}