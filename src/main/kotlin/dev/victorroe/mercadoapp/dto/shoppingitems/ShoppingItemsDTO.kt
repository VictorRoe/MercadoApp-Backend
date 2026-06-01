package dev.victorroe.mercadoapp.dto.shoppingitems

import io.swagger.v3.oas.annotations.media.Schema

data class ShoppingItemsDTO(
    @Schema(description = "Identificador único del item")
    val id: Long?,
    @Schema(description = "Nombre del producto")
    val productName: String?,
    @Schema(description = "Unidad de medida del producto (ej. kg, unidad, litro)")
    val productUnit: String?,
    @Schema(description = "Cantidad del producto en la lista")
    val quantity: Int,
    @Schema(description = "Indica si el item fue comprado")
    val checked: Boolean,
)
