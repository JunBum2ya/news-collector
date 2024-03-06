package com.midas.newscollector.service

import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.repository.CompanyRepository
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.*

@DisplayName("비즈니스 로직 - 뉴스 사이트")
@ExtendWith(MockitoExtension::class)
class CompanyServiceTest {
    @InjectMocks
    private lateinit var companyService: CompanyService

    @Mock
    private lateinit var companyRepository: CompanyRepository

    @DisplayName("전체 조회를 하면 CompanyDto 리스트가 반환된다.")
    @Test
    fun givenNothing_whenSearchAllCompany_thenReturnsCompanyDtoList() {
        //given
        given(companyRepository.findAll()).willReturn(createCompanies())
        //when
        val companies = companyService.getAllCompanies()
        //then
        then(companyRepository).should().findAll()
        assertThat(companies).isNotEmpty
        assertThat(companies.size).isEqualTo(3)
    }

    @DisplayName("활성화된 사이트 조회를 하면 CompanyDto 리스트가 반환된다.")
    @Test
    fun givenNothing_whenSearchActiveCompanies_thenReturnsCompanyDtoSet() {
        //given
        val activeTestCompanies = createCompanies().filter { it.active }.toList()
        given(companyRepository.searchActiveCompanies()).willReturn(activeTestCompanies)
        //when
        val activeCompanies = companyService.getAllActiveCompanyList()
        //then
        then(companyRepository).should().searchActiveCompanies()
        assertThat(activeCompanies).isNotEmpty
        assertThat(activeCompanies.size).isEqualTo(2)
    }

    @DisplayName("CompanyType으로 활성화를 하면 CompanyDto가 반환된다.")
    @Test
    fun givenCompanyDto_whenActivateCompany_thenReturnsCompanyDto() {
        //given
        given(companyRepository.findById(any(CompanyType::class.java))).willReturn(
            Optional.of(Company(id = CompanyType.NAVER, active = false))
        )
        //when
        val companyDto = companyService.activateCompany(CompanyType.NAVER)
        //then
        then(companyRepository).should().findById(any(CompanyType::class.java))
        assertThat(companyDto).isNotNull
        assertThat(companyDto.active).isTrue()
    }

    @DisplayName("CompanyType으로 비활성화를 하면 CompanyDto가 반환된다.")
    @Test
    fun givenCompanyDto_whenDeactivateCompany_thenReturnsCompanyDto() {
        //given
        given(companyRepository.findById(any(CompanyType::class.java))).willReturn(
            Optional.of(Company(id = CompanyType.NAVER, active = true))
        )
        //when
        val companyDto = companyService.deactivateCompany(CompanyType.NAVER)
        //then
        then(companyRepository).should().findById(any(CompanyType::class.java))
        assertThat(companyDto).isNotNull
        assertThat(companyDto.active).isFalse()
    }

    private fun createCompanies(): List<Company> {
        return listOf(
            Company(id = CompanyType.GOOGLE, active = true),
            Company(id = CompanyType.GOOGLE, active = true),
            Company(id = CompanyType.NAVER, active = false)
        )
    }

}