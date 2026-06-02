package dev.victorroe.mercadoapp.model

import io.swagger.v3.oas.annotations.media.Schema

data class Product(
    @Schema(description = "Identificador único del producto")
    val id: Long?,
    @Schema(description = "Nombre del producto")
    val name: String,
    @Schema(description = "Unidad de medida (ej. kg, litro, unidad)")
    val unit: String,
    @Schema(description = "Categoría del producto")
    val category: Category?,
)
