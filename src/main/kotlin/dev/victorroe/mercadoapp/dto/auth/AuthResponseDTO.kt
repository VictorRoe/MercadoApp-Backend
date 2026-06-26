package dev.victorroe.mercadoapp.dto.auth

data class AuthResponseDTO(
    val accessToken: String,
    val refreshToken: String,
    val expiresIn: Long
)
