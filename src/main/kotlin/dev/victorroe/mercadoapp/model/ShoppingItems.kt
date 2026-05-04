package dev.victorroe.mercadoapp.model

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "shopping_items")
data class ShoppingItems(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,
/*
    relacionarlos con su IDs

    val shoppingList: ShoppingList
    val product: Product
 */
    val quantity: Int,
)
