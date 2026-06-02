package dev.victorroe.mercadoapp.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Categoría del producto")
enum class Category {
    PRODUCE,
    BAKERY,
    DAIRY,
    MEAT,
    PANTRY,
    FROZEN,
    DRINKS,
    HOME
}
