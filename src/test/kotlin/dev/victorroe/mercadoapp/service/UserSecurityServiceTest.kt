package dev.victorroe.mercadoapp.service

import dev.victorroe.mercadoapp.entity.UserEntity
import dev.victorroe.mercadoapp.model.Role
import dev.victorroe.mercadoapp.repository.UserRepository
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.BDDMockito.given
import org.mockito.InjectMocks
import org.mockito.Mock
import org.mockito.junit.jupiter.MockitoExtension
import org.springframework.security.core.userdetails.UsernameNotFoundException
import java.util.Optional

@ExtendWith(MockitoExtension::class)
class UserSecurityServiceTest {

    @Mock lateinit var userRepository: UserRepository

    @InjectMocks
    lateinit var service: UserSecurityService

    @Test
    fun `loads user details with the role as authority`() {
        given(userRepository.findByEmail("ana@mail.com"))
            .willReturn(Optional.of(UserEntity(1L, "Ana", "ana@mail.com", "hash", Role.ADMIN)))

        val details = service.loadUserByUsername("ana@mail.com")

        assertEquals("ana@mail.com", details.username)
        assertEquals("hash", details.password)
        assertEquals("ROLE_ADMIN", details.authorities.single().authority)
    }

    @Test
    fun `throws when the user does not exist`() {
        given(userRepository.findByEmail("nobody@mail.com")).willReturn(Optional.empty())

        assertThrows<UsernameNotFoundException> { service.loadUserByUsername("nobody@mail.com") }
    }
}
