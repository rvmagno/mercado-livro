package com.mercadolivro.security

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper
import com.mercadolivro.controller.request.LoginRequest
import com.mercadolivro.exception.AuthenticationException
import com.mercadolivro.repository.CustomerRepository
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter
import javax.servlet.FilterChain
import javax.servlet.http.HttpServletRequest
import javax.servlet.http.HttpServletResponse

class AuthenticationFilter (
    authenticationManager: AuthenticationManager,
    private val customerRepository: CustomerRepository,
    private val jwtUtil: JwtUtil
): UsernamePasswordAuthenticationFilter(authenticationManager){

    override fun attemptAuthentication(request: HttpServletRequest, response: HttpServletResponse): Authentication {

        try {
            val login = jacksonObjectMapper().readValue(request.inputStream, LoginRequest::class.java)
            val id = customerRepository.findByEmail(login.email)?.id
            val authToken = UsernamePasswordAuthenticationToken(id, login.password)
            return authenticationManager.authenticate(authToken)
        } catch (e: Exception) {
            println("falha ao autenticar")
            throw AuthenticationException("Falha ao authenticar", "999")
        }

    }

    override fun successfulAuthentication(request: HttpServletRequest, response: HttpServletResponse, chain: FilterChain, authResult: Authentication) {
        val id = (authResult.principal as UserCustomerDetail).id
        val token = jwtUtil.generateToken(id)
        response.addHeader("Authorization", "Bearer $token")
    }
}