package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.shoppingitems.AddItemRequestDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.UpdateShoppingItemDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.model.Status
import dev.victorroe.mercadoapp.model.Status.*
import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.UpdateShoppingListDTO
import dev.victorroe.mercadoapp.exception.ResourceNotFoundException
import dev.victorroe.mercadoapp.mapper.ProductMapper
import dev.victorroe.mercadoapp.mapper.ShoppingListMapper
import dev.victorroe.mercadoapp.mapper.SupermarketMapper
import dev.victorroe.mercadoapp.model.ShoppingItems
import dev.victorroe.mercadoapp.model.ShoppingList
import dev.victorroe.mercadoapp.repository.ProductRepository
import dev.victorroe.mercadoapp.repository.ShoppingItemRepository
import dev.victorroe.mercadoapp.repository.ShoppingListRepository
import dev.victorroe.mercadoapp.repository.SupermarketRepository
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
            .orElseThrow { ResourceNotFoundException("Shopping list with id $listId not found") }
        return mapper.toDTO(mapper.toModel(entity))
    }

    @Transactional
    fun create(dto: CreateShoppingListDTO): ShoppingListDTO {
        val supermarketEntity = supermarketRepository.findById(dto.supermarketId)
            .orElseThrow { ResourceNotFoundException("Supermarket with id ${dto.supermarketId} not found") }

        val model = ShoppingList(
            name = dto.name,
            status = ACTIVATED,
            date = LocalDateTime.now(),
            supermarket = supermarketMapper.toModel(supermarketEntity),
        )

        val savedEntity = shoppingListRepository.save(mapper.toEntity(model))
        return mapper.toDTO(mapper.toModel(savedEntity))
    }

    @Transactional
    fun addItemToList(listId: Long, dto: AddItemRequestDTO) {
        val listEntity = shoppingListRepository.findById(listId)
            .orElseThrow { ResourceNotFoundException("Shopping list with id $listId not found") }

        val productEntity = productRepository.findById(dto.productId)
            .orElseThrow { ResourceNotFoundException("Product with id ${dto.productId} not found") }

        val newItemModel = ShoppingItems(
            shoppingList = mapper.toModel(listEntity),
            product = productMapper.toModel(productEntity),
            quantity = dto.quantity,
        )

        shopppingItemsRepository.save(mapper.toItemEntity(newItemModel))
    }

    @Transactional(readOnly = true)
    fun findAllLists(status: Status? = null): List<ShoppingListDTO> {
        val entities = if (status != null) {
            shoppingListRepository.findByStatus(status)
        } else {
            shoppingListRepository.findAll()
        }
        return entities
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
            .orElseThrow { ResourceNotFoundException("Shopping list with id $id not found") }
        entity.status = COMPLETED
        val saved = shoppingListRepository.save(entity)
        return mapper.toDTO(mapper.toModel(saved))
    }

    @Transactional
    fun deleteList(id: Long) {
        val entity = shoppingListRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Shopping list with id $id not found") }
        shoppingListRepository.delete(entity)
    }

    @Transactional
    fun updateList(id: Long, dto: UpdateShoppingListDTO): ShoppingListDTO {
        val entity = shoppingListRepository.findById(id)
            .orElseThrow { ResourceNotFoundException("Shopping list with id $id not found") }

        dto.name?.let { entity.name = it }
        dto.supermarketId?.let { supermarketId ->
            entity.supermarket = supermarketRepository.findById(supermarketId)
                .orElseThrow { ResourceNotFoundException("Supermarket with id $supermarketId not found") }
        }

        val saved = shoppingListRepository.save(entity)
        return mapper.toDTO(mapper.toModel(saved))
    }

    @Transactional
    fun deleteItem(listId: Long, itemId: Long) {
        val item = shopppingItemsRepository.findById(itemId)
            .orElseThrow { ResourceNotFoundException("Item with id $itemId not found") }
        shopppingItemsRepository.delete(item)
    }

    @Transactional
    fun toggleItem(listId: Long, itemId: Long, dto: UpdateShoppingItemDTO): ShoppingItemsDTO {
        val item = shopppingItemsRepository.findById(itemId)
            .orElseThrow { ResourceNotFoundException("Item with id $itemId not found") }
        item.checked = dto.checked
        val saved = shopppingItemsRepository.save(item)
        return mapper.toShoppingItemDTO(mapper.toItemModel(saved))
    }
}
