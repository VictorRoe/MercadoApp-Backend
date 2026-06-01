package dev.victorroe.mercadoapp

import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.status

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("dev")
class OpenApiIntegrationTest {

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Test
    fun `openapi json endpoint returns 200`() {
        mockMvc.perform(get("/v3/api-docs"))
            .andExpect(status().isOk())
    }

    @Test
    fun `swagger ui is accessible`() {
        mockMvc.perform(get("/swagger-ui/index.html"))
            .andExpect(status().isOk())
    }
}
