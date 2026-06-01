package dev.victorroe.mercadoapp.dto.supermarket

import io.swagger.v3.oas.annotations.media.Schema

data class SupermarketDTO(
    @Schema(description = "Identificador único del supermercado")
    val id: Long? = null,
    @Schema(description = "Nombre del supermercado")
    val name: String?,
)
