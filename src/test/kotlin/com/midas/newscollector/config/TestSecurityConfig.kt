package com.midas.newscollector.config

import com.midas.newscollector.component.ApiKeyAuthenticator
import org.springframework.boot.autoconfigure.security.servlet.PathRequest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Import
import org.springframework.security.config.Customizer
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configurers.AuthorizeHttpRequestsConfigurer.AuthorizationManagerRequestMatcherRegistry
import org.springframework.security.config.annotation.web.configurers.CsrfConfigurer
import org.springframework.security.web.SecurityFilterChain

@TestConfiguration
@Import(ApiKeyAuthenticator::class)
class TestSecurityConfig(private val apiKeyAuthenticator: ApiKeyAuthenticator) {

    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests { it.anyRequest().access(apiKeyAuthenticator::hasApiKey) }
            .build()
    }
}