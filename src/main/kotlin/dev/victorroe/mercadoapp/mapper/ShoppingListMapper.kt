package dev.victorroe.mercadoapp.mapper

import dev.victorroe.mercadoapp.dto.shoppingitems.ShoppingItemsDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.CreateShoppingListDTO
import dev.victorroe.mercadoapp.dto.shoppinglist.ShoppingListDTO
import dev.victorroe.mercadoapp.entity.ShoppingItemEntity
import dev.victorroe.mercadoapp.entity.ShoppingListEntity
import dev.victorroe.mercadoapp.model.ShoppingItems
import dev.victorroe.mercadoapp.model.ShoppingList
import org.mapstruct.Mapper
import org.mapstruct.Mapping

@Mapper(componentModel = "spring")
interface ShoppingListMapper {

    @Mapping(source =  "supermarket.name", target = "supermarketName")
    @Mapping(source = "date", target = "date", dateFormat = "dd/MM/yyyy HH:mm")
    fun toDTO(entity: ShoppingList): ShoppingListDTO

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "date", ignore = true)
    @Mapping(target =  "status", ignore = true)
    @Mapping(target = "supermarket", ignore = true)
    @Mapping(target =  "items", ignore = true)
    fun toModel(dto: CreateShoppingListDTO): ShoppingList

    @Mapping(source = "product.name", target = "productName")
    @Mapping(source = "product.unit", target = "productUnit")
    fun toShoppingItemDTO(entity: ShoppingItems): ShoppingItemsDTO

    fun toEntity(model: ShoppingList): ShoppingListEntity

    fun toModel(entity: ShoppingListEntity): ShoppingList

    fun toItemEntity(model: ShoppingItems): ShoppingItemEntity

    @Mapping(target = "shoppingList", ignore = true)
    fun toItemModel(entity: ShoppingItemEntity): ShoppingItems
}