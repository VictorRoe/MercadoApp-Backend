package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.auth.LoginRequestDTO
import dev.victorroe.mercadoapp.dto.auth.RegisterRequestDTO
import dev.victorroe.mercadoapp.entity.UserEntity
import dev.victorroe.mercadoapp.exception.DuplicateEmailException
import dev.victorroe.mercadoapp.model.Role
import dev.victorroe.mercadoapp.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class AuthServiceTest {

    @Mock lateinit var userRepository: UserRepository
    @Mock lateinit var passwordEncoder: PasswordEncoder
    @Mock lateinit var jwtService: JwtService
    @Mock lateinit var authenticationManager: AuthenticationManager
    @Mock lateinit var tokenDenylistService: TokenDenylistService
    @Mock lateinit var refreshTokenService: RefreshTokenService

    @InjectMocks
    lateinit var service: AuthService

    private val user = UserEntity(
        id = 7L, fullName = "Ana Maria Lopez", email = "ana@mail.com", password = "hash", role = Role.USER
    )

    @Test
    fun `register stores the user and returns both tokens`() {
        given(userRepository.existsByEmail("ana@mail.com")).willReturn(false)
        given(passwordEncoder.encode("secret")).willReturn("hash")
        given(userRepository.save(any(UserEntity::class.java))).willReturn(user)
        given(jwtService.generateToken(7L, "ana@mail.com", "Ana", "USER")).willReturn("access")
        given(refreshTokenService.issue(7L)).willReturn("refresh")
        given(jwtService.accessTokenExpiresInSeconds()).willReturn(900L)

        val result = service.register(RegisterRequestDTO("Ana Maria Lopez", "ana@mail.com", "secret"))

        assertEquals("access", result.accessToken)
        assertEquals("refresh", result.refreshToken)
        assertEquals(900L, result.expiresIn)
    }

    @Test
    fun `register rejects an email that is already taken`() {
        given(userRepository.existsByEmail("ana@mail.com")).willReturn(true)

        assertThrows<DuplicateEmailException> {
            service.register(RegisterRequestDTO("Ana", "ana@mail.com", "secret"))
        }
        then(userRepository).should(never()).save(any(UserEntity::class.java))
    }

    @Test
    fun `login authenticates and returns both tokens`() {
        given(userRepository.findByEmail("ana@mail.com")).willReturn(Optional.of(user))
        given(jwtService.generateToken(7L, "ana@mail.com", "Ana", "USER")).willReturn("access")
        given(refreshTokenService.issue(7L)).willReturn("refresh")
        given(jwtService.accessTokenExpiresInSeconds()).willReturn(900L)

        val result = service.login(LoginRequestDTO("ana@mail.com", "secret"))

        then(authenticationManager)
            .should().authenticate(UsernamePasswordAuthenticationToken("ana@mail.com", "secret"))
        assertEquals("access", result.accessToken)
        assertEquals("refresh", result.refreshToken)
    }

    @Test
    fun `refresh rotates the refresh token and issues a new access token`() {
        given(refreshTokenService.rotate("old")).willReturn(RotationResult("new-refresh", 7L))
        given(userRepository.findById(7L)).willReturn(Optional.of(user))
        given(jwtService.generateToken(7L, "ana@mail.com", "Ana", "USER")).willReturn("new-access")
        given(jwtService.accessTokenExpiresInSeconds()).willReturn(900L)

        val result = service.refresh("old")

        assertEquals("new-access", result.accessToken)
        assertEquals("new-refresh", result.refreshToken)
    }

    @Test
    fun `logout revokes the access token and the refresh token when present`() {
        service.logout("access", "refresh")

        then(tokenDenylistService).should().revoke("access")
        then(refreshTokenService).should().revoke("refresh")
    }

    @Test
    fun `logout without refresh token only revokes the access token`() {
        service.logout("access", null)

        then(tokenDenylistService).should().revoke("access")
        then(refreshTokenService).shouldHaveNoInteractions()
    }
}
