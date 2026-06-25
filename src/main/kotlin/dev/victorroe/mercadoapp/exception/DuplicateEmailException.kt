package dev.victorroe.mercadoapp.exception

class DuplicateEmailException(email: String) : RuntimeException("Email already registered: $email")
