package dev.victorroe.mercadoapp.entity

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.Table
import java.time.Instant

@Entity
@Table(name = "revoked_tokens")
class RevokedTokenEntity(

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(name = "token_hash", nullable = false, unique = true)
    var tokenHash: String = "",

    @Column(name = "expires_at", nullable = false)
    var expiresAt: Instant = Instant.EPOCH
) {
}
