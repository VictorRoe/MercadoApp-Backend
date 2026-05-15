package dev.victorroe.mercadoapp.task

import dev.victorroe.mercadoapp.repository.ShoppingListRepository
import jakarta.transaction.Transactional
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class WeeklyExpirationScheduler(
    private val repository: ShoppingListRepository
) {

    @Scheduled(cron = "0 0 0 * * MON")
    @Transactional
    fun expiredAllListEveryMonday(){
        val now = LocalDateTime.now()
        val updatedCount = repository.updateExpiredLists(now)

        println("Limpieza semanal completada: $updatedCount listas EXPIRED")
    }

}