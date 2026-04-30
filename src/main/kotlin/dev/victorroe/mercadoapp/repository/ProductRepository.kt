package dev.victorroe.mercadoapp.repository

import dev.victorroe.mercadoapp.model.Product
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.config.EnableJpaRepositories
import org.springframework.stereotype.Repository

@Repository
@EnableJpaRepositories
interface ProductRepository: JpaRepository<Product, Long> {
}