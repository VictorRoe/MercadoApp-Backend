package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.entity.RefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant
import java.util.Optional

@Repository
interface RefreshTokenRepository : JpaRepository<RefreshTokenEntity, Long> {

    fun findByTokenHash(tokenHash: String): Optional<RefreshTokenEntity>

    @Modifying
    @Transactional
    fun deleteByExpiresAtBeforeOrRevokedTrue(instant: Instant): Int
}
