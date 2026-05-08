package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.dto.shoppingitems.AddItemRequestDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.service.ShoppingListService
import org.apache.coyote.Response
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shoppingList")
class ShoppingListController(private val service: ShoppingListService) {

    @GetMapping("/{id}")
    fun getShoppingList(@PathVariable id:Long): ResponseEntity<ShoppingListDTO> {
        return ResponseEntity.ok(service.findListById(id))
    }
    @PostMapping
    fun create(@RequestBody dto: CreateShoppingListDTO): ResponseEntity<ShoppingListDTO> {
        val createShoppingList = service.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createShoppingList)
    }

    @PostMapping("/{id}/items")
    fun addItem(
        @PathVariable("id") id: Long,
        @RequestBody dto: AddItemRequestDTO
    ): ResponseEntity<Void> {

        service.addItemToList(id, dto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }


}