package com.midas.newscollector.dto.request

import com.midas.newscollector.dto.MemberDto
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException

class MemberRequest(var memberId: String?, var password: String?, var email: String?) {

    fun toDto(): MemberDto {
        return MemberDto(
            memberId = memberId ?: throw CustomException(ResponseStatus.NOT_VALID_REQUEST),
            password = password ?: throw CustomException(ResponseStatus.NOT_VALID_REQUEST),
            email = email ?: throw CustomException(ResponseStatus.NOT_VALID_REQUEST)
        )
    }

}