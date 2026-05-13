package dev.victorroe.mercadoapp.model

import dev.victorroe.mercadoapp.model.Status.ACTIVATED

import java.time.LocalDateTime

data class ShoppingList(
    val id: Long? = null,
    val name: String,
    val status: Status = ACTIVATED,
    val supermarket: Supermarket?,
    val date: LocalDateTime = LocalDateTime.now(),
    val items: MutableList<ShoppingItems>? = mutableListOf(),
)
