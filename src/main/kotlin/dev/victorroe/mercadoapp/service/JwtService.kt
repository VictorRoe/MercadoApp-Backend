package dev.victorroe.mercadoapp.service

import io.jsonwebtoken.Claims
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import org.springframework.beans.factory.annotation.Value
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.stereotype.Service
import java.util.Date
import javax.crypto.SecretKey

@Service
class JwtService(
    @Value("\${app.jwt.secret}") private val secret: String,
    @Value("\${app.jwt.expiration}") private val expiration: Long
) {
    private val signingKey: SecretKey by lazy {
        Keys.hmacShaKeyFor(secret.toByteArray(Charsets.UTF_8))
    }

    fun generateToken(userId: Long, email: String, name: String, role: String): String {
        val now = Date()
        return Jwts.builder()
            .subject(email)
            .claim("userId", userId)
            .claim("name", name)
            .claim("role", role)
            .issuedAt(now)
            .expiration(Date(now.time + expiration))
            .signWith(signingKey)
            .compact()
    }

    fun extractEmail(token: String): String? = extractClaim(token) { it.subject }

    fun extractExpiration(token: String): Date = extractClaim(token) { it.expiration }

    fun isTokenValid(token: String, userDetails: UserDetails): Boolean {
        val email = extractEmail(token) ?: return false
        return email == userDetails.username && !isTokenExpired(token)
    }

    private fun isTokenExpired(token: String): Boolean =
        extractClaim(token) { it.expiration }.before(Date())

    private fun <T> extractClaim(token: String, resolver: (Claims) -> T): T =
        resolver(parseClaims(token))

    private fun parseClaims(token: String): Claims =
        Jwts.parser()
            .verifyWith(signingKey)
            .build()
            .parseSignedClaims(token)
            .payload
}
