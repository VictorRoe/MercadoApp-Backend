package dev.victorroe.mercadoapp.model

import io.swagger.v3.oas.annotations.media.Schema

@Schema(description = "Estado de la lista: ACTIVATED (activa), INACTIVATED (inactiva), COMPLETED (completada), EXPIRED (expirada automáticamente)")
enum class Status {
    EXPIRED, ACTIVATED, INACTIVATED, COMPLETED
}
