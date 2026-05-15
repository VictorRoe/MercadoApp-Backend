package dev.victorroe.mercadoapp.model

data class User(
    val fullName: String,
    val email: String,
    val password: String,
    val role: Role,
    val id: Long?,
)
