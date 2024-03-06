package com.midas.newscollector.dto.response

enum class ResponseStatus(val code: String, val message: String) {
    SUCCESS(code = "200", message = "SUCCESS"),
    UNKNOWN_ERROR(code = "001", message = "OCCUR UNKONWN ERROR"),
    UNAUTHENTICATED_USER(code = "002", message = "허용되지 않은 사용자입니다."),
    ACCESS_NOT_EXIST_ENTITY(code = "003", message = "존재 하지 않는 엔티티에 접근했습니다."),
    NOT_VALID_REQUEST(code = "004", message = "필수 파라미터가 입력되지 않았습니다.")
}