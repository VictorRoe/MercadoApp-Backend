package dev.victorroe.mercadoapp.dto.shoppinglist

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateShoppingListDTO(
    @Schema(description = "Nuevo nombre de la lista (opcional)")
    val name: String?,
    @Schema(description = "ID del nuevo supermercado (opcional)")
    val supermarketId: Long?,
)
