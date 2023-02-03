package com.mercadolivro.service

import com.mercadolivro.exception.AuthenticationException
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.UserCustomerDetail
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.stereotype.Service

@Service
class UserDetailCustomService(
    private val customerRepository: CustomerRepository
) : UserDetailsService {

    override fun loadUserByUsername(id: String): UserDetails {
        val customer = customerRepository.findById(id.toInt())
            .orElseThrow {throw AuthenticationException("Usuario n encontrado", "998")}

        return UserCustomerDetail(customer)
    }
}