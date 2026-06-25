package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.entity.RevokedTokenEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Repository
interface RevokedTokenRepository : JpaRepository<RevokedTokenEntity, Long> {

    fun existsByTokenHash(tokenHash: String): Boolean

    @Modifying
    @Transactional
    fun deleteByExpiresAtBefore(instant: Instant): Int
}
