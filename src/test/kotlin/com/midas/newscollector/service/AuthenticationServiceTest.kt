package com.midas.newscollector.service

import com.midas.newscollector.component.TokenProvider
import com.midas.newscollector.dto.MemberDto
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.crypto.password.PasswordEncoder

class AuthenticationServiceTest : BehaviorSpec({

    val memberService = mockk<MemberService>()
    val tokenProvider = mockk<TokenProvider>()
    val passwordEncoder = mockk<PasswordEncoder>()
    val authenticationService = AuthenticationService(memberService, tokenProvider, passwordEncoder)

    Given("memberDto가 주어졌을 때") {
        val memberDto = MemberDto(memberId = "test", password = "test", email = "test@test.com")
        every { tokenProvider.createAccessToken(any(Authentication::class)) }.returns("access-token")
        every { tokenProvider.createRefreshToken(any(Authentication::class)) }.returns("refresh-token")
        every { memberService.registerMember(any(MemberDto::class), any(PasswordEncoder::class)) }.returns(memberDto)
        When("token을 정상적으로 발급받으면") {
            every { memberService.getMember(any(String::class)) }.returns(memberDto)
            every { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }.returns(true)
            val token = authenticationService.issueRefreshToken(memberDto)
            Then("리프레시 토큰과 jwt 토큰을 발급 받는다.") {
                token shouldNotBe null
                token.accessToken shouldBe "access-token"
                token.refreshToken shouldBe "refresh-token"
                token.userId shouldBe "test"
                verify { tokenProvider.createAccessToken(any(Authentication::class)) }
                verify { tokenProvider.createRefreshToken(any(Authentication::class)) }
                verify { memberService.getMember(any(String::class)) }
                verify { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }
            }
        }
        When("등록되지 않은 사용자일 경우") {
            every { memberService.getMember(any(String::class)) }.returns(null)
            val exception = shouldThrow<CustomException> { authenticationService.issueRefreshToken(memberDto) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResponseStatus.UNAUTHENTICATED_USER.code
                exception.message shouldBe "유저가 없습니다."
                verify { memberService.getMember(any(String::class)) }
            }
        }
        When("패스워드가 일치하지 않을 경우") {
            every { memberService.getMember(any(String::class)) }.returns(memberDto)
            every { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }.returns(false)
            val exception = shouldThrow<CustomException> { authenticationService.issueRefreshToken(memberDto) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResponseStatus.UNAUTHENTICATED_USER.code
                exception.message shouldBe "패스워드를 확인하십시오."
                verify { memberService.getMember(any(String::class)) }
                verify { passwordEncoder.matches(any(CharSequence::class), any(String::class)) }
            }
        }
        When("새로운 사용자를 등록했다면") {
            val token = authenticationService.registerMember(memberDto)
            Then("토큰을 발급받는다.") {
                token shouldNotBe null
                token.accessToken shouldBe "access-token"
                token.refreshToken shouldBe "refresh-token"
                token.userId shouldBe "test"
                verify { memberService.registerMember(any(MemberDto::class), any(PasswordEncoder::class)) }
                verify { tokenProvider.createAccessToken(any(Authentication::class)) }
                verify { tokenProvider.createRefreshToken(any(Authentication::class)) }
            }
        }
    }
    Given("리프레시 토큰을 입력받아") {
        val refreshToken = "refresh-token"
        every { tokenProvider.getAuthentication(any(String::class)) }
            .returns(UsernamePasswordAuthenticationToken(null,null))
        every { tokenProvider.createAccessToken(any(Authentication::class)) }.returns("access-token")
        When("토큰 발급에 성공하였다면") {
            every { tokenProvider.validateToken(any(String::class)) }.returns(true)
            val token = authenticationService.issueAccessToken(refreshToken)
            Then("jwt 토큰을 발급받는다.") {
                token shouldBe "access-token"
                verify { tokenProvider.validateToken(any(String::class)) }
                verify { tokenProvider.getAuthentication(any(String::class)) }
                verify { tokenProvider.createAccessToken(any(Authentication::class)) }
            }
        }
        When("리프레시 토큰의 유효성 검사에 실패하였을 경우") {
            every { tokenProvider.validateToken(any(String::class)) }.returns(false)
            val exception = shouldThrow<CustomException> { authenticationService.issueAccessToken(refreshToken) }
            Then("예외가 발생한다.") {
                exception.code shouldBe ResponseStatus.UNAUTHENTICATED_USER.code
                exception.message shouldBe ResponseStatus.UNAUTHENTICATED_USER.message
                verify { tokenProvider.validateToken(any(String::class)) }
            }
        }
    }

})