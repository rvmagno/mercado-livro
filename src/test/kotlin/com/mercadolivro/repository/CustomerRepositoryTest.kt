package com.mercadolivro.repository

import com.mercadolivro.helper.buildCustomer
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ActiveProfiles

@SpringBootTest
@ActiveProfiles("test")
@ExtendWith(MockKExtension::class)
class CustomerRepositoryTest {

    @Autowired
    private lateinit var repository: CustomerRepository

    @BeforeEach
    fun setup() = repository.deleteAll()

    @Test
    fun testFindById(){
        val marcos = repository.save(buildCustomer(name = "Marcos"))
        val matheus = repository.save(buildCustomer(name = "Matheus"))
        val alex = repository.save(buildCustomer(name = "Alex"))

        val customers = repository.findByNameContaining("Ma")

        assertEquals(listOf(marcos, matheus), customers)

    }

    @Nested
    inner class `exists by email`{

        @Test
        fun `should return true when email exists`(){
            val email = "email@teste.com"
            repository.save(buildCustomer(email = email))

            val exists = repository.existsByEmail(email)

            assertTrue(exists)
        }

        @Test
        fun `should return false when email not exists`(){
            val email = "emaildiff@teste.com"

            val exists = repository.existsByEmail(email)

            assertFalse(exists)
        }
    }


    @Nested
    inner class `find by email`{

        @Test
        fun `should return true when email found`(){
            val email = "email@teste.com"
            val customer = repository.save(buildCustomer(email = email))

            val result = repository.findByEmail(email)

            assertNotNull(result)
            assertEquals(customer, result)
        }

        @Test
        fun `should return null when email not found`(){
            val email = "emaildiff@teste.com"

            val result = repository.findByEmail(email)

            assertNull(result)
        }
    }
}