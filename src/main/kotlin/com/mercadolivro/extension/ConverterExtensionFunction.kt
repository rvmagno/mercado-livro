package com.mercadolivro.extension

import com.mercadolivro.controller.request.PostBookRequest
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutBookRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.controller.response.BookResponse
import com.mercadolivro.controller.response.CustomerResponse
import com.mercadolivro.enums.BookStatus
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel

fun PostCustomerRequest.toCustomerModel() : CustomerModel{
    return CustomerModel(
        name = this.name,
        email = this.email,
        status = CustomerStatus.ATIVO,
        password = this.password)
}
fun PutCustomerRequest.toCustomerModel(previusCustomer: CustomerModel) : CustomerModel{
    return CustomerModel(
        id = previusCustomer.id,
        name = this.name,
        email = this.email,
        status = previusCustomer.status,
        password = previusCustomer.password
    )
}

fun PostBookRequest.toBookModel(customer: CustomerModel):BookModel{
    return BookModel(
        name = this.name,
        price = this.price,
        status = BookStatus.ATIVO,
        customer = customer
    )
}
fun PutBookRequest.toBookModel(previusBook: BookModel):BookModel{
    return BookModel(
        id = previusBook.id,
        name = this.name ?: previusBook.name,
        price = this.price ?: previusBook.price,
        status = previusBook.status,
        customer = previusBook.customer

    )
}

fun CustomerModel.toCustomerResponse(): CustomerResponse {
    return  CustomerResponse(
        id = this.id,
        name = this.name,
        email = this.email,
        status = this.status
    )
}

fun BookModel.toBookResponse() : BookResponse{
    return BookResponse(
        id = this.id,
        name = this.name,
        price = this.price,
        status = this.status,
        customer = this.customer

    )
}