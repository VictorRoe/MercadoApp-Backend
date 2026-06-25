package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.config.JwtAuthFilter
import dev.victorroe.mercadoapp.service.AuthService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.willDoNothing
import org.mockito.Mockito.never
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@WebMvcTest(
    controllers = [AuthController::class],
    excludeFilters = [
        ComponentScan.Filter(type = FilterType.ASSIGNABLE_TYPE, classes = [JwtAuthFilter::class])
    ]
)
class AuthControllerTest {

    @Autowired
    lateinit var mockMvc: MockMvc

    @MockitoBean
    lateinit var authService: AuthService

    @Test
    fun `POST logout with bearer token revokes it and returns 200`() {
        willDoNothing().given(authService).logout("the-token")

        mockMvc.perform(
            post("/auth/logout").header(HttpHeaders.AUTHORIZATION, "Bearer the-token")
        )
            .andExpect(status().isOk)

        then(authService).should().logout("the-token")
    }

    @Test
    fun `POST logout without authorization header returns 401`() {
        mockMvc.perform(post("/auth/logout"))
            .andExpect(status().isUnauthorized)

        then(authService).should(never()).logout(org.mockito.ArgumentMatchers.anyString())
    }
}
