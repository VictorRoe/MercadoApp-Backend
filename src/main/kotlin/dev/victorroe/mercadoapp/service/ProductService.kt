package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.mapper.ProductMapper
import dev.victorroe.mercadoapp.model.Product
import dev.victorroe.mercadoapp.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class ProductService(
    private val repository: ProductRepository,
    private val mapper: ProductMapper,
    ) {

    fun findAll(): List<Product> = mapper.toModelList(repository.findAll())

    fun findById(id: Long): Product? {
        val entity = repository.findById(id).orElse(null)
        return entity?.let { mapper.toModel(it) }
    }

    fun create(product: Product): Product{

        val entity = mapper.toEntity(product)
        val saved = repository.save(entity)
        return mapper.toModel(saved)
    }
}