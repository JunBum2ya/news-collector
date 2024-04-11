package com.midas.newscollector.service

import com.midas.newscollector.domain.constant.CompanyType
import com.midas.newscollector.dto.CompanyDto
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.repository.CompanyRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.util.function.ToIntFunction

@Transactional
@Service
class CompanyService(private val companyRepository: CompanyRepository) {
    /**
     * 전체 플랫폼 조회
     * 정렬
     * @return List: CompanyDto
     */
    @Transactional(readOnly = true)
    fun getAllCompanies(): List<CompanyDto> {
        return companyRepository.findAll()
            .stream()
            .map(CompanyDto::from)
            .sorted(Comparator.comparingInt { a: CompanyDto -> a.companyType.ordinal })
            .toList()
    }

    /**
     * 활성화된 플랫폼 조회
     */
    @Transactional(readOnly = true)
    fun getAllActiveCompanyList(): List<CompanyDto> {
        return companyRepository.searchActiveCompanies()
            .stream()
            .map(CompanyDto::from)
            .sorted(Comparator.comparingInt { a: CompanyDto -> a.companyType.ordinal })
            .toList()
    }

    /**
     * 플랫폼 활성화
     */
    fun activateCompany(companyType: CompanyType): CompanyDto {
        val company = companyRepository.findByIdOrNull(companyType)
            ?: throw CustomException(ResponseStatus.ACCESS_NOT_EXIST_ENTITY)
        company.active = true
        return CompanyDto.from(company)
    }

    /**
     * 플랫폼 비활성화
     */
    fun deactivateCompany(companyType: CompanyType): CompanyDto {
        val company = companyRepository.findByIdOrNull(companyType)
            ?: throw CustomException(ResponseStatus.ACCESS_NOT_EXIST_ENTITY)
        company.active = false
        return CompanyDto.from(company)
    }
}