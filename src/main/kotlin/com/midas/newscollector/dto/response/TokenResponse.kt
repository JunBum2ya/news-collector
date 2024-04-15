package com.midas.newscollector.dto.response

import com.midas.newscollector.dto.TokenDto

data class TokenResponse(val userId: String, val refreshToken: String, val accessToken: String) {

    companion object {
        fun from(tokenDto: TokenDto): TokenResponse {
            return TokenResponse(
                userId = tokenDto.userId,
                refreshToken = tokenDto.refreshToken,
                accessToken = tokenDto.accessToken
            )
        }
    }
}
