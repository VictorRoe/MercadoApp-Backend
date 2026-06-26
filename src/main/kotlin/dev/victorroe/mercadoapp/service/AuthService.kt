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
    private val authenticationManager: AuthenticationManager,
    private val tokenDenylistService: TokenDenylistService,
    private val refreshTokenService: RefreshTokenService
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
        return issueTokens(saved)
    }

    fun login(request: LoginRequestDTO): AuthResponseDTO {
        authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(request.email, request.password)
        )
        val user = userRepository.findByEmail(request.email).orElseThrow()
        return issueTokens(user)
    }

    fun refresh(refreshToken: String): AuthResponseDTO {
        val rotation = refreshTokenService.rotate(refreshToken)
        val user = userRepository.findById(rotation.userId).orElseThrow()
        val accessToken = jwtService.generateToken(
            user.id!!, user.email!!, firstName(user.fullName), user.role!!.name
        )
        return AuthResponseDTO(accessToken, rotation.rawToken, jwtService.accessTokenExpiresInSeconds())
    }

    fun logout(accessToken: String, refreshToken: String?) {
        tokenDenylistService.revoke(accessToken)
        refreshToken?.let { refreshTokenService.revoke(it) }
    }

    private fun issueTokens(user: UserEntity): AuthResponseDTO {
        val accessToken = jwtService.generateToken(
            user.id!!, user.email!!, firstName(user.fullName), user.role!!.name
        )
        val refreshToken = refreshTokenService.issue(user.id!!)
        return AuthResponseDTO(accessToken, refreshToken, jwtService.accessTokenExpiresInSeconds())
    }

    private fun firstName(fullName: String?): String =
        fullName?.trim()?.split(Regex("\\s+"))?.firstOrNull().orEmpty()
}
