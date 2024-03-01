package com.midas.newscollector.repository

import com.midas.newscollector.config.JpaConfig
import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import org.assertj.core.api.Assertions
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

    @BeforeEach
    fun initCompany() {
        companyRepository.save(Company(CompanyType.NAVER, active = false))
        companyRepository.save(Company(CompanyType.GOOGLE))
        companyRepository.save(Company(CompanyType.DAUM))
    }

    @DisplayName("전체 조회를 하면 Company 목록이 조회된다.")
    @Test
    fun givenNothing_whenSearchAllCompany_thenReturnsCompanyList() {
        //given
        //when
        val companies = companyRepository.findAll()
        //then
        Assertions.assertThat(companies).isNotEmpty()
        Assertions.assertThat(companies.size).isEqualTo(3)
    }

    @DisplayName("active 여부로 조회를 하면 Company 목록이 조회된다.")
    @Test
    fun givenNothing_whenSearchActiveCompanies_thenReturnsActiveCompanies() {
        //when
        val companies = companyRepository.searchActiveCompanies()
        //then
        Assertions.assertThat(companies).isNotEmpty()
        Assertions.assertThat(companies.size).isEqualTo(2)
    }

}