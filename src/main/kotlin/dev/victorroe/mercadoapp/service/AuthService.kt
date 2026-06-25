package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.auth.AuthResponseDTO
import dev.victorroe.mercadoapp.dto.auth.LoginRequestDTO
import dev.victorroe.mercadoapp.dto.auth.RegisterRequestDTO
import dev.victorroe.mercadoapp.entity.UserEntity
import dev.victorroe.mercadoapp.exception.DuplicateEmailException
import dev.victorroe.mercadoapp.model.Role
import dev.victorroe.mercadoapp.repository.UserRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service

@Service
class AuthService(
    private val userRepository: UserRepository,
    private val passwordEncoder: PasswordEncoder,
    private val jwtService: JwtService,
    private val authenticationManager: AuthenticationManager
) {

    fun register(request: RegisterRequestDTO): AuthResponseDTO {
        if (userRepository.existsByEmail(request.email)) {
            throw DuplicateEmailException(request.email)
        }
        val user = UserEntity(
            fullName = request.fullName,
            email = request.email,
            password = passwordEncoder.encode(request.password),
            role = Role.USER
        )
        val saved = userRepository.save(user)
        val token = jwtService.generateToken(saved.id!!, saved.email!!, saved.role!!.name)
        return AuthResponseDTO(token)
    }

    fun login(request: LoginRequestDTO): AuthResponseDTO {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email).orElseThrow()
        val token = jwtService.generateToken(user.id!!, user.email!!, user.role!!.name)
        return AuthResponseDTO(token)
    }
}
