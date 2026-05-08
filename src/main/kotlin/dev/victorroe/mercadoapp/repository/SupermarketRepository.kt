package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.model.Supermarket
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface SupermarketRepository: JpaRepository<Supermarket, Long> {
}