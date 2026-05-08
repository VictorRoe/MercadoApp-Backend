package dev.victorroe.mercadoapp.dto.shoppinglist

import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.model.ShoppingItems
import dev.victorroe.mercadoapp.model.Status
import java.time.LocalDateTime

data class ShoppingListDTO(
    val id: Long?,
    val name: String?,
    val status: Status?,
    val supermarketName: String,
    val date: LocalDateTime?,
    val items: List<ShoppingItemsDTO>?
)
