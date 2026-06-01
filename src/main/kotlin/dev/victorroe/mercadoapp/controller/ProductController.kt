package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.model.Product
import dev.victorroe.mercadoapp.service.ProductService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/products")
@Tag(name = "Products", description = "Gestión de productos")
class ProductController(private val service: ProductService) {

    @GetMapping
    @Operation(summary = "Listar todos los productos")
    @ApiResponse(responseCode = "200", description = "Lista de productos obtenida exitosamente")
    fun findAll() = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    @Operation(summary = "Obtener un producto por ID")
    @ApiResponse(responseCode = "200", description = "Producto encontrado")
    @ApiResponse(responseCode = "404", description = "Producto no encontrado")
    fun findProductById(@PathVariable id: Long) = ResponseEntity.ok(service.findById(id))

    @PostMapping
    @Operation(summary = "Crear un producto")
    @ApiResponse(responseCode = "201", description = "Producto creado exitosamente")
    fun save(@RequestBody product: Product): ResponseEntity<Product> {
        val savedProduct = service.create(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }
}