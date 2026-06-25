package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.dto.auth.AuthResponseDTO
import dev.victorroe.mercadoapp.dto.auth.LoginRequestDTO
import dev.victorroe.mercadoapp.dto.auth.RegisterRequestDTO
import dev.victorroe.mercadoapp.service.AuthService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.validation.Valid
import org.springframework.http.HttpHeaders
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestHeader
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "Authentication", description = "Registro e inicio de sesión")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    @Operation(summary = "Registrar un nuevo usuario")
    fun register(@Valid @RequestBody dto: RegisterRequestDTO): ResponseEntity<AuthResponseDTO> =
        ResponseEntity.status(HttpStatus.CREATED).body(authService.register(dto))

    @PostMapping("/login")
    @Operation(summary = "Iniciar sesión y obtener JWT")
    fun login(@RequestBody dto: LoginRequestDTO): ResponseEntity<AuthResponseDTO> =
        ResponseEntity.ok(authService.login(dto))

    @PostMapping("/logout")
    @Operation(summary = "Cerrar sesión y revocar el token actual")
    fun logout(
        @RequestHeader(HttpHeaders.AUTHORIZATION, required = false) authHeader: String?
    ): ResponseEntity<Void> {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build()
        }
        authService.logout(authHeader.substring(7))
        return ResponseEntity.ok().build()
    }
}
