package dev.victorroe.mercadoapp.entity

import dev.victorroe.mercadoapp.model.Status.ACTIVATED

import dev.victorroe.mercadoapp.model.Status
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
class ShoppingListEntity(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = "",
    @Enumerated(EnumType.STRING)
    var status: Status? = ACTIVATED,
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "supermarket_id")
    var supermarket: SupermarketEntity? = null,
    var date: LocalDateTime? = LocalDateTime.now(),
    @OneToMany(mappedBy = "shoppingList", cascade = [CascadeType.ALL], orphanRemoval = true)
    var items: MutableList<ShoppingItemEntity>? = mutableListOf(),
) {
}