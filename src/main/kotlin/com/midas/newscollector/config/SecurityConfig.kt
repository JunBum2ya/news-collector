package com.midas.newscollector.config

import com.midas.newscollector.component.ApiKeyAuthenticator
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.web.SecurityFilterChain

@Configuration
class SecurityConfig(private val apiKeyAuthenticator: ApiKeyAuthenticator) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .authorizeHttpRequests { request -> request.anyRequest().access(apiKeyAuthenticator::hasApiKey) }
            .build()
    }
}