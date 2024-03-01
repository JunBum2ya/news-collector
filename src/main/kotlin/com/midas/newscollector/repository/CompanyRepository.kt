package com.midas.newscollector.repository;

import com.midas.newscollector.domain.Company
import com.midas.newscollector.domain.constant.CompanyType
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface CompanyRepository : JpaRepository<Company, CompanyType> {
    @Query("SELECT c FROM Company c WHERE c.active")
    fun searchActiveCompanies(): List<Company>;
}