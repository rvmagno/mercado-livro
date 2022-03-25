package com.mercadolivro.repository

import com.mercadolivro.model.PurchaseModel
import org.springframework.data.repository.CrudRepository

interface PurchaseRepositoty : CrudRepository<PurchaseModel, Int > {

}
