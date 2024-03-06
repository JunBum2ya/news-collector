package com.midas.newscollector.exception

import com.midas.newscollector.dto.response.ResponseStatus

class CustomException(val code: String, message: String) : RuntimeException(message) {
    constructor(status: ResponseStatus) : this(code = status.code, message = status.message)
    constructor(status: ResponseStatus, message: String) : this(code = status.code, message = message)
}