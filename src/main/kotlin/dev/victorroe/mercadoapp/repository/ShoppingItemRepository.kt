package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.entity.ShoppingItemEntity
import org.springframework.data.jpa.repository.JpaRepository

interface ShoppingItemRepository: JpaRepository<ShoppingItemEntity, Long> {
}