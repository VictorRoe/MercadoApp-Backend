package dev.victorroe.mercadoapp.model

import jakarta.persistence.CascadeType
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.Table
import java.time.LocalDateTime

@Entity
@Table(name = "shopping_list")
data class ShoppingList(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Long?,

    val name: String?,

    @Enumerated(EnumType.STRING)
    val status: Status?,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supermarket_id")
    val supermarket: Supermarket?,

    val date: LocalDateTime?,

    @OneToMany(mappedBy = "shoppingList", cascade = [CascadeType.ALL], orphanRemoval = true)
    val items: MutableList<ShoppingItems>? = mutableListOf()
)
