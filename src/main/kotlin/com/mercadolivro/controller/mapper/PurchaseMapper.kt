package com.mercadolivro.controller.mapper

import com.mercadolivro.controller.request.PostPurchaseResquest
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.service.BookService
import com.mercadolivro.service.CustomerService
import org.springframework.stereotype.Component

@Component
class PurchaseMapper (
    val bookSerive: BookService,
    val customerService: CustomerService){

    fun toModel(request: PostPurchaseResquest): PurchaseModel{
        val customer = customerService.findById(request.customerId)
        val books = bookSerive.findAllByIds(request.bookId)

        return PurchaseModel(
            customer = customer,
            books = books,
            price = books.sumOf { it.price }
        )
    }

}