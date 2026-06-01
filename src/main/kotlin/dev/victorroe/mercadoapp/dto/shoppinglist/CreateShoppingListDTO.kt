package dev.victorroe.mercadoapp.dto.shoppinglist

import io.swagger.v3.oas.annotations.media.Schema

data class CreateShoppingListDTO(
    @Schema(description = "Nombre de la lista de compras")
    val name: String,
    @Schema(description = "ID del supermercado asociado")
    val supermarketId: Long,
)
