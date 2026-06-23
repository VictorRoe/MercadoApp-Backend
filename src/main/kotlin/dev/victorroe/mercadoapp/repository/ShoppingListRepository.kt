package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.entity.ShoppingListEntity
import dev.victorroe.mercadoapp.model.Status
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Repository
interface ShoppingListRepository : JpaRepository<ShoppingListEntity, Long> {

    @Modifying
    @Transactional
    @Query("UPDATE ShoppingListEntity s SET s.status = 'EXPIRED' WHERE s.status = 'COMPLETED' AND s.date < :threshold")
    fun updateExpiredLists(threshold: LocalDateTime): Int

    fun findByStatus(status: Status): List<ShoppingListEntity>
}
