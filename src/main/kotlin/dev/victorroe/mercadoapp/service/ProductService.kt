package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.mapper.ProductMapper
import dev.victorroe.mercadoapp.model.Product
import dev.victorroe.mercadoapp.repository.ProductRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class ProductService(
    private val repository: ProductRepository,
    private val mapper: ProductMapper,
    ) {

    @Transactional(readOnly = true)
    fun findAll(): List<Product> = mapper.toModelList(repository.findAll())

    @Transactional(readOnly = true)
    fun findById(id: Long): Product? {
        val entity = repository.findById(id).orElse(null)
        return entity?.let { mapper.toModel(it) }
    }

    @Transactional
    fun create(product: Product): Product{

        val entity = mapper.toEntity(product)
        val saved = repository.save(entity)
        return mapper.toModel(saved)
    }
}