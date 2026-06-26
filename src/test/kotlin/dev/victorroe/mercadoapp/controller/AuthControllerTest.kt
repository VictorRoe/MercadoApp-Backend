package dev.victorroe.mercadoapp.controller

import com.fasterxml.jackson.databind.ObjectMapper
import dev.victorroe.mercadoapp.config.JwtAuthFilter
import dev.victorroe.mercadoapp.dto.auth.AuthResponseDTO
import dev.victorroe.mercadoapp.dto.auth.RefreshRequestDTO
import dev.victorroe.mercadoapp.exception.InvalidRefreshTokenException
import dev.victorroe.mercadoapp.service.AuthService
import org.junit.jupiter.api.Test
import org.mockito.BDDMockito.given
import org.mockito.BDDMockito.then
import org.mockito.BDDMockito.willThrow
import org.mockito.Mockito.never
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest
import org.springframework.context.annotation.ComponentScan
import org.springframework.context.annotation.FilterType
import org.springframework.http.HttpHeaders
import org.springframework.http.MediaType
import org.springframework.test.context.bean.override.mockito.MockitoBean
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath
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

    private val objectMapper = ObjectMapper()

    @MockitoBean
    lateinit var authService: AuthService

    @Test
    fun `POST refresh returns a new token pair`() {
        given(authService.refresh("the-refresh"))
            .willReturn(AuthResponseDTO("new-access", "new-refresh", 900))

        mockMvc.perform(
            post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RefreshRequestDTO("the-refresh")))
        )
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.accessToken").value("new-access"))
            .andExpect(jsonPath("$.refreshToken").value("new-refresh"))
            .andExpect(jsonPath("$.expiresIn").value(900))

        then(authService).should().refresh("the-refresh")
    }

    @Test
    fun `POST refresh with an invalid token returns 401`() {
        willThrow(InvalidRefreshTokenException()).given(authService).refresh("bad")

        mockMvc.perform(
            post("/auth/refresh")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RefreshRequestDTO("bad")))
        )
            .andExpect(status().isUnauthorized)
    }

    @Test
    fun `POST logout with bearer token and refresh body revokes both and returns 200`() {
        mockMvc.perform(
            post("/auth/logout")
                .header(HttpHeaders.AUTHORIZATION, "Bearer the-token")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(RefreshRequestDTO("the-refresh")))
        )
            .andExpect(status().isOk)

        then(authService).should().logout("the-token", "the-refresh")
    }

    @Test
    fun `POST logout without a refresh body still revokes the access token`() {
        mockMvc.perform(
            post("/auth/logout").header(HttpHeaders.AUTHORIZATION, "Bearer the-token")
        )
            .andExpect(status().isOk)

        then(authService).should().logout("the-token", null)
    }

    @Test
    fun `POST logout without authorization header returns 401`() {
        mockMvc.perform(post("/auth/logout"))
            .andExpect(status().isUnauthorized)

        then(authService).should(never())
            .logout(org.mockito.ArgumentMatchers.anyString(), org.mockito.ArgumentMatchers.anyString())
    }
}
