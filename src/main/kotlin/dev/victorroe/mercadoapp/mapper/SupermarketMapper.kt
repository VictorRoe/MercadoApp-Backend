package dev.victorroe.mercadoapp.mapper

import dev.victorroe.mercadoapp.dto.supermarket.CreateSupermarketDTO
import dev.victorroe.mercadoapp.dto.supermarket.SupermarketDTO
import dev.victorroe.mercadoapp.entity.SupermarketEntity
import dev.victorroe.mercadoapp.model.Supermarket
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface SupermarketMapper {

    @Mapping(target = "id", ignore = true)
    fun toModel(dto: CreateSupermarketDTO): Supermarket

    fun toEntity(model: Supermarket): SupermarketEntity

    fun toModel(entity: SupermarketEntity): Supermarket

    @Mapping(target = "name", defaultValue = "Paso Null")
    fun toDto(model: Supermarket): SupermarketDTO

}