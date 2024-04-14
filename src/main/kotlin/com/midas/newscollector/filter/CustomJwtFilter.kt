package com.midas.newscollector.filter

import com.midas.newscollector.component.TokenProvider
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletRequest
import jakarta.servlet.ServletResponse
import jakarta.servlet.http.HttpServletRequest
import org.slf4j.LoggerFactory
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.util.StringUtils
import org.springframework.web.filter.GenericFilterBean

class CustomJwtFilter(private val tokenProvider: TokenProvider) : GenericFilterBean() {

    private val logger = LoggerFactory.getLogger(CustomJwtFilter::class.java)

    override fun doFilter(request: ServletRequest, response: ServletResponse, chain: FilterChain) {
        val httpServletRequest = request as HttpServletRequest
        val jwt = resolveToken(httpServletRequest)
        val requestURI = httpServletRequest.requestURI
        if (StringUtils.hasText(jwt) && tokenProvider.validateToken(jwt)) {
            val authentication = tokenProvider.getAuthentication(jwt!!) // null체크 완료
            SecurityContextHolder.getContext().authentication = authentication
            logger.debug("Security Context에 '${authentication.name}' 인증 정보를 저장했습니다, uri: ${requestURI}")
        } else {
            logger.debug("유효한 JWT 토큰이 없습니다, uri: ${requestURI}")
        }
        chain.doFilter(request, response)
    }

    private fun resolveToken(request: HttpServletRequest): String? {
        val bearerToken = request.getHeader(AUTHORIZATION_HEADER)
        return if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer "))
            bearerToken.substring(7)
        else null
    }

    companion object {
        const val AUTHORIZATION_HEADER: String = "Authorization"
    }
}