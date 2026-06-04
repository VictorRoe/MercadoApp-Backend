package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.exception.ResourceNotFoundException
import dev.victorroe.mercadoapp.mapper.ProductMapper
import dev.victorroe.mercadoapp.repository.ProductRepository
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class ProductServiceTest {

    @Mock lateinit var repository: ProductRepository
    @Mock lateinit var mapper: ProductMapper

    @InjectMocks
    lateinit var service: ProductService

    @Test
    fun `findById throws ResourceNotFoundException when product not found`() {
        given(repository.findById(99L)).willReturn(Optional.empty())

        assertThrows<ResourceNotFoundException> {
            service.findById(99L)
        }
    }
}
