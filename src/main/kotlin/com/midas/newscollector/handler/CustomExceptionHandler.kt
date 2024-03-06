package com.midas.newscollector.handler

import com.midas.newscollector.dto.response.CommonResponse
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.MethodArgumentNotValidException
import org.springframework.web.bind.annotation.ExceptionHandler
import org.springframework.web.bind.annotation.RestControllerAdvice

@RestControllerAdvice
class CustomExceptionHandler {
    @ExceptionHandler(CustomException::class)
    fun handleCustomException(e: CustomException): ResponseEntity<CommonResponse<Any>> {
        return ResponseEntity.badRequest()
            .body(CommonResponse.of(code = e.code, message = e.message ?: "UNKNOWN ERROR"))
    }

    @ExceptionHandler(MethodArgumentNotValidException::class)
    fun handleMethodArgumentNotValidException(e: MethodArgumentNotValidException): ResponseEntity<CommonResponse<Any>> {
        return ResponseEntity.badRequest().body(CommonResponse.of(ResponseStatus.NOT_VALID_REQUEST))
    }

    @ExceptionHandler(Exception::class)
    fun handleException(e: Exception): ResponseEntity<CommonResponse<Any>> {
        return ResponseEntity.badRequest().body(CommonResponse.of(ResponseStatus.UNKNOWN_ERROR))
    }
}