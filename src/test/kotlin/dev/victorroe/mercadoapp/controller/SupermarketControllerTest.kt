package dev.victorroe.mercadoapp.controller

import tools.jackson.databind.ObjectMapper
import dev.victorroe.mercadoapp.dto.supermarket.CreateSupermarketDTO
import dev.victorroe.mercadoapp.dto.supermarket.SupermarketDTO
import dev.victorroe.mercadoapp.service.SupermarketService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@WebMvcTest(SupermarketController::class)
class SupermarketControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @Autowired
    lateinit var objectMapper: ObjectMapper

    @MockitoBean
    lateinit var service: SupermarketService

    private fun supermarketDTO(id: Long = 1L, name: String = "Exito") = SupermarketDTO(id = id, name = name)

    @Test
    fun `GET supermarket returns 200 with all supermarkets`() {
        given(service.findAll()).willReturn(listOf(supermarketDTO(1L, "Exito"), supermarketDTO(2L, "Carulla")))

        mockMvc.perform(get("/supermarket"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$[0].id").value(1))
            .andExpect(jsonPath("$[0].name").value("Exito"))
            .andExpect(jsonPath("$[1].id").value(2))
            .andExpect(jsonPath("$[1].name").value("Carulla"))
    }

    @Test
    fun `GET supermarket returns 200 with empty list when no supermarkets`() {
        given(service.findAll()).willReturn(emptyList())

        mockMvc.perform(get("/supermarket"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$").isArray)
            .andExpect(jsonPath("$").isEmpty)
    }

    @Test
    fun `POST supermarket returns 201 with created supermarket`() {
        val createDto = CreateSupermarketDTO(name = "Jumbo")
        val responseDto = supermarketDTO(3L, "Jumbo")
        given(service.create(createDto)).willReturn(responseDto)

        mockMvc.perform(
            post("/supermarket")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(createDto))
        )
            .andExpect(status().isCreated)
            .andExpect(jsonPath("$.id").value(3))
            .andExpect(jsonPath("$.name").value("Jumbo"))
    }
}
