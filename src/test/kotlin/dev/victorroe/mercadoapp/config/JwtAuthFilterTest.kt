package dev.victorroe.mercadoapp.config

import dev.victorroe.mercadoapp.service.JwtService
import dev.victorroe.mercadoapp.service.TokenDenylistService
import dev.victorroe.mercadoapp.service.UserSecurityService
import jakarta.servlet.FilterChain
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertNotNull
import org.junit.jupiter.api.Assertions.assertNull
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.mock.web.MockHttpServletRequest
import org.springframework.mock.web.MockHttpServletResponse
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UsernameNotFoundException

@ExtendWith(MockitoExtension::class)
class JwtAuthFilterTest {

    @Mock lateinit var jwtService: JwtService
    @Mock lateinit var userSecurityService: UserSecurityService
    @Mock lateinit var tokenDenylistService: TokenDenylistService
    @Mock lateinit var chain: FilterChain

    private val response = MockHttpServletResponse()
    private val userDetails = User("ana@mail.com", "pwd", emptyList())

    private fun filter() = JwtAuthFilter(jwtService, userSecurityService, tokenDenylistService)

    private fun requestWithBearer(token: String) =
        MockHttpServletRequest().apply { addHeader("Authorization", "Bearer $token") }

    @AfterEach
    fun clearContext() = SecurityContextHolder.clearContext()

    @Test
    fun `request without authorization header continues unauthenticated`() {
        val request = MockHttpServletRequest()

        filter().doFilter(request, response, chain)

        then(chain).should().doFilter(request, response)
        assertNull(SecurityContextHolder.getContext().authentication)
    }

    @Test
    fun `valid bearer token authenticates the request`() {
        val request = requestWithBearer("tkn")
        given(jwtService.extractEmail("tkn")).willReturn("ana@mail.com")
        given(userSecurityService.loadUserByUsername("ana@mail.com")).willReturn(userDetails)
        given(jwtService.isTokenValid("tkn", userDetails)).willReturn(true)
        given(tokenDenylistService.isRevoked("tkn")).willReturn(false)

        filter().doFilter(request, response, chain)

        assertNotNull(SecurityContextHolder.getContext().authentication)
        assertEquals("ana@mail.com", SecurityContextHolder.getContext().authentication?.name)
        then(chain).should().doFilter(request, response)
    }

    @Test
    fun `revoked token is not authenticated`() {
        val request = requestWithBearer("tkn")
        given(jwtService.extractEmail("tkn")).willReturn("ana@mail.com")
        given(userSecurityService.loadUserByUsername("ana@mail.com")).willReturn(userDetails)
        given(jwtService.isTokenValid("tkn", userDetails)).willReturn(true)
        given(tokenDenylistService.isRevoked("tkn")).willReturn(true)

        filter().doFilter(request, response, chain)

        assertNull(SecurityContextHolder.getContext().authentication)
        then(chain).should().doFilter(request, response)
    }

    @Test
    fun `unparseable token continues unauthenticated`() {
        val request = requestWithBearer("garbage")
        given(jwtService.extractEmail("garbage")).willThrow(IllegalArgumentException("bad token"))

        filter().doFilter(request, response, chain)

        assertNull(SecurityContextHolder.getContext().authentication)
        then(chain).should().doFilter(request, response)
    }

    @Test
    fun `unknown user continues unauthenticated`() {
        val request = requestWithBearer("tkn")
        given(jwtService.extractEmail("tkn")).willReturn("ghost@mail.com")
        given(userSecurityService.loadUserByUsername("ghost@mail.com"))
            .willThrow(UsernameNotFoundException("nope"))

        filter().doFilter(request, response, chain)

        assertNull(SecurityContextHolder.getContext().authentication)
        then(chain).should().doFilter(request, response)
    }
}
