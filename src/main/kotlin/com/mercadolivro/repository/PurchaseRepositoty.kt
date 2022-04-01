package com.mercadolivro.repository

import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.PurchaseModel
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.repository.CrudRepository

interface PurchaseRepositoty : CrudRepository<PurchaseModel, Int > {


    fun findByCustomer(get: CustomerModel, pageable: Pageable): Page<PurchaseModel>

}
