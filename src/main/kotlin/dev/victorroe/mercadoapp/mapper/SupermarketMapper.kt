package dev.victorroe.mercadoapp.mapper

import dev.victorroe.mercadoapp.dto.supermarket.CreateSupermarketDTO
import dev.victorroe.mercadoapp.dto.supermarket.SupermarketDTO
import dev.victorroe.mercadoapp.model.Supermarket
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface SupermarketMapper {

    @Mapping(target = "id", ignore = true)
    fun toEntity(dto: CreateSupermarketDTO): Supermarket
    fun toDto(entity: Supermarket): SupermarketDTO

}