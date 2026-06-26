package dev.victorroe.mercadoapp.task

import dev.victorroe.mercadoapp.service.RefreshTokenService
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class RefreshTokenCleanupScheduler(
    private val refreshTokenService: RefreshTokenService
) {

    @Scheduled(cron = "0 0 * * * *")
    fun purgeExpiredTokens() {
        val deleted = refreshTokenService.purgeExpired()
        println("Limpieza de refresh tokens completada: $deleted tokens expirados o revocados eliminados")
    }
}
