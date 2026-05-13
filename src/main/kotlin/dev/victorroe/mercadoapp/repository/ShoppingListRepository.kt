package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.entity.ShoppingListEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ShoppingListRepository: JpaRepository<ShoppingListEntity, Long> {
}