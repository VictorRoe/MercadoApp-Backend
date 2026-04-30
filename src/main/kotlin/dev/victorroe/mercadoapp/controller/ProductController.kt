package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.model.Product
import dev.victorroe.mercadoapp.service.ProductService
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
class ProductController(private val service: ProductService) {

    @GetMapping
    fun findAll() = ResponseEntity.ok(service.findAll())

    @GetMapping("/{id}")
    fun findProductById(@PathVariable id:Long) = ResponseEntity.ok(service.findProductById(id))

    @PostMapping
    fun save(@RequestBody product: Product): ResponseEntity<Product> {
        val savedProduct = service.save(product)
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct)
    }
}