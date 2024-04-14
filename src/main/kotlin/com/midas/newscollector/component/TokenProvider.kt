package com.midas.newscollector.component

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.midas.newscollector.dto.MemberPrincipal
import io.jsonwebtoken.*
import io.jsonwebtoken.security.Keys
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.InitializingBean
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.stereotype.Component
import java.security.Key
import java.util.*
import java.util.stream.Collectors

@Component
class TokenProvider(
    @Value("\${jwt.secret}") val secret: String,
    @Value("\${jwt.access-token-validity-in-seconds}") accessTokenValidityInSeconds: Long,
    @Value("\${jwt.refresh-token-validity-in-seconds}") refreshTokenValidityInSeconds: Long,
    val mapper: ObjectMapper
) : InitializingBean {

    private val logger = LoggerFactory.getLogger(TokenProvider::class.java)

    private val AUTHORITIES_KEY = "auth"
    private val accessTokenValidityInMilliseconds = accessTokenValidityInSeconds * 1000
    private val refreshTokenValidityInMilliseconds = refreshTokenValidityInSeconds * 1000
    private var key: Key? = null

    override fun afterPropertiesSet() {
        val keyBytes = Base64.getDecoder().decode(this.secret)
        this.key = Keys.hmacShaKeyFor(keyBytes)
    }

    /**
     * 토큰 발행 로직
     */
    fun createJwtToken(authentication: Authentication, expiredTime: Long): String {
        val authorities = authentication.authorities
            .stream()
            .map { obj -> obj.authority }
            .collect(Collectors.toList())
        val principal = authentication.principal
        return Jwts.builder()
            .setSubject(mapper.writeValueAsString(principal))
            .claim(this.AUTHORITIES_KEY, authorities)
            .signWith(key, SignatureAlgorithm.HS512)
            .setExpiration(Date(Date().time + expiredTime))
            .compact()
    }

    /**
     * jwt token 발행
     */
    fun createAccessToken(authentication: Authentication): String {
        return createJwtToken(authentication, accessTokenValidityInMilliseconds)
    }

    /**
     * refresh token 발행
     */
    fun createRefreshToken(authentication: Authentication): String {
        return createJwtToken(authentication, refreshTokenValidityInMilliseconds)
    }

    /**
     * token으로 authentication 파싱
     */
    fun getAuthentication(token: String): Authentication {
        val claims = Jwts.parserBuilder()
            .setSigningKey(key)
            .build()
            .parseClaimsJws(token)
            .body
        val authorities = Arrays.stream(
            claims[AUTHORITIES_KEY].toString()
                .split(",".toRegex())
                .dropLastWhile { it.isEmpty() }
                .toTypedArray()
        ).map { authority -> SimpleGrantedAuthority(authority) }
            .toList()
        val principal = mapper.readValue<MemberPrincipal>(claims.subject)
        return UsernamePasswordAuthenticationToken(principal, token, authorities)
    }

    /**
     * token 유효성 검사
     */
    fun validateToken(token: String?): Boolean {
        try {
            Jwts.parserBuilder().setSigningKey(key).build().parseClaimsJws(token)
            return true
        }catch (e: SecurityException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: MalformedJwtException) {
            logger.info("잘못된 JWT 서명입니다.")
        } catch (e: ExpiredJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: UnsupportedJwtException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e: IllegalArgumentException) {
            logger.info("만료된 JWT 토큰입니다.")
        } catch (e : SignatureException) {
            logger.info("잘못된 JWT 토큰입니다.")
        }
        return false
    }

}