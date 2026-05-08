package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.shoppingitems.AddItemRequestDTO
import dev.victorroe.mercadoapp.model.Status.*

import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.mapper.ShoppingListMapper
import dev.victorroe.mercadoapp.model.ShoppingItems
import dev.victorroe.mercadoapp.repository.ProductRepository
import dev.victorroe.mercadoapp.repository.ShoppingItemRepository
import dev.victorroe.mercadoapp.repository.ShoppingListRepository
import dev.victorroe.mercadoapp.repository.SupermarketRepository
import jakarta.persistence.EntityNotFoundException
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDateTime

@Service
class ShoppingListService(
    private val mapper: ShoppingListMapper,
    private val supermarketRepository: SupermarketRepository,
    private val shoppingListRepository: ShoppingListRepository,
    private val shopppingItemsRepository: ShoppingItemRepository,
    private val productRepository: ProductRepository
) {

    fun findListById(listId: Long): ShoppingListDTO {
        val entity = shoppingListRepository.findById(listId)
            .orElseThrow{ RuntimeException("No shopping list found for id $listId") }

        return mapper.toDTO(entity)
    }

    @Transactional
    fun create(dto: CreateShoppingListDTO): ShoppingListDTO {
        val supermarketId = supermarketRepository.findById(dto.supermarketId)
            .orElseThrow{ EntityNotFoundException("Supermarket ID no existe: ${dto.supermarketId}") }

        val initialEntity = mapper.toEntity(dto)

        val entityToSave = initialEntity.copy(
            supermarket = supermarketId,
            status = ACTIVATED,
            date = LocalDateTime.now(),
        )

        return mapper.toDTO(shoppingListRepository.save(entityToSave))
    }

    @Transactional
    fun addItemToList(listId: Long, dto: AddItemRequestDTO){

        val shoppingList = shoppingListRepository.findById(listId)
            .orElseThrow{ RuntimeException("No shopping list found for id $listId") }

        val product = productRepository.findById(dto.productId)
            .orElseThrow{ RuntimeException("No product found for id $listId") }

        val newItem = ShoppingItems(
            shoppingList = shoppingList,
            product = product,
            quantity = dto.quantity
        )

        shopppingItemsRepository.save(newItem)
    }


}