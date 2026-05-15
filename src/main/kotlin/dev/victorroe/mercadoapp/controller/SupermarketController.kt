package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.dto.supermarket.CreateSupermarketDTO
import dev.victorroe.mercadoapp.dto.supermarket.SupermarketDTO
import dev.victorroe.mercadoapp.service.SupermarketService
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/supermarket")
class SupermarketController(private val service: SupermarketService) {

    @PostMapping
    fun save(@RequestBody dto: CreateSupermarketDTO): ResponseEntity<SupermarketDTO> {
        return ResponseEntity.status(HttpStatus.CREATED).body(service.create(dto))
    }
}