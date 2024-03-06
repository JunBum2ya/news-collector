package com.midas.newscollector.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.midas.newscollector.config.TestSecurityConfig
import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.dto.request.CompanyRequest
import com.midas.newscollector.service.CompanyService
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest
import org.springframework.boot.test.mock.mockito.MockBean
import org.springframework.context.annotation.Import
import org.springframework.http.MediaType
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers

@DisplayName("View 컨트롤러 - 뉴스 사이트")
@WebMvcTest(CompanyRestController::class)
@Import(TestSecurityConfig::class)
class CompanyRestControllerTest(@Autowired val mvc: MockMvc) {
    @MockBean
    private lateinit var companyService: CompanyService
    private val objectMapper = ObjectMapper()

    @DisplayName("[view][GET] 전체 검색 사이트 조회")
    @Test
    fun givenNothing_whenRequestAllCompanies_thenReturnsAllCompanies() {
        //given
        val companyDtoList: List<CompanyDto> = listOf(
            CompanyDto(companyType = CompanyType.NAVER, active = true),
            CompanyDto(companyType = CompanyType.GOOGLE, active = true),
            CompanyDto(companyType = CompanyType.DAUM, active = false)
        )
        given(companyService.getAllCompanies()).willReturn(companyDtoList)
        //when
        mvc.perform(MockMvcRequestBuilders.get("/company").header("api-key", "0000"))
            .andExpect(MockMvcResultMatchers.status().isOk())
            .andExpect(MockMvcResultMatchers.jsonPath("$.code").value("200"))
            .andExpect(MockMvcResultMatchers.jsonPath("$.data").isArray())

        //then
        then(companyService).should().getAllCompanies()
    }

    @DisplayName("[view][POST] 검색 사이트 활성화")
    @Test
    @Throws(Exception::class)
    fun givenCompanyRequest_whenRequestActivateCompany_thenReturnsCompany() {
        //given
        val companyRequest = CompanyRequest(companyName = "NAVER")
        given(companyService.activateCompany(CompanyType.NAVER))
            .willReturn(CompanyDto(companyType = CompanyType.NAVER, active = true))
        //when
        mvc.perform(
            post("/company")
                .header("api-key", "0000")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(companyRequest))
        ).andExpect(MockMvcResultMatchers.status().isOk())
        //then
        then(companyService).should().activateCompany(CompanyType.NAVER)
    }

    @DisplayName("[view][DELETE] 검색 사이트 비활성화")
    @Test
    @Throws(Exception::class)
    fun givenCompanyRequest_whenRequestDeactivateCompany_thenReturnsCompany() {
        //given
        given(companyService.deactivateCompany(CompanyType.NAVER))
            .willReturn(CompanyDto(companyType = CompanyType.NAVER, active = true))
        //when
        mvc.perform(
            delete("/company")
                .header("api-key", "0000")
                .param("companyName","NAVER")
        ).andExpect(MockMvcResultMatchers.status().isOk())
        //then
        then(companyService).should().deactivateCompany(CompanyType.NAVER)
    }

}