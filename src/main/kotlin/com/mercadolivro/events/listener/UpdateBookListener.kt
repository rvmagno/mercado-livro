package com.mercadolivro.events.listener

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.service.BookService
import com.mercadolivro.service.PurchaseService
import org.springframework.context.event.EventListener
import org.springframework.scheduling.annotation.Async
import org.springframework.stereotype.Component
import java.util.*

@Component
class UpdateBookListener(
    private val bookService: BookService
) {

    @Async
    @EventListener
    fun listener(purchaseEvent: PurchaseEvent){
        bookService.purchase(purchaseEvent.purchaseModel.books)
    }

}