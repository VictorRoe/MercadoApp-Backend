package dev.victorroe.mercadoapp.dto.shoppingitems

import io.swagger.v3.oas.annotations.media.Schema

data class AddItemRequestDTO(
    @Schema(description = "ID del producto a agregar")
    val productId: Long,
    @Schema(description = "Cantidad del producto")
    val quantity: Int,
)
