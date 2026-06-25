package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.dto.supermarket.CreateSupermarketDTO
import dev.victorroe.mercadoapp.dto.supermarket.SupermarketDTO
import dev.victorroe.mercadoapp.service.SupermarketService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.security.SecurityRequirement
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/supermarket")
@Tag(name = "Supermarkets", description = "Gestión de supermercados")
@SecurityRequirement(name = "bearerAuth")
class SupermarketController(private val service: SupermarketService) {

    @GetMapping
    @Operation(summary = "Obtener todos los supermercados")
    @ApiResponse(responseCode = "200", description = "Lista de supermercados")
    fun findAll(): ResponseEntity<List<SupermarketDTO>> {
        return ResponseEntity.ok(service.findAll())
    }

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Crear un supermercado")
    @ApiResponse(responseCode = "201", description = "Supermercado creado exitosamente")
    fun save(@RequestBody dto: CreateSupermarketDTO): ResponseEntity<SupermarketDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto))
    }
}