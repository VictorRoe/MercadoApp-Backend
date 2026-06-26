package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.entity.RefreshTokenEntity
import dev.victorroe.mercadoapp.exception.InvalidRefreshTokenException
import dev.victorroe.mercadoapp.repository.RefreshTokenRepository
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.security.SecureRandom
import java.time.Instant
import java.util.Base64

/** Result of rotating a refresh token: the new raw token and the owning user id. */
data class RotationResult(val rawToken: String, val userId: Long)

@Service
class RefreshTokenService(
    private val refreshTokenRepository: RefreshTokenRepository,
    @Value("\${app.jwt.refresh-expiration}") private val refreshExpiration: Long
) {

    private val secureRandom = SecureRandom()
    private val encoder = Base64.getUrlEncoder().withoutPadding()

    /** Generate an opaque refresh token, persist its hash for [userId], and return the raw value. */
    fun issue(userId: Long): String {
        val rawToken = generateRawToken()
        val now = Instant.now()
        refreshTokenRepository.save(
            RefreshTokenEntity(
                tokenHash = hash(rawToken),
                userId = userId,
                expiresAt = now.plusMillis(refreshExpiration),
                revoked = false,
                createdAt = now
            )
        )
        return rawToken
    }

    /** Validate and consume [rawToken], revoking it and issuing a fresh one (single-use rotation). */
    fun rotate(rawToken: String): RotationResult {
        val entity = refreshTokenRepository.findByTokenHash(hash(rawToken))
            .orElseThrow { InvalidRefreshTokenException() }

        if (entity.revoked || entity.expiresAt.isBefore(Instant.now())) {
            throw InvalidRefreshTokenException()
        }

        entity.revoked = true
        refreshTokenRepository.save(entity)

        return RotationResult(rawToken = issue(entity.userId), userId = entity.userId)
    }

    /** Revoke a refresh token if present. Idempotent: unknown tokens are ignored. */
    fun revoke(rawToken: String) {
        refreshTokenRepository.findByTokenHash(hash(rawToken)).ifPresent {
            if (!it.revoked) {
                it.revoked = true
                refreshTokenRepository.save(it)
            }
        }
    }

    /** Delete expired or revoked refresh tokens. Returns the number of rows removed. */
    fun purgeExpired(): Int =
        refreshTokenRepository.deleteByExpiresAtBeforeOrRevokedTrue(Instant.now())

    private fun generateRawToken(): String {
        val bytes = ByteArray(32)
        secureRandom.nextBytes(bytes)
        return encoder.encodeToString(bytes)
    }

    private fun hash(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(token.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
