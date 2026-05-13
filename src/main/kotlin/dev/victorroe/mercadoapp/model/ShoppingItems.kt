package dev.victorroe.mercadoapp.model

data class ShoppingItems(
    val id: Long? = null,
    val product: Product,
    val quantity: Int,
    val shoppingList: ShoppingList? = null,
)
