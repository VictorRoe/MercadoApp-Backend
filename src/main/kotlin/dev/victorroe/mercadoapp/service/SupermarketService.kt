package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.supermarket.CreateSupermarketDTO
import dev.victorroe.mercadoapp.dto.supermarket.SupermarketDTO
import dev.victorroe.mercadoapp.mapper.SupermarketMapper
import dev.victorroe.mercadoapp.model.Supermarket
import dev.victorroe.mercadoapp.repository.SupermarketRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class SupermarketService(
    private val repository: SupermarketRepository,
    private val mapper: SupermarketMapper
) {

    @Transactional
    fun create(dto: CreateSupermarketDTO): SupermarketDTO {
        val entity = mapper.toEntity(dto)
        val saved = repository.save(entity)
        return mapper.toDto(saved)
    }

}