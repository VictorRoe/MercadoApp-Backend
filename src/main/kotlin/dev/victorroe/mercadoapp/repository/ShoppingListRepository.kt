package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.entity.ShoppingListEntity
import jakarta.transaction.Transactional
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import java.time.LocalDateTime

@Repository
interface ShoppingListRepository: JpaRepository<ShoppingListEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ShoppingListEntity s SET s.status = 'EXPIRED' WHERE s.status = 'ACTIVATED' AND s.date < :threshold")
    fun updateExpiredLists(threshold: LocalDateTime): Int
}