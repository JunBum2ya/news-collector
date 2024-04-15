package com.midas.newscollector.config

import com.midas.newscollector.component.JwtAuthenticationEntryPoint
import com.midas.newscollector.component.TokenProvider
import com.midas.newscollector.filter.CustomJwtFilter
import com.midas.newscollector.handler.JwtAccessDeniedHandler
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.security.web.SecurityFilterChain
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

@EnableWebSecurity
@Configuration
class SecurityConfig(
    private val tokenProvider: TokenProvider,
    val jwtAuthenticationEntryPoint: JwtAuthenticationEntryPoint,
    val jwtAccessDeniedHandler: JwtAccessDeniedHandler
) {
    @Bean
    @Throws(Exception::class)
    fun securityFilterChain(httpSecurity: HttpSecurity): SecurityFilterChain {
        return httpSecurity
            .csrf { it.disable() }
            .exceptionHandling {
                it.authenticationEntryPoint(jwtAuthenticationEntryPoint).accessDeniedHandler(jwtAccessDeniedHandler)
            }
            .headers { it.frameOptions { o -> o.sameOrigin() } }
            .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
            .authorizeHttpRequests { request ->
                request.requestMatchers(
                    "/member/refresh-token",
                    "/member/access-token",
                    "/member/register-member"
                ).permitAll().anyRequest().authenticated()
            }
            .addFilterBefore(CustomJwtFilter(tokenProvider), UsernamePasswordAuthenticationFilter::class.java)
            .build()
    }

    @Bean
    fun passwordEncoder(): PasswordEncoder {
        return BCryptPasswordEncoder()
    }
}