package com.midas.newscollector.dto.response

import com.fasterxml.jackson.annotation.JsonInclude

@JsonInclude(JsonInclude.Include.NON_NULL)
class CommonResponse<T>(
    private val code: String,
    private val message: String,
    private val data: T? = null
) {
    companion object {
        fun <T> of(status: ResponseStatus, data: T): CommonResponse<T> {
            return CommonResponse(code = status.code, message = status.message, data)
        }

        fun <T> of(data: T): CommonResponse<T> {
            return of(ResponseStatus.SUCCESS, data)
        }

        fun of(responseStatus: ResponseStatus): CommonResponse<Any> {
            return CommonResponse(code = responseStatus.code, message = responseStatus.message)
        }

        fun of(code: String, message: String): CommonResponse<Any> {
            return CommonResponse(code = code, message = message)
        }
    }
}