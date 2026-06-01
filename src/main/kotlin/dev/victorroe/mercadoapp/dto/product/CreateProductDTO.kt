package dev.victorroe.mercadoapp.dto.product

import io.swagger.v3.oas.annotations.media.Schema

data class CreateProductDTO(
    @Schema(description = "Nombre del producto")
    val name: String,
    @Schema(description = "Unidad de medida (ej. kg, litro, unidad)")
    val unit: String,
)
