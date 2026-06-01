package dev.victorroe.mercadoapp.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class OpenApiConfig {
    @Bean
    fun customOpenAPI(): OpenAPI = OpenAPI()
        .info(
            Info()
                .title("MercadoApp API")
                .version("1.0.0")
                .description("API para gestión de listas de compras, productos y supermercados.")
        )
}
