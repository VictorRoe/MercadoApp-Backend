package dev.victorroe.mercadoapp.mapper

import dev.victorroe.mercadoapp.dto.product.CreateProductDTO
import dev.victorroe.mercadoapp.dto.product.ProductDTO
import dev.victorroe.mercadoapp.entity.ProductEntity
import dev.victorroe.mercadoapp.model.Product
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ProductMapper {

    @Mapping(target = "id", ignore = true)
    fun toModel(dto: CreateProductDTO): Product

    fun toDTO(model: Product): ProductDTO

    fun toEntity(model : Product): ProductEntity

    fun toModel(dto: ProductEntity): Product

    fun toModelList(entities: List<ProductEntity>): List<Product>
}