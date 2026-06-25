package dev.victorroe.mercadoapp.dto.auth

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.Size

data class RegisterRequestDTO(
    val fullName: String,

    @field:Email(message = "Email must be a valid email address")
    val email: String,

    @field:Size(min = 5, message = "Password must be at least 5 characters")
    val password: String
)
