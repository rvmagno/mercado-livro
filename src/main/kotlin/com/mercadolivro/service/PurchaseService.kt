package com.mercadolivro.service

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.repository.PurchaseRepositoty
import org.springframework.context.ApplicationEventPublisher
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service

@Service
class PurchaseService(
    private val repository: PurchaseRepositoty,
    private val applicationEventPublisher: ApplicationEventPublisher
) {

    fun create(purchaseModel: PurchaseModel){
        repository.save(purchaseModel)
        applicationEventPublisher.publishEvent(PurchaseEvent(this, purchaseModel))
    }

    fun update(purchaseModel: PurchaseModel) {
        repository.save(purchaseModel)
    }

}
