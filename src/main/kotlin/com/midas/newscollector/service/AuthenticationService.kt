package com.midas.newscollector.service

import com.midas.newscollector.component.TokenProvider
import com.midas.newscollector.dto.MemberDto
import com.midas.newscollector.dto.MemberPrincipal
import com.midas.newscollector.dto.TokenDto
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import org.springframework.http.HttpStatus
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthenticationService(
    private val memberService: MemberService,
    private val tokenProvider: TokenProvider,
    private val passwordEncoder: PasswordEncoder
) {

    @Throws(CustomException::class)
    fun issueRefreshToken(memberDto: MemberDto): TokenDto {
        val member = memberService.getMember(memberDto.memberId)
            ?: throw CustomException(ResponseStatus.UNAUTHENTICATED_USER, "유저가 없습니다.")
        if (passwordEncoder.matches(memberDto.password, member.password)) {
            return createRefreshToken(member)
        } else {
            throw CustomException(ResponseStatus.UNAUTHENTICATED_USER, "패스워드를 확인하십시오.")
        }
    }

    /**
     * member 등록 후 token 발급
     */
    fun registerMember(memberDto: MemberDto): TokenDto {
        val member = memberService.registerMember(memberDto, passwordEncoder)
        return createRefreshToken(member)
    }

    private fun createRefreshToken(memberDto: MemberDto): TokenDto {
        val principal = MemberPrincipal.from(memberDto)
        val authentication =
            UsernamePasswordAuthenticationToken(principal, principal.password, principal.authorities)
        val refreshToken = tokenProvider.createRefreshToken(authentication)
        val accessToken = tokenProvider.createAccessToken(authentication)
        return TokenDto(userId = principal.memberId, refreshToken = refreshToken, accessToken = accessToken)
    }

    /**
     * refresh token 발급
     */
    @Throws(CustomException::class)
    fun issueAccessToken(refreshToken: String): String {
        if (tokenProvider.validateToken(refreshToken)) {
            val authentication = tokenProvider.getAuthentication(refreshToken)
            return tokenProvider.createAccessToken(authentication)
        } else {
            throw CustomException(ResponseStatus.UNAUTHENTICATED_USER)
        }
    }

}