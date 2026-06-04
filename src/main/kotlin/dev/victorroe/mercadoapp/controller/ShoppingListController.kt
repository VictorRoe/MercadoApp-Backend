package dev.victorroe.mercadoapp.controller

import dev.victorroe.mercadoapp.dto.shoppingitems.AddItemRequestDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.dto.shoppingitems.UpdateShoppingItemDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.UpdateShoppingListDTO
import dev.victorroe.mercadoapp.model.Status
import dev.victorroe.mercadoapp.service.ShoppingListService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.responses.ApiResponse
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/shoppingList")
@Tag(name = "Shopping Lists", description = "Gestión de listas de compra")
class ShoppingListController(private val service: ShoppingListService) {

    @GetMapping
    @Operation(summary = "Obtener todas las listas", description = "Retorna todas las listas de compras registradas")
    @ApiResponse(responseCode = "200", description = "Listas obtenidas exitosamente")
    fun getAllLists(
        @RequestParam(required = false) status: Status?
    ): ResponseEntity<List<ShoppingListDTO>> {
        return ResponseEntity.ok(service.findAllLists(status))
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una lista por ID")
    @ApiResponse(responseCode = "200", description = "Lista encontrada")
    @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    fun getShoppingList(@PathVariable id: Long): ResponseEntity<ShoppingListDTO> {
        return ResponseEntity.ok(service.findListById(id))
    }

    @PostMapping
    @Operation(summary = "Crear una lista de compras")
    @ApiResponse(responseCode = "201", description = "Lista creada exitosamente")
    fun create(@RequestBody dto: CreateShoppingListDTO): ResponseEntity<ShoppingListDTO> {
        val createShoppingList = service.create(dto)
        return ResponseEntity.status(HttpStatus.CREATED).body(createShoppingList)
    }

    @GetMapping("/history")
    @Operation(summary = "Ver historial de listas", description = "Retorna todas las listas con estado COMPLETED o EXPIRED")
    @ApiResponse(responseCode = "200", description = "Historial obtenido exitosamente")
    fun getHistory(): ResponseEntity<List<ShoppingListDTO>> {
        return ResponseEntity.ok(service.findHistory())
    }

    @PostMapping("/{id}/complete")
    @Operation(summary = "Marcar una lista como completada")
    @ApiResponse(responseCode = "200", description = "Lista marcada como completada")
    @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    fun completeList(@PathVariable id: Long): ResponseEntity<ShoppingListDTO> {
        return ResponseEntity.ok(service.completeList(id))
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Eliminar una lista")
    @ApiResponse(responseCode = "204", description = "Lista eliminada exitosamente")
    @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    fun deleteList(@PathVariable id: Long): ResponseEntity<Void> {
        service.deleteList(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}")
    @Operation(summary = "Actualizar una lista", description = "Permite actualizar el nombre o el supermercado de la lista")
    @ApiResponse(responseCode = "200", description = "Lista actualizada exitosamente")
    @ApiResponse(responseCode = "404", description = "Lista no encontrada")
    fun updateList(
        @PathVariable id: Long,
        @RequestBody dto: UpdateShoppingListDTO,
    ): ResponseEntity<ShoppingListDTO> {
        return ResponseEntity.ok(service.updateList(id, dto))
    }

    @PostMapping("/{id}/items")
    @Operation(summary = "Agregar un producto a la lista")
    @ApiResponse(responseCode = "201", description = "Item agregado exitosamente")
    @ApiResponse(responseCode = "404", description = "Lista o producto no encontrado")
    fun addItem(
        @PathVariable("id") id: Long,
        @RequestBody dto: AddItemRequestDTO
    ): ResponseEntity<Void> {
        service.addItemToList(id, dto)
        return ResponseEntity.status(HttpStatus.CREATED).build()
    }

    @DeleteMapping("/{id}/items/{itemId}")
    @Operation(summary = "Eliminar un producto de la lista")
    @ApiResponse(responseCode = "204", description = "Item eliminado exitosamente")
    @ApiResponse(responseCode = "404", description = "Lista o item no encontrado")
    fun deleteItem(
        @PathVariable id: Long,
        @PathVariable itemId: Long,
    ): ResponseEntity<Void> {
        service.deleteItem(id, itemId)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/items/{itemId}")
    @Operation(summary = "Marcar o desmarcar un item como comprado")
    @ApiResponse(responseCode = "200", description = "Estado del item actualizado")
    @ApiResponse(responseCode = "404", description = "Lista o item no encontrado")
    fun toggleItem(
        @PathVariable id: Long,
        @PathVariable itemId: Long,
        @RequestBody dto: UpdateShoppingItemDTO,
    ): ResponseEntity<ShoppingItemsDTO> {
        return ResponseEntity.ok(service.toggleItem(id, itemId, dto))
    }
}