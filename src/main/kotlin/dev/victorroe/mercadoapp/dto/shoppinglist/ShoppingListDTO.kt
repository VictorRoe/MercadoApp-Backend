package dev.victorroe.mercadoapp.dto.shoppinglist

import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.model.Status

data class ShoppingListDTO(
    val id: Long?,
    val name: String?,
    val status: Status?,
    val supermarketName: String?,
    val date: String?,
    val items: List<ShoppingItemsDTO>?
)
