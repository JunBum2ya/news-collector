package com.midas.newscollector.dto.request

import com.midas.newscollector.dto.MemberDto
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import jakarta.validation.constraints.NotEmpty

class RefreshTokenRequest(
    @field:NotEmpty(message = "아이디를 입력하시오.") var userId: String?,
    @field:NotEmpty(message = "패스워드를 입력하시오.") var password: String?
) {
    fun toDto(): MemberDto {
        return MemberDto(
            memberId = userId ?: throw CustomException(ResponseStatus.NOT_VALID_REQUEST),
            password = password ?: throw CustomException(ResponseStatus.NOT_VALID_REQUEST),
            email = ""
        );
    }
}