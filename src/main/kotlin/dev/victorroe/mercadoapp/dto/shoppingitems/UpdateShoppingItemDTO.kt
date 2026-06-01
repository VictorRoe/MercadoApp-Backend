package dev.victorroe.mercadoapp.dto.shoppingitems

import io.swagger.v3.oas.annotations.media.Schema

data class UpdateShoppingItemDTO(
    @Schema(description = "true si el item fue comprado, false si no")
    val checked: Boolean,
)
