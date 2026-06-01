package dev.victorroe.mercadoapp.dto.shoppinglist

import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.model.Status
import io.swagger.v3.oas.annotations.media.Schema

data class ShoppingListDTO(
    @Schema(description = "Identificador único de la lista")
    val id: Long?,
    @Schema(description = "Nombre de la lista")
    val name: String?,
    @Schema(description = "Estado actual de la lista")
    val status: Status?,
    @Schema(description = "Nombre del supermercado asociado")
    val supermarketName: String?,
    @Schema(description = "Fecha de creación en formato ISO-8601")
    val date: String?,
    @Schema(description = "Items incluidos en la lista")
    val items: List<ShoppingItemsDTO>?
)
