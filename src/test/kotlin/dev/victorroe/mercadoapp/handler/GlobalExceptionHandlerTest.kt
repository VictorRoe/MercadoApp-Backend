package dev.victorroe.mercadoapp.handler

import dev.victorroe.mercadoapp.controller.ShoppingListController
import dev.victorroe.mercadoapp.exception.ResourceNotFoundException
import dev.victorroe.mercadoapp.service.ShoppingListService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.hamcrest.Matchers.containsString
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(ShoppingListController::class)
class GlobalExceptionHandlerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var service: ShoppingListService

    @Test
    fun `returns 404 with ProblemDetail when ResourceNotFoundException is thrown`() {
        given(service.findListById(99L))
            .willThrow(ResourceNotFoundException("Shopping list with id 99 not found"))

        mockMvc.perform(get("/shoppingList/99"))
            .andExpect(status().isNotFound)
            .andExpect(jsonPath("$.status").value(404))
            .andExpect(jsonPath("$.detail").value("Shopping list with id 99 not found"))
    }

    @Test
    fun `returns 400 with ProblemDetail when invalid status query param is provided`() {
        mockMvc.perform(get("/shoppingList").param("status", "INVALIDO"))
            .andExpect(status().isBadRequest)
            .andExpect(jsonPath("$.status").value(400))
            .andExpect(jsonPath("$.detail", containsString("INVALIDO")))
            .andExpect(jsonPath("$.detail", containsString("EXPIRED")))
            .andExpect(jsonPath("$.detail", containsString("ACTIVATED")))
            .andExpect(jsonPath("$.detail", containsString("INACTIVATED")))
    }

    @Test
    fun `returns 500 with ProblemDetail for unexpected exceptions`() {
        given(service.findListById(99L))
            .willThrow(RuntimeException("Unexpected error"))

        mockMvc.perform(get("/shoppingList/99"))
            .andExpect(status().isInternalServerError)
            .andExpect(jsonPath("$.status").value(500))
            .andExpect(jsonPath("$.detail").value("An unexpected error occurred"))
    }
}
