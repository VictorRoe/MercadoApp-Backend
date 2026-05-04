package dev.victorroe.mercadoapp.model

import jakarta.persistence.Entity
import jakarta.persistence.Table

@Entity
@Table(name = "users")
data class User(

    val id: Long?,
    val fullName: String,
    val email: String,
    val password: String,
    val role: Role,
)
