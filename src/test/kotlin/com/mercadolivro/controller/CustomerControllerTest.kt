package com.mercadolivro.controller

import com.fasterxml.jackson.databind.ObjectMapper
import com.mercadolivro.controller.request.PostCustomerRequest
import com.mercadolivro.controller.request.PutCustomerRequest
import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.repository.CustomerRepository
import com.mercadolivro.security.UserCustomerDetail
import org.junit.jupiter.api.AfterEach
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.http.MediaType
import org.springframework.security.test.context.support.WithMockUser
import org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.user
import org.springframework.test.context.ActiveProfiles
import org.springframework.test.context.ContextConfiguration
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.*

@SpringBootTest
@AutoConfigureMockMvc
@ContextConfiguration
@ActiveProfiles("test")
@WithMockUser
class CustomerControllerTest{

    @Autowired
    private lateinit var mockMvc: MockMvc

    @Autowired
    private lateinit var repository: CustomerRepository

    @Autowired
    private lateinit var objectMapper : ObjectMapper


    @BeforeEach
    fun setup() = repository.deleteAll()

    @AfterEach
    fun tearDown() = repository.deleteAll()


    @Test
    fun `should return all customers`(){

        val customer1 = repository.save(buildCustomer())
        val customer2 = repository.save(buildCustomer())

        mockMvc.perform(get("/customers"))
            .andExpect(status().isOk)
            .andExpect(jsonPath("$.length()").value(2))
            // customer 1
            .andExpect(jsonPath("$[0]id").value(customer1.id))
            .andExpect(jsonPath("$[0]name").value(customer1.name))
            .andExpect(jsonPath("$[0]email").value(customer1.email))
            .andExpect(jsonPath("$[0]status").value(customer1.status.name))
            // customer 2
            .andExpect(jsonPath("$[1]id").value(customer2.id))
            .andExpect(jsonPath("$[1]name").value(customer2.name))
            .andExpect(jsonPath("$[1]email").value(customer2.email))
            .andExpect(jsonPath("$[1]status").value(customer2.status.name))


    }

    @Test
    fun `should return all customers by name`(){

        val customer1 = repository.save(buildCustomer(name = "jose"))
        val customer2 = repository.save(buildCustomer(name = "maria"))

        mockMvc.perform(get("/customers?name=jo"))
            .andExpect(status().isOk)
            // customer 1
            .andExpect(jsonPath("$.length()").value(1))
            .andExpect(jsonPath("$[0]id").value(customer1.id))
            .andExpect(jsonPath("$[0]name").value(customer1.name))
            .andExpect(jsonPath("$[0]email").value(customer1.email))
            .andExpect(jsonPath("$[0]status").value(customer1.status.name))

    }

    @Test
    fun `should create customers`(){
        val request = PostCustomerRequest("fake name", "fake@email.com", "123")

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))

            .andExpect(status().isCreated)

        val customers = repository.findAll().toList()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)
    }

    @Test
    fun `should return error when create customer`(){
        val request = PostCustomerRequest("", "fake@email.com", "123")

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsBytes(request)))

            .andExpect(status().isUnprocessableEntity)


            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
            .andExpect(jsonPath("$.internalCode").value("ML-001"))
    }


    @Test
    fun `should get a customer by id`(){

        val customer = repository.save(buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}")
                    .with(user(UserCustomerDetail(customer)) ) )
            .andExpect(status().isOk)
            // customer 1

            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.name))

    }

    @Test
    @WithMockUser(roles=["ADMIN"])
    fun `should get a customer by id when user is admin`(){

        val customer = repository.save(buildCustomer())

        mockMvc.perform(get("/customers/${customer.id}"))
            .andExpect(status().isOk)
            // customer 1

            .andExpect(jsonPath("$.id").value(customer.id))
            .andExpect(jsonPath("$.name").value(customer.name))
            .andExpect(jsonPath("$.email").value(customer.email))
            .andExpect(jsonPath("$.status").value(customer.status.name))

    }


    @Test
    fun `should return forbidden`(){

        val customer = repository.save(buildCustomer())

        mockMvc.perform(get("/customers/0")
                    .with(user(UserCustomerDetail(customer)) ) )
            .andExpect(status().isForbidden)

            .andExpect(jsonPath("$.httpCode").value(403))
            .andExpect(jsonPath("$.message").value("Access Denied"))
            .andExpect(jsonPath("$.internalCode").value("000"))

    }


    @Test
    fun `should update customer`(){

        val customer = repository.save(buildCustomer())
        val request = PutCustomerRequest("Romulo", "romulo@email.com")

        mockMvc.perform(put("/customers/${customer.id}")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request)))

            .andExpect(status().isNoContent)

        val customers = repository.findAll().toList()
        assertEquals(1, customers.size)
        assertEquals(request.name, customers[0].name)
        assertEquals(request.email, customers[0].email)
    }


    @Test
    fun `should return not found when update and customer not exists`(){

        val request = PutCustomerRequest("Romulo", "romulo@email.com")

        mockMvc.perform(put("/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request)))

            .andExpect(status().isNotFound)

            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer Id 1 not exists"))
            .andExpect(jsonPath("$.internalCode").value("ML-201"))
    }




    @Test
    fun `should return error when update customer`(){
        val request = PutCustomerRequest("", "fake@email.com")

        mockMvc.perform(put("/customers/1")
            .contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsBytes(request)))

            .andExpect(status().isUnprocessableEntity)

            .andExpect(jsonPath("$.httpCode").value(422))
            .andExpect(jsonPath("$.message").value("Invalid Request"))
            .andExpect(jsonPath("$.internalCode").value("ML-001"))
    }



    @Test
    fun `should remove customer`(){
        val customer = repository.save(buildCustomer())

        mockMvc.perform(delete("/customers/${customer.id}")
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isNoContent)

        val customerDeleted = repository.findById(customer.id!!)

        assertEquals(CustomerStatus.INATIVO, customerDeleted.get().status)
    }

    @Test
    fun `should remove a not exists customer`(){
        mockMvc.perform(delete("/customers/5")
            .contentType(MediaType.APPLICATION_JSON))

            .andExpect(status().isNotFound)

            .andExpect(jsonPath("$.httpCode").value(404))
            .andExpect(jsonPath("$.message").value("Customer Id 5 not exists"))
            .andExpect(jsonPath("$.internalCode").value("ML-201"))
    }


}
