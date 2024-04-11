package com.midas.newscollector.service

import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.CompanyRepository
import io.kotest.assertions.throwables.shouldThrow
import io.kotest.core.spec.style.BehaviorSpec
import io.kotest.matchers.collections.shouldHaveSize
import io.kotest.matchers.nulls.shouldNotBeNull
import io.kotest.matchers.shouldBe
import io.kotest.matchers.shouldNotBe
import io.kotest.matchers.throwable.shouldHaveMessage
import io.mockk.every
import io.mockk.mockk
import io.mockk.verify
import org.springframework.data.repository.findByIdOrNull

class CompanyServiceTest: BehaviorSpec({

    val companyRepository = mockk<CompanyRepository>()
    val companyService = CompanyService(companyRepository)

    Given("아무것도 주어지지 않은 상태에서") {
        every { companyRepository.findAll() }.returns(createCompanies())
        When("전체 조회를 하면") {
            val companies = companyService.getAllCompanies()
            Then("CompanyDto 리스트가 반환된다.") {
                companies shouldHaveSize 3
                verify { companyRepository.findAll() }
            }
        }
    }
    Given("아무것도 주어지지 않은 상태에서") {
        every { companyRepository.searchActiveCompanies() }.returns(createCompanies().filter { it.active })
        When("활성화된 뉴스플랫폼을 조회하면") {
            val activeCompanies = companyService.getAllActiveCompanyList()
            Then("CompanyDto 리스트가 반환된다") {
                activeCompanies shouldHaveSize 2
                verify { companyRepository.searchActiveCompanies() }
            }
        }
    }
    Given("CompanyType으로") {
        val companyType = CompanyType.NAVER
        When("뉴스플랫폼을 활성화를 하면") {
            every { companyRepository.findByIdOrNull(any(CompanyType::class)) }
                .returns(Company(companyType, active = false))
            val company = companyService.activateCompany(companyType)
            Then("CompanyDto가 반환된다.") {
                company.shouldNotBeNull()
                company.companyType shouldBe companyType
                company.active shouldBe true
                verify { companyRepository.findByIdOrNull(any(CompanyType::class)) }
            }
        }
        When("존재하지 않는 뉴스 플랫폼을 활성화하면") {
            every { companyRepository.findByIdOrNull(any(CompanyType::class)) }
                .returns(null)
            Then("CustomException이 발생된다.") {
                val exception = shouldThrow<CustomException> { companyService.activateCompany(companyType) }
                exception.code shouldBe ResponseStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception shouldHaveMessage ResponseStatus.ACCESS_NOT_EXIST_ENTITY.message
                verify { companyRepository.findByIdOrNull(any(CompanyType::class)) }
            }
        }
    }
    Given("CompanyType으로") {
        val companyType = CompanyType.NAVER
        When("뉴스 플랫폼을 비활성화 하면") {
            every { companyRepository.findByIdOrNull(any(CompanyType::class)) }.returns(Company(companyType))
            val company = companyService.deactivateCompany(companyType)
            Then("CompanyDto가 반환된다.") {
                company shouldNotBe null
                company.companyType shouldBe companyType
                company.active shouldBe false
                verify { companyRepository.findByIdOrNull(any(CompanyType::class)) }
            }
        }
        When("존재하지 않는 뉴스 플랫폼을 비활성화하면") {
            every { companyRepository.findByIdOrNull(any(CompanyType::class)) }.returns(null)
            Then("CustomException이 발생한다.") {
                val exception = shouldThrow<CustomException> { companyService.deactivateCompany(companyType) }
                exception.code shouldBe ResponseStatus.ACCESS_NOT_EXIST_ENTITY.code
                exception shouldHaveMessage ResponseStatus.ACCESS_NOT_EXIST_ENTITY.message
                verify { companyRepository.findByIdOrNull(any(CompanyType::class)) }
            }
        }
    }
}) {
    companion object {
        private fun createCompanies(): List<Company> {
            return listOf(
                Company(id = CompanyType.GOOGLE, active = true),
                Company(id = CompanyType.GOOGLE, active = true),
                Company(id = CompanyType.NAVER, active = false)
            )
        }
    }

}