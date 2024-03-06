package com.midas.newscollector.controller

import com.midas.newscollector.dto.request.KeywordRequest
import com.midas.newscollector.dto.response.CommonResponse
import com.midas.newscollector.dto.response.KeywordResponse
import com.midas.newscollector.dto.response.ResponseStatus
import com.midas.newscollector.exception.CustomException
import com.midas.newscollector.service.KeywordService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RequestMapping("keyword")
@RestController
class KeywordRestController(private val keywordService: KeywordService) {

    /**
     * 활성화된 키워드  검색
     */
    @GetMapping
    fun getAllActiveKeyword(): ResponseEntity<CommonResponse<List<KeywordResponse>>> {
        val keywords = keywordService.getActiveKeywords().stream().map(KeywordResponse::of).toList()
        return ResponseEntity.ok(CommonResponse.of(keywords))
    }

    /**
     * 키워드 활성화
     */
    @PostMapping
    fun activateKeyword(@RequestBody @Valid keywordRequest: KeywordRequest): ResponseEntity<CommonResponse<KeywordResponse>> {
        val keyword = keywordService.activateKeyword(
            keywordRequest.keyword ?: throw CustomException(ResponseStatus.NOT_VALID_REQUEST)
        )
        return ResponseEntity.ok(CommonResponse.of(KeywordResponse.of(keyword)))
    }

    /**
     * 키워드 비활성화
     */
    @DeleteMapping
    fun deactivateKeyword(@RequestParam keyword: String): ResponseEntity<CommonResponse<KeywordResponse>> {
        val keywordDto = keywordService.deactivateKeyword(keyword)
        return ResponseEntity.ok(CommonResponse.of(KeywordResponse.of(keywordDto)))
    }

}