package dev.victorroe.mercadoapp.controller

import tools.jackson.databind.ObjectMapper
import dev.victorroe.mercadoapp.dto.shoppingitems.UpdateShoppingItemDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.UpdateShoppingListDTO
import dev.victorroe.mercadoapp.model.Status
import dev.victorroe.mercadoapp.service.ShoppingListService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.willDoNothing
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(ShoppingListController::class)
class ShoppingListControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var service: ShoppingListService

    private fun activatedList() = ShoppingListDTO(
        id = 1L,
        name = "Compra Semanal",
        status = Status.ACTIVATED,
        supermarketName = "Exito",
        date = "29/05/2026 13:48",
        items = emptyList(),
    )

    @Test
    fun `GET shoppingList returns 200 with all lists`() {
        given(service.findAllLists(null)).willReturn(listOf(activatedList()))

        mockMvc.perform(get("/shoppingList"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Compra Semanal"))
            .andExpect(jsonPath("$[0].status").value("ACTIVATED"))
    }

    @Test
    fun `GET shoppingList with status returns 200 with filtered lists`() {
        given(service.findAllLists(Status.ACTIVATED)).willReturn(listOf(activatedList()))

        mockMvc.perform(get("/shoppingList").param("status", "ACTIVATED"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].status").value("ACTIVATED"))
    }

    @Test
    fun `GET shoppingList history returns 200 with completed lists`() {
        val completedList = ShoppingListDTO(
            id = 2L,
            name = "Compra Terminada",
            status = Status.COMPLETED,
            supermarketName = "Exito",
            date = "28/05/2026 10:00",
            items = emptyList(),
        )
        given(service.findHistory()).willReturn(listOf(completedList))

        mockMvc.perform(get("/shoppingList/history"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].status").value("COMPLETED"))
            .andExpect(jsonPath("$[0].id").value(2))
    }

    @Test
    fun `POST shoppingList id complete returns 200 with COMPLETED status`() {
        val completedList = ShoppingListDTO(
            id = 1L,
            name = "Compra Semanal",
            status = Status.COMPLETED,
            supermarketName = "Exito",
            date = "29/05/2026 13:48",
            items = emptyList(),
        )
        given(service.completeList(1L)).willReturn(completedList)

        mockMvc.perform(post("/shoppingList/1/complete"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(1))
            .andExpect(jsonPath("$.status").value("COMPLETED"))
    }

    @Test
    fun `DELETE shoppingList id returns 204`() {
        willDoNothing().given(service).deleteList(1L)

        mockMvc.perform(delete("/shoppingList/1"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `PATCH shoppingList id returns 200 with updated list`() {
        val updateDto = UpdateShoppingListDTO(name = "Nuevo Nombre", supermarketId = null)
        val updatedList = ShoppingListDTO(
            id = 1L,
            name = "Nuevo Nombre",
            status = Status.ACTIVATED,
            supermarketName = "Exito",
            date = "29/05/2026 13:48",
            items = emptyList(),
        )
        given(service.updateList(1L, updateDto)).willReturn(updatedList)

        mockMvc.perform(
            patch("/shoppingList/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.name").value("Nuevo Nombre"))
    }

    @Test
    fun `DELETE shoppingList id items itemId returns 204`() {
        willDoNothing().given(service).deleteItem(1L, 5L)

        mockMvc.perform(delete("/shoppingList/1/items/5"))
            .andExpect(status().isNoContent)
    }

    @Test
    fun `PATCH shoppingList id items itemId returns 200 with updated item`() {
        val updateDto = UpdateShoppingItemDTO(checked = true)
        val updatedItem = ShoppingItemsDTO(
            id = 5L,
            productName = "Leche",
            productUnit = "Litro",
            quantity = 2,
            checked = true,
        )
        given(service.toggleItem(1L, 5L, updateDto)).willReturn(updatedItem)

        mockMvc.perform(
            patch("/shoppingList/1/items/5")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updateDto))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.id").value(5))
            .andExpect(jsonPath("$.checked").value(true))
    }
}
