package dev.victorroe.mercadoapp.exception

class InvalidRefreshTokenException(message: String = "Invalid or expired refresh token") :
    RuntimeException(message)
