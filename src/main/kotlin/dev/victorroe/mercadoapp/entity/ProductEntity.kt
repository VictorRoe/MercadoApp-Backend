package dev.victorroe.mercadoapp.entity

import dev.victorroe.mercadoapp.model.Category
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class ProductEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var name: String? = "",
    var unit: String? = "",
    @Enumerated(EnumType.STRING)
    var category: Category? = null,
) {
}