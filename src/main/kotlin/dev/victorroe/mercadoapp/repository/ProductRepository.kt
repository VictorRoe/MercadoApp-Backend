package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.entity.ProductEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository

@Repository
interface ProductRepository: JpaRepository<ProductEntity, Long> {
}