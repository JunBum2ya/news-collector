package com.midas.newscollector.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.midas.newscollector.dto.MemberDto
import com.midas.newscollector.dto.TokenDto
import com.midas.newscollector.dto.request.AccessTokenRequest
import com.midas.newscollector.dto.request.MemberRequest
import com.midas.newscollector.dto.request.RefreshTokenRequest
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.handler.CustomExceptionHandler
import com.midas.newscollector.service.AuthenticationService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

class MemberRestControllerTest : DescribeSpec({

    val authenticationService = mockk<AuthenticationService>()
    val memberRestController = MemberRestController(authenticationService)
    val mapper = ObjectMapper()
    val mvc = MockMvcBuilders.standaloneSetup(memberRestController, CustomExceptionHandler()).build()

    describe("[view][POST] 리프레쉬 토큰 발급") {
        val token = TokenDto(userId = "test", refreshToken = "12345678", accessToken = "87654321")
        every { authenticationService.issueRefreshToken(any(MemberDto::class)) }.returns(token)
        it("정상 호출") {
            val request = RefreshTokenRequest("test", "1234")
            mvc.perform(
                post("/member/refresh-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.message").value(ResponseStatus.SUCCESS.message))
                .andExpect(jsonPath("$.data.accessToken").value(token.accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(token.refreshToken))
            verify { authenticationService.issueRefreshToken(any(MemberDto::class)) }
        }
    }
    describe("[view][POST] 사용자 등록") {
        val token = TokenDto(userId = "test", refreshToken = "12345678", accessToken = "87654321")
        every { authenticationService.registerMember(any(MemberDto::class)) }.returns(token)
        it("정상 호출") {
            val request = MemberRequest("test", "test", email = "test@test.com")
            mvc.perform(
                post("/member/register-member")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.message").value(ResponseStatus.SUCCESS.message))
                .andExpect(jsonPath("$.data.accessToken").value(token.accessToken))
                .andExpect(jsonPath("$.data.refreshToken").value(token.refreshToken))
            verify { authenticationService.registerMember(any(MemberDto::class)) }
        }
    }
    describe("[view][POST] jwt 토큰 발급") {
        every { authenticationService.issueAccessToken(any(String::class)) }.returns("test-token")
        it("정상 호출") {
            val request = AccessTokenRequest("test")
            mvc.perform(
                post("/member/access-token")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.message").value(ResponseStatus.SUCCESS.message))
                .andExpect(jsonPath("$.data").value("test-token"))
            verify { authenticationService.issueAccessToken(any(String::class)) }
        }
    }

})