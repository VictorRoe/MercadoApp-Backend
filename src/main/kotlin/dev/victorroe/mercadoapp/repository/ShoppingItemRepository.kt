package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.model.ShoppingItems
import org.springframework.data.jpa.repository.JpaRepository

interface ShoppingItemRepository: JpaRepository<ShoppingItems, Long> {
}