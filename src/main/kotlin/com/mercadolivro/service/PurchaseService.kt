package com.mercadolivro.service

import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.PurchaseRepositoty
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    val repository: PurchaseRepositoty
) {

    fun create(purchaseModel: PurchaseModel){
        repository.save(purchaseModel)
    }

}
