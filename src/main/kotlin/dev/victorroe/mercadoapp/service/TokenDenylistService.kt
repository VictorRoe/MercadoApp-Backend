package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.entity.RevokedTokenEntity
import dev.victorroe.mercadoapp.repository.RevokedTokenRepository
import org.springframework.stereotype.Service
import java.security.MessageDigest
import java.time.Instant

@Service
class TokenDenylistService(
    private val revokedTokenRepository: RevokedTokenRepository,
    private val jwtService: JwtService
) {

    fun revoke(token: String) {
        val hash = hash(token)
        if (revokedTokenRepository.existsByTokenHash(hash)) {
            return
        }
        val expiresAt = jwtService.extractExpiration(token).toInstant()
        revokedTokenRepository.save(RevokedTokenEntity(tokenHash = hash, expiresAt = expiresAt))
    }

    fun isRevoked(token: String): Boolean =
        revokedTokenRepository.existsByTokenHash(hash(token))

    fun purgeExpired(): Int =
        revokedTokenRepository.deleteByExpiresAtBefore(Instant.now())

    private fun hash(token: String): String {
        val digest = MessageDigest.getInstance("SHA-256")
        val bytes = digest.digest(token.toByteArray(Charsets.UTF_8))
        return bytes.joinToString("") { "%02x".format(it) }
    }
}
