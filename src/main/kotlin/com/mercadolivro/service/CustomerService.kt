package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Roles
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import org.springframework.web.bind.annotation.PathVariable

@Service
class CustomerService(
    private val repository: CustomerRepository,
    private val bookService: BookService,
    private val bCrypt: BCryptPasswordEncoder
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
        val customerCopy = customer.copy(
            role = setOf(Roles.CUSTOMER),
            password = bCrypt.encode(customer.password)
        )
        repository.save(customerCopy)
    }

    fun updateCustomer(customerModel: CustomerModel){
        if(!repository.existsById(customerModel.id!!)){
            throw NotFoundException(Errors.ML201.message.format(customerModel.id), Errors.ML201.code)
        }

        repository.save(customerModel)
    }

    fun deleteCustomer(id: Int){
        val customer = findById(id)
        bookService.deleteByCustomer(customer)
        customer.status = CustomerStatus.INATIVO
        repository.save(customer)
    }

    fun emailAvailable(email: String): Boolean {
        return !repository.existsByEmail(email)
    }
}