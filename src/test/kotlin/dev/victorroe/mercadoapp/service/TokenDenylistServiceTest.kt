package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.entity.RevokedTokenEntity
import dev.victorroe.mercadoapp.repository.RevokedTokenRepository
import org.junit.jupiter.api.Assertions.assertFalse
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.never
import org.mockito.BDDMockito.then
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Date

@ExtendWith(MockitoExtension::class)
class TokenDenylistServiceTest {

    @Mock lateinit var revokedTokenRepository: RevokedTokenRepository
    @Mock lateinit var jwtService: JwtService

    @InjectMocks
    lateinit var service: TokenDenylistService

    private val token = "a.b.c"

    @Test
    fun `revoke persists a new token hash with its expiration`() {
        given(revokedTokenRepository.existsByTokenHash(anyString())).willReturn(false)
        given(jwtService.extractExpiration(token)).willReturn(Date(System.currentTimeMillis() + 60_000))

        service.revoke(token)

        then(revokedTokenRepository).should().save(any(RevokedTokenEntity::class.java))
    }

    @Test
    fun `revoke is idempotent and does not persist an already revoked token`() {
        given(revokedTokenRepository.existsByTokenHash(anyString())).willReturn(true)

        service.revoke(token)

        then(revokedTokenRepository).should(never()).save(any(RevokedTokenEntity::class.java))
    }

    @Test
    fun `isRevoked returns true when the token hash exists`() {
        given(revokedTokenRepository.existsByTokenHash(anyString())).willReturn(true)

        assertTrue(service.isRevoked(token))
    }

    @Test
    fun `isRevoked returns false when the token hash is not present`() {
        given(revokedTokenRepository.existsByTokenHash(anyString())).willReturn(false)

        assertFalse(service.isRevoked(token))
    }
}
