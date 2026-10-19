package dev.victorroe.mercadoapp.service

import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.security.core.userdetails.User
import java.util.Date

class JwtServiceTest {

    private val secret = "unit-test-secret-unit-test-secret-unit-test-secret"
    private val service = JwtService(secret, 900_000L)

    private val userDetails = User("ana@mail.com", "pwd", emptyList())

    @Test
    fun `access token lifetime is exposed in seconds`() {
        assertEquals(900L, service.accessTokenExpiresInSeconds())
    }

    @Test
    fun `generated token carries the email as subject and a future expiration`() {
        val token = service.generateToken(1L, "ana@mail.com", "Ana", "USER")

        assertEquals("ana@mail.com", service.extractEmail(token))
        assertTrue(service.extractExpiration(token).after(Date()))
    }

    @Test
    fun `token is valid for the user it was issued to`() {
        val token = service.generateToken(1L, "ana@mail.com", "Ana", "USER")

        assertTrue(service.isTokenValid(token, userDetails))
    }

    @Test
    fun `token is invalid for a different user`() {
        val token = service.generateToken(1L, "ana@mail.com", "Ana", "USER")

        assertFalse(service.isTokenValid(token, User("other@mail.com", "pwd", emptyList())))
    }

    @Test
    fun `expired token is rejected when parsed`() {
        val expired = JwtService(secret, -1_000L).generateToken(1L, "ana@mail.com", "Ana", "USER")

        assertThrows<ExpiredJwtException> { service.isTokenValid(expired, userDetails) }
    }

    @Test
    fun `token signed with another key is rejected`() {
        val foreign = JwtService("another-secret-another-secret-another-secret-123", 900_000L)
            .generateToken(1L, "ana@mail.com", "Ana", "USER")

        assertThrows<SignatureException> { service.extractEmail(foreign) }
    }
}
