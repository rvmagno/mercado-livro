package com.mercadolivro.controller.request

import javax.validation.constraints.Email
import javax.validation.constraints.NotEmpty

data class PostCustomerRequest (

    @field: NotEmpty(message = "nome deve ser informado")
    var name: String,

    @field: Email(message = "email deve ser valido")
    var email: String
)