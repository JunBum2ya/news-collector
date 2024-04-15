package com.midas.newscollector.controller

import com.midas.newscollector.dto.request.AccessTokenRequest
import com.midas.newscollector.dto.request.MemberRequest
import com.midas.newscollector.dto.request.RefreshTokenRequest
import com.midas.newscollector.dto.response.CommonResponse
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.dto.response.TokenResponse
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.service.AuthenticationService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.security.core.parameters.P
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RequestMapping("/member")
@RestController
class MemberRestController(val authenticationService: AuthenticationService) {

    /**
     * refresh 토큰 발급
     */
    @PostMapping("/refresh-token")
    fun issueRefreshToken(@Valid @RequestBody request: RefreshTokenRequest): ResponseEntity<CommonResponse<TokenResponse>> {
        val token = authenticationService.issueRefreshToken(request.toDto())
        return ResponseEntity.ok(CommonResponse.of(TokenResponse.from(token)))
    }

    /**
     * member 등록
     */
    @PostMapping("/register-member")
    fun registerMember(@Valid @RequestBody request: MemberRequest): ResponseEntity<CommonResponse<TokenResponse>> {
        val token = authenticationService.registerMember(request.toDto())
        return ResponseEntity.ok(CommonResponse.Companion.of(TokenResponse.from(token)))
    }

    /**
     * jwt 토큰 발급
     */
    @PostMapping("/access-token")
    fun issueAccessToken(@Valid @RequestBody request: AccessTokenRequest): ResponseEntity<CommonResponse<String>> {
        val refreshToken = request.refreshToken ?: throw CustomException(ResponseStatus.NOT_VALID_REQUEST)
        val accessToken = authenticationService.issueAccessToken(refreshToken)
        return ResponseEntity.ok(CommonResponse.of(accessToken))
    }

}