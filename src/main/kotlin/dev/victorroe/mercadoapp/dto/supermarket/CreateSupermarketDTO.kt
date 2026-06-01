package dev.victorroe.mercadoapp.dto.supermarket

import io.swagger.v3.oas.annotations.media.Schema

data class CreateSupermarketDTO(
    @Schema(description = "Nombre del supermercado")
    val name: String,
)
