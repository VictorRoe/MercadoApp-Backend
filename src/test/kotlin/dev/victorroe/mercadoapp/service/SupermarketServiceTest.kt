package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.dto.supermarket.CreateSupermarketDTO
import dev.victorroe.mercadoapp.entity.SupermarketEntity
import dev.victorroe.mercadoapp.mapper.SupermarketMapperImpl
import dev.victorroe.mercadoapp.repository.SupermarketRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.ArgumentMatchers.any
import org.mockito.BDDMockito.given
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension

@ExtendWith(MockitoExtension::class)
class SupermarketServiceTest {

    @Mock lateinit var repository: SupermarketRepository

    private fun service() = SupermarketService(repository, SupermarketMapperImpl())

    @Test
    fun `create persists the supermarket and returns its dto`() {
        given(repository.save(any(SupermarketEntity::class.java))).willReturn(SupermarketEntity(1L, "Exito"))

        val result = service().create(CreateSupermarketDTO("Exito"))

        assertEquals(1L, result.id)
        assertEquals("Exito", result.name)
    }

    @Test
    fun `findAll maps every stored supermarket`() {
        given(repository.findAll())
            .willReturn(listOf(SupermarketEntity(1L, "Exito"), SupermarketEntity(2L, "Carulla")))

        val result = service().findAll()

        assertEquals(listOf("Exito", "Carulla"), result.map { it.name })
    }
}
