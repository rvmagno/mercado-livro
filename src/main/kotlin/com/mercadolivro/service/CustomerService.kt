package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class CustomerService(
    val repository: CustomerRepository,
    val bookService: BookService
) {


    fun findById(@PathVariable id: Int) : CustomerModel {
        return repository.findById(id).orElseThrow{NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)}
    }


    fun getAll(name: String?) : List<CustomerModel> {
        name?.let {
            return repository.findByNameContaining(name)
        }
        return repository.findAll() as List<CustomerModel>
    }

    fun createCustomer(customer: CustomerModel){
        repository.save(customer)
    }

    fun updateCustomer(customerModel: CustomerModel){
        if(!repository.existsById(customerModel.id!!)){
            throw Exception("n existe pelo id")
        }

        repository.save(customerModel)
    }

    fun deleteCustomer(id: Int){
        val customer = findById(id)
        bookService.deleteByCustomer(customer)
        customer.status = CustomerStatus.INATIVO
        repository.save(customer)
    }
}