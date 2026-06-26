package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.entity.RefreshTokenEntity
import dev.victorroe.mercadoapp.exception.InvalidRefreshTokenException
import dev.victorroe.mercadoapp.repository.RefreshTokenRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertThrows
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentCaptor
import org.mockito.ArgumentMatchers.any
import org.mockito.ArgumentMatchers.anyString
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.time.Instant
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class RefreshTokenServiceTest {

    @Mock lateinit var refreshTokenRepository: RefreshTokenRepository

    private val refreshExpiration = 2_592_000_000L

    private fun service() = RefreshTokenService(refreshTokenRepository, refreshExpiration)

    @Test
    fun `issue persists only the hash and returns the raw token`() {
        given(refreshTokenRepository.save(any(RefreshTokenEntity::class.java)))
            .willAnswer { it.getArgument(0) }

        val raw = service().issue(userId = 7)

        val captor = ArgumentCaptor.forClass(RefreshTokenEntity::class.java)
        then(refreshTokenRepository).should().save(captor.capture())
        val saved = captor.value
        assertEquals(7, saved.userId)
        assertTrue(saved.tokenHash.isNotBlank())
        // The stored hash must not be the raw token itself.
        assertTrue(saved.tokenHash != raw)
        assertTrue(saved.expiresAt.isAfter(Instant.now()))
    }

    @Test
    fun `rotate revokes the old token and issues a new one`() {
        val existing = RefreshTokenEntity(
            id = 1,
            tokenHash = "ignored",
            userId = 42,
            expiresAt = Instant.now().plusSeconds(3600),
            revoked = false,
            createdAt = Instant.now()
        )
        given(refreshTokenRepository.findByTokenHash(anyString())).willReturn(Optional.of(existing))
        given(refreshTokenRepository.save(any(RefreshTokenEntity::class.java)))
            .willAnswer { it.getArgument(0) }

        val result = service().rotate("some-raw-token")

        assertEquals(42, result.userId)
        assertTrue(existing.revoked, "old token must be revoked on rotation")
        // One save for revoking the old token, one for issuing the new one.
        then(refreshTokenRepository).should(org.mockito.BDDMockito.times(2))
            .save(any(RefreshTokenEntity::class.java))
    }

    @Test
    fun `rotate rejects an unknown token`() {
        given(refreshTokenRepository.findByTokenHash(anyString())).willReturn(Optional.empty())

        assertThrows(InvalidRefreshTokenException::class.java) { service().rotate("nope") }
    }

    @Test
    fun `rotate rejects an expired token`() {
        val expired = RefreshTokenEntity(
            id = 2, tokenHash = "h", userId = 1,
            expiresAt = Instant.now().minusSeconds(10), revoked = false, createdAt = Instant.now()
        )
        given(refreshTokenRepository.findByTokenHash(anyString())).willReturn(Optional.of(expired))

        assertThrows(InvalidRefreshTokenException::class.java) { service().rotate("expired") }
    }

    @Test
    fun `rotate rejects an already revoked token`() {
        val revoked = RefreshTokenEntity(
            id = 3, tokenHash = "h", userId = 1,
            expiresAt = Instant.now().plusSeconds(3600), revoked = true, createdAt = Instant.now()
        )
        given(refreshTokenRepository.findByTokenHash(anyString())).willReturn(Optional.of(revoked))

        assertThrows(InvalidRefreshTokenException::class.java) { service().rotate("revoked") }
    }
}
