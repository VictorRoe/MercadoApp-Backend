package dev.victorroe.mercadoapp.dto.product

import dev.victorroe.mercadoapp.model.Category
import io.swagger.v3.oas.annotations.media.Schema

data class CreateProductDTO(
    @Schema(description = "Nombre del producto")
    val name: String,
    @Schema(description = "Unidad de medida (ej. kg, litro, unidad)")
    val unit: String,
    @Schema(description = "Categoría del producto")
    val category: Category?,
)
