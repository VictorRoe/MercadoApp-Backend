package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.model.Product
import dev.victorroe.mercadoapp.repository.ProductRepository
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductService(private val repository: ProductRepository) {

    fun findAll(): List<Product> = repository.findAll().toList()

    fun findProductById(id:Long): Product? = repository.findByIdOrNull(id)

    fun save(product: Product): Product = repository.save(product)
}