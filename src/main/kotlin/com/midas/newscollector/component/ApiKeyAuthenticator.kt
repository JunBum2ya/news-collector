package com.midas.newscollector.component

import org.springframework.security.authorization.AuthorizationDecision
import org.springframework.security.core.Authentication
import org.springframework.security.web.access.intercept.RequestAuthorizationContext
import org.springframework.stereotype.Component
import java.util.function.Supplier

/**
 * API KEY 인증 절차
 */
@Component
class ApiKeyAuthenticator {
    private val HEADER_NAME = "api-key"
    private val ALLOWED_VALUE = "0000"

    fun hasApiKey(
        supplier: Supplier<Authentication>,
        authentication: RequestAuthorizationContext
    ): AuthorizationDecision {
        return AuthorizationDecision(ALLOWED_VALUE == authentication.request.getHeader(HEADER_NAME))
    }
}
