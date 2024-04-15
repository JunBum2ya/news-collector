package com.midas.newscollector.dto.request

import jakarta.validation.constraints.NotEmpty

class AccessTokenRequest(@field:NotEmpty(message = "리프레시 토큰을 입력하시오.") var refreshToken: String?) {
}