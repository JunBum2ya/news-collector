package com.midas.newscollector.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.midas.newscollector.dto.KeywordDto
import com.midas.newscollector.dto.request.KeywordRequest
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.service.KeywordService
import io.kotest.assertions.print.print
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders

@WebMvcTest
class KeywordRestControllerTest : DescribeSpec({

    val keywordService = mockk<KeywordService>()
    val keywordRestController = KeywordRestController(keywordService)
    val mapper = ObjectMapper()

    val mvc = MockMvcBuilders.standaloneSetup(keywordRestController).build()

    describe("[view][GET] 활성화된 키워드 검색") {
        every { keywordService.getActiveKeywords() }.returns(listOf(KeywordDto(name = "선거"), KeywordDto(name = "코로나")))
        it("정상 호출") {
            mvc.perform(get("/keyword"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.message").value(ResponseStatus.SUCCESS.message))
                .andExpect(jsonPath("$.data").isArray)
            verify { keywordService.getActiveKeywords() }
        }
    }
    describe("[view][POST] 키워드 활성화") {
        val keywordDto = KeywordDto(name = "코로나")
        every { keywordService.activateKeyword(any(String::class)) }.returns(keywordDto)
        it("정상 호출") {
            val request = KeywordRequest(keyword = "코로나")
            mvc.perform(post("/keyword")
                .contentType(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(request))
            ).andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.message").value(ResponseStatus.SUCCESS.message))
                .andExpect(jsonPath("$.data").isMap)
            verify { keywordService.activateKeyword(any(String::class)) }
        }
    }
    describe("[view][DELETE] 키워드 비활성화") {
        val keywordDto = KeywordDto(name = "코로나", active = false)
        every { keywordService.deactivateKeyword(any(String::class)) }.returns(keywordDto)
        it("정상 호출") {
            mvc.perform(delete("/keyword")
                .param("keyword","코로나"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.message").value(ResponseStatus.SUCCESS.message))
                .andExpect(jsonPath("$.data").isMap)
            verify { keywordService.deactivateKeyword(any(String::class)) }
        }
    }
})