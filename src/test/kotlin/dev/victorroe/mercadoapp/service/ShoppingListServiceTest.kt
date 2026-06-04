package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.shoppingitems.AddItemRequestDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.UpdateShoppingItemDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.UpdateShoppingListDTO
import dev.victorroe.mercadoapp.model.Status
import org.mockito.Mockito.verify
import dev.victorroe.mercadoapp.entity.ShoppingListEntity
import dev.victorroe.mercadoapp.exception.ResourceNotFoundException
import dev.victorroe.mercadoapp.mapper.ProductMapper
import dev.victorroe.mercadoapp.mapper.ShoppingListMapper
import dev.victorroe.mercadoapp.mapper.SupermarketMapper
import dev.victorroe.mercadoapp.repository.ProductRepository
import dev.victorroe.mercadoapp.repository.ShoppingItemRepository
import dev.victorroe.mercadoapp.repository.ShoppingListRepository
import dev.victorroe.mercadoapp.repository.SupermarketRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ShoppingListServiceTest {

    @Mock lateinit var mapper: ShoppingListMapper
    @Mock lateinit var supermarketRepository: SupermarketRepository
    @Mock lateinit var shoppingListRepository: ShoppingListRepository
    @Mock lateinit var shopppingItemsRepository: ShoppingItemRepository
    @Mock lateinit var productRepository: ProductRepository
    @Mock lateinit var supermarketMapper: SupermarketMapper
    @Mock lateinit var productMapper: ProductMapper

    @InjectMocks
    lateinit var service: ShoppingListService

    @Test
    fun `findListById throws ResourceNotFoundException when list not found`() {
        given(shoppingListRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.findListById(99L)
        }
    }

    @Test
    fun `create throws ResourceNotFoundException when supermarket not found`() {
        val dto = CreateShoppingListDTO(name = "Test List", supermarketId = 99L)
        given(supermarketRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.create(dto)
        }
    }

    @Test
    fun `addItemToList throws ResourceNotFoundException when list not found`() {
        val dto = AddItemRequestDTO(productId = 1L, quantity = 2)
        given(shoppingListRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.addItemToList(99L, dto)
        }
    }

    @Test
    fun `addItemToList throws ResourceNotFoundException when product not found`() {
        val dto = AddItemRequestDTO(productId = 99L, quantity = 2)
        given(shoppingListRepository.findById(1L)).willReturn(Optional.of(ShoppingListEntity()))
        given(productRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.addItemToList(1L, dto)
        }
    }

    @Test
    fun `completeList throws ResourceNotFoundException when list not found`() {
        given(shoppingListRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.completeList(99L)
        }
    }

    @Test
    fun `deleteList throws ResourceNotFoundException when list not found`() {
        given(shoppingListRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.deleteList(99L)
        }
    }

    @Test
    fun `updateList throws ResourceNotFoundException when list not found`() {
        val dto = UpdateShoppingListDTO(name = "New Name", supermarketId = null)
        given(shoppingListRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.updateList(99L, dto)
        }
    }

    @Test
    fun `updateList throws ResourceNotFoundException when supermarket not found`() {
        val dto = UpdateShoppingListDTO(name = null, supermarketId = 99L)
        given(shoppingListRepository.findById(1L)).willReturn(Optional.of(ShoppingListEntity()))
        given(supermarketRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.updateList(1L, dto)
        }
    }

    @Test
    fun `deleteItem throws ResourceNotFoundException when item not found`() {
        given(shopppingItemsRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.deleteItem(1L, 99L)
        }
    }

    @Test
    fun `toggleItem throws ResourceNotFoundException when item not found`() {
        given(shopppingItemsRepository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.toggleItem(1L, 99L, UpdateShoppingItemDTO(checked = true))
        }
    }

    @Test
    fun `findAllLists calls findAll when status is null`() {
        given(shoppingListRepository.findAll()).willReturn(emptyList())

        service.findAllLists(null)

        verify(shoppingListRepository).findAll()
    }

    @Test
    fun `findAllLists calls findByStatus when status is provided`() {
        given(shoppingListRepository.findByStatus(Status.ACTIVATED)).willReturn(emptyList())

        service.findAllLists(Status.ACTIVATED)

        verify(shoppingListRepository).findByStatus(Status.ACTIVATED)
    }
}
