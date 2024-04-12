package com.midas.newscollector.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import com.fasterxml.jackson.module.kotlin.KotlinModule
import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.dto.request.CompanyRequest
import com.midas.newscollector.dto.response.CompanyResponse
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.handler.CustomExceptionHandler
import com.midas.newscollector.service.CompanyService
import io.kotest.core.spec.style.DescribeSpec
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*
import org.springframework.test.web.servlet.setup.MockMvcBuilders
import org.springframework.web.context.WebApplicationContext

class CompanyRestControllerTest : DescribeSpec({
    val companyService = mockk<CompanyService>()
    val companyRestController = CompanyRestController(companyService)
    val customExceptionHandler = CustomExceptionHandler()
    val mapper = ObjectMapper().registerModule(JavaTimeModule())

    val mvc = MockMvcBuilders.standaloneSetup(companyRestController,customExceptionHandler).build()

    describe("[view][GET] 전체 검색 조회") {
        val companyList = listOf(
            CompanyDto(companyType = CompanyType.NAVER, active = true),
            CompanyDto(companyType = CompanyType.GOOGLE, active = true),
            CompanyDto(companyType = CompanyType.DAUM, active = false)
        )
        every { companyService.getAllCompanies() }.returns(companyList)
        it("정상 호출") {
            mvc.perform(get("/company"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.data").isArray)
            verify { companyService.getAllCompanies() }
        }
    }
    describe("[view][POST] 뉴스 플랫폼 활성화") {
        every { companyService.activateCompany(any(CompanyType::class)) }.returns(CompanyDto(companyType = CompanyType.NAVER))
        it("정상 호출") {
            val request = CompanyRequest(companyName = "GOOGLE")
            mvc.perform(
                post("/company")
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(mapper.writeValueAsString(request))
            )
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.data").isMap)
            verify { companyService.activateCompany(any(CompanyType::class)) }
        }
    }
    describe("[view][DELETE] 뉴스 플랫폼 비활성화") {
        val company = CompanyDto(companyType = CompanyType.NAVER)
        every { companyService.deactivateCompany(any(CompanyType::class)) }.returns(company)
        it("should return 200") {
            mvc.perform(delete("/company")
                .queryParam("companyName","GOOGLE"))
                .andExpect(status().isOk)
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.code").value(ResponseStatus.SUCCESS.code))
                .andExpect(jsonPath("$.data").isMap)
            verify { companyService.deactivateCompany(any(CompanyType::class)) }
        }
    }

})