package com.midas.newscollector.controller

import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.dto.request.CompanyRequest
import com.midas.newscollector.dto.response.CommonResponse
import com.midas.newscollector.dto.response.CompanyResponse
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.service.CompanyService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("company")
@RestController
class CompanyRestController(private val companyService: CompanyService) {
    /**
     * company 전체 조회
     */
    @GetMapping
    fun getCompanies(): ResponseEntity<CommonResponse<List<CompanyResponse>>> {
        val companies = companyService.getAllCompanies().map { CompanyResponse.of(it) }
        return ResponseEntity.ok(CommonResponse.of(companies))
    }

    /**
     * company 활성화
     */
    @PostMapping
    fun activateCompany(@RequestBody @Valid companyRequest: CompanyRequest): ResponseEntity<CommonResponse<CompanyResponse>> {
        val company = companyService.activateCompany(companyRequest.getCompanyType())
        return ResponseEntity.ok(CommonResponse.of(CompanyResponse.of(company)))
    }

    /**
     * company 비활성화
     */
    @DeleteMapping
    fun deactivateCompany(@RequestParam companyName: String): ResponseEntity<CommonResponse<CompanyResponse>> {
        val company = companyService.deactivateCompany(CompanyType.valueOf(companyName))
        return ResponseEntity.ok(CommonResponse.of(CompanyResponse.of(company)))
    }
}