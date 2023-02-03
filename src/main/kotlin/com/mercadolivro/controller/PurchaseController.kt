package com.mercadolivro.controller

import com.mercadolivro.controller.mapper.PurchaseMapper
import com.mercadolivro.controller.request.PostPurchaseResquest
import com.mercadolivro.service.PurchaseService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("purchases")
class PurchaseController(
    private val service: PurchaseService,
    private val mapper: PurchaseMapper
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun purchase(@RequestBody request: PostPurchaseResquest){
        service.create(mapper.toModel(request))
    }

}