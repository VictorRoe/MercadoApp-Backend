package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.shoppingitems.AddItemRequestDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.UpdateShoppingItemDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.model.Status.*

import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.UpdateShoppingListDTO
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

// TODO: Migrar mas adelante cuando las listas crezcan a una QUERY en repositories
    @Transactional(readOnly = true)
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

    @Transactional(readOnly = true)
    fun findAllLists(): List<ShoppingListDTO> {
        return shoppingListRepository.findAll()
            .map { mapper.toModel(it) }
            .map { mapper.toDTO(it) }
    }

    @Transactional(readOnly = true)
    fun findHistory(): List<ShoppingListDTO> {
        return shoppingListRepository.findByStatus(COMPLETED)
            .map { mapper.toModel(it) }
            .map { mapper.toDTO(it) }
    }

    @Transactional
    fun completeList(id: Long): ShoppingListDTO {
        val entity = shoppingListRepository.findById(id)
            .orElseThrow { EntityNotFoundException("No shopping list found for id $id") }
        entity.status = COMPLETED
        val saved = shoppingListRepository.save(entity)
        return mapper.toDTO(mapper.toModel(saved))
    }

    @Transactional
    fun deleteList(id: Long) {
        val entity = shoppingListRepository.findById(id)
            .orElseThrow { EntityNotFoundException("No shopping list found for id $id") }
        shoppingListRepository.delete(entity)
    }

    @Transactional
    fun updateList(id: Long, dto: UpdateShoppingListDTO): ShoppingListDTO {
        val entity = shoppingListRepository.findById(id)
            .orElseThrow { EntityNotFoundException("No shopping list found for id $id") }

        dto.name?.let { entity.name = it }
        dto.supermarketId?.let { supermarketId ->
            entity.supermarket = supermarketRepository.findById(supermarketId)
                .orElseThrow { EntityNotFoundException("Supermarket ID no existe: $supermarketId") }
        }

        val saved = shoppingListRepository.save(entity)
        return mapper.toDTO(mapper.toModel(saved))
    }

    @Transactional
    fun deleteItem(listId: Long, itemId: Long) {
        val item = shopppingItemsRepository.findById(itemId)
            .orElseThrow { EntityNotFoundException("Item with id $itemId not found") }
        shopppingItemsRepository.delete(item)
    }

    @Transactional
    fun toggleItem(listId: Long, itemId: Long, dto: UpdateShoppingItemDTO): ShoppingItemsDTO {
        val item = shopppingItemsRepository.findById(itemId)
            .orElseThrow { EntityNotFoundException("Item with id $itemId not found") }
        item.checked = dto.checked
        val saved = shopppingItemsRepository.save(item)
        return mapper.toShoppingItemDTO(mapper.toItemModel(saved))
    }

}