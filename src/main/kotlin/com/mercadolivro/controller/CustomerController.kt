package com.mercadolivro.controller

import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.extension.toCustomerModel
import com.mercadolivro.extension.toCustomerResponse
import com.mercadolivro.service.CustomerService
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.*
import javax.validation.Valid

@RestController
@RequestMapping("customer")
class CustomerController(
    val service: CustomerService
) {


    @GetMapping ("/{id}")
    fun getCustomer(@PathVariable id: Int) : CustomerResponse {
        return service.findById(id).toCustomerResponse()
    }

    @GetMapping
    fun getAll(@RequestParam name: String?) : List<CustomerResponse> {
        return service.getAll(name).map { it.toCustomerResponse() }
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createCustomer(@RequestBody @Valid customer: PostCustomerRequest){
        service.createCustomer(customer.toCustomerModel())
    }

    @PutMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun updateCustomer(@PathVariable id: Int, @RequestBody @Valid putCustomer: PutCustomerRequest){
        val customerSaved = service.findById(id)
        service.updateCustomer(putCustomer.toCustomerModel(customerSaved))
    }


    @DeleteMapping("{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteCustomer(@PathVariable id: Int){
        service.deleteCustomer(id)
    }

}