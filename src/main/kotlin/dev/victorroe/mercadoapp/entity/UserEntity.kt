package dev.victorroe.mercadoapp.entity

import dev.victorroe.mercadoapp.model.Role
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "users")
class UserEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,
    var fullName: String? = "",
    var email: String? = "",
    var password: String? = "",
    @Enumerated(EnumType.STRING)
    var role: Role? = null
) {
}