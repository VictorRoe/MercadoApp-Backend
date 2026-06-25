package dev.victorroe.mercadoapp.task

import dev.victorroe.mercadoapp.service.TokenDenylistService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RevokedTokenCleanupScheduler(
    private val tokenDenylistService: TokenDenylistService
) {

    @Scheduled(cron = "0 0 * * * *")
    fun purgeExpiredTokens() {
        val deleted = tokenDenylistService.purgeExpired()
        println("Limpieza de tokens revocados completada: $deleted tokens expirados eliminados")
    }
}
