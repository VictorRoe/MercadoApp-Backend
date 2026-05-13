package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.shoppingitems.AddItemRequestDTO
import dev.victorroe.mercadoapp.model.Status.*

import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.mapper.ProductMapper
import dev.victorroe.mercadoapp.mapper.ShoppingListMapper
import dev.victorroe.mercadoapp.mapper.SupermarketMapper
import dev.victorroe.mercadoapp.model.ShoppingItems
import dev.victorroe.mercadoapp.model.ShoppingList
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
    private val productRepository: ProductRepository,
    private val supermarketMapper: SupermarketMapper,
    private val productMapper: ProductMapper,
) {

    fun findListById(listId: Long): ShoppingListDTO {
        val entity = shoppingListRepository.findById(listId)
            .orElseThrow{ RuntimeException("No shopping list found for id $listId") }

        val model = mapper.toModel(entity)

        return mapper.toDTO(model)
    }

    @Transactional
    fun create(dto: CreateShoppingListDTO): ShoppingListDTO {
        val supermarketEntity = supermarketRepository.findById(dto.supermarketId)
            .orElseThrow{ EntityNotFoundException("Supermarket ID no existe: ${dto.supermarketId}") }

        val supermarketModel = supermarketMapper.toModel(supermarketEntity)

        val model = ShoppingList(
            name = dto.name,
            status = ACTIVATED,
            date = LocalDateTime.now(),
            supermarket = supermarketModel,
        )

        val savedEntity = shoppingListRepository.save(mapper.toEntity(model))
        val savedModel = mapper.toModel(savedEntity)

        return mapper.toDTO(savedModel)
    }

    @Transactional
    fun addItemToList(listId: Long, dto: AddItemRequestDTO){

       val listEntity = shoppingListRepository.findById(listId)
        .orElseThrow{ RuntimeException("No shopping list found for id $listId") }

        val productEntity = productRepository.findById(dto.productId)
        .orElseThrow{ EntityNotFoundException("Product with id $dto.productId not found") }

        val newItemModel = ShoppingItems(
            shoppingList = mapper.toModel(listEntity),
            product = productMapper.toModel(productEntity),
            quantity = dto.quantity,
        )

        shopppingItemsRepository.save(mapper.toItemEntity(newItemModel))
    }


}