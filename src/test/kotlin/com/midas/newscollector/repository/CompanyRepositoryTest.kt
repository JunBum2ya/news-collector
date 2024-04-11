package com.midas.newscollector.repository

import com.midas.newscollector.config.JpaConfig
import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import org.assertj.core.api.Assertions
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest
import org.springframework.context.annotation.Import
import org.springframework.test.context.ActiveProfiles

@DisplayName("리포지토리 테스트 - Company")
@DataJpaTest
@Import(JpaConfig::class)
@ActiveProfiles("testdb")
class CompanyRepositoryTest(@Autowired private val companyRepository: CompanyRepository) {

    val companies = mutableListOf<Company>()

    @BeforeEach
    fun initCompany() {
        companies.clear()
        companies.add(companyRepository.save(Company(CompanyType.NAVER, active = false)))
        companies.add(companyRepository.save(Company(CompanyType.GOOGLE)))
        companies.add(companyRepository.save(Company(CompanyType.DAUM)))
    }

    @DisplayName("전체 조회를 하면 Company 목록이 조회된다.")
    @Test
    fun givenNothing_whenSearchAllCompany_thenReturnsCompanyList() {
        //given
        //when
        val companies = companyRepository.findAll()
        //then
        assertThat(companies).isNotEmpty()
        assertThat(companies.size).isEqualTo(3)
    }

    @DisplayName("active 여부로 조회를 하면 Company 목록이 조회된다.")
    @Test
    fun givenNothing_whenSearchActiveCompanies_thenReturnsActiveCompanies() {
        //when
        val companies = companyRepository.searchActiveCompanies()
        //then
        assertThat(companies).isNotEmpty()
        assertThat(companies.size).isEqualTo(2)
    }

    @DisplayName("company를 활성화하면 데이터베이스에서 수정된다.")
    @Test
    fun givenCompany_whenActivateCompany_thenUpdateCompany() {
        //given
        val company = companies[0]
        //when
        company.active = true
        companyRepository.flush()
        //then
        val updatedCompany = companyRepository.searchActiveCompanies().first { it.id == CompanyType.NAVER }
        assertThat(updatedCompany.active).isTrue()
    }

    @DisplayName("company를 비활성화 하면 데이터베이스에서 수정된다.")
    @Test
    fun givenCompany_whenDeactivateCompany_thenUpdateCompany() {
        //given
        val company = companies[1]
        //when
        company.active = false
        companyRepository.flush()
        //then
        val activeCompanies = companyRepository.searchActiveCompanies()
        assertThat(activeCompanies).hasSize(1)
    }

}