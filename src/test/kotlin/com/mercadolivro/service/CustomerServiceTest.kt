package com.mercadolivro.service

import com.mercadolivro.enums.CustomerStatus
import com.mercadolivro.enums.Errors
import com.mercadolivro.enums.Roles
import com.mercadolivro.exception.NotFoundException
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.repository.CustomerRepository
import io.mockk.every
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.impl.annotations.SpyK
import io.mockk.junit5.MockKExtension
import io.mockk.just
import io.mockk.runs
import io.mockk.verify
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import java.util.*

@ExtendWith(MockKExtension::class)
class CustomerServiceTest {

    @MockK
    private lateinit var repository: CustomerRepository

    @MockK
    private lateinit var bookService: BookService

    @MockK
    private lateinit var bCrypt: BCryptPasswordEncoder

    @InjectMockKs
    @SpyK
    private lateinit var customerService: CustomerService

    @BeforeEach
    fun setUp() {
        println("Starting CustomerService test")

    }

    @Test
    fun testGetAll() {

        val fakeCustomer = listOf(buildCustomer(), buildCustomer())
        every { repository.findAll() } returns fakeCustomer
        val customers = customerService.getAll(null)

        assertEquals(fakeCustomer, customers)
        verify(exactly = 1) { repository.findAll() }
        verify(exactly = 0) { repository.findByNameContaining(any()) }
    }

    @Test
    fun testGetAllUsingName() {
        val name = UUID.randomUUID().toString()

        val fakeCustomer = listOf(buildCustomer(), buildCustomer())

        every { repository.findByNameContaining(name) } returns fakeCustomer

        val customers = customerService.getAll(name)

        assertEquals(fakeCustomer, customers)
        verify(exactly = 0) { repository.findAll() }
        verify(exactly = 1) { repository.findByNameContaining(name) }
    }

    @Test
    fun testCreate(){
        var initialPassword = Random().nextInt().toString()
        val fakeCustomer = buildCustomer(password = initialPassword)
        val fakePassword = UUID.randomUUID().toString()
        val fakeCustomerEncrypted = fakeCustomer.copy(password = fakePassword)

        every{ repository.save(fakeCustomerEncrypted) } returns fakeCustomer
        every{ bCrypt.encode(initialPassword) } returns fakePassword

        customerService.createCustomer(fakeCustomer)

        verify(exactly = 1) {repository.save(fakeCustomerEncrypted)}
        verify(exactly = 1) {bCrypt.encode(initialPassword) }
    }

    @Test
    fun testFindById(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { repository.findById(id) } returns Optional.of(fakeCustomer)

        val customer = customerService.findById(id)

        verify(exactly = 1) { repository.findById(id) }
    }


    @Test
    fun testReturningException(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { repository.findById(id) } returns Optional.empty()

        val error = assertThrows(NotFoundException::class.java) { customerService.findById(id) }

        assertEquals("Customer Id $id not exists" , error.message)
        assertEquals("ML-201" , error.errorCode)
        verify(exactly = 1) { repository.findById(id) }
    }

    @Test
    fun testUpdateCustomer(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { repository.existsById(id) } returns true
        every { repository.save(fakeCustomer) } returns fakeCustomer

        customerService.updateCustomer(fakeCustomer)

        verify(exactly = 1) { repository.existsById(id) }
        verify(exactly = 1) { repository.save(fakeCustomer) }
    }

    @Test
    fun testUpdateCustomerWithException(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)

        every { repository.existsById(id) } returns false
        every { repository.save(fakeCustomer) } returns fakeCustomer

        val error = assertThrows(NotFoundException::class.java) { customerService.updateCustomer(fakeCustomer) }

        assertEquals("Customer Id $id not exists" , error.message)
        assertEquals("ML-201" , error.errorCode)

        verify(exactly = 1) { repository.existsById(id) }
        verify(exactly = 0) { repository.save(fakeCustomer) }
    }

    @Test
    fun testDeleteCustomer(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerService.findById(id) } returns fakeCustomer
        every { repository.save(expectedCustomer) } returns expectedCustomer
        every { bookService.deleteByCustomer(fakeCustomer) } just runs

        customerService.deleteCustomer(id)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 1) { bookService.deleteByCustomer(fakeCustomer) }
        verify(exactly = 1) { repository.save(expectedCustomer) }
    }

    @Test
    fun testDeleteCustomerThrowingException(){
        val id = Random().nextInt()
        val fakeCustomer = buildCustomer(id = id)
        val expectedCustomer = fakeCustomer.copy(status = CustomerStatus.INATIVO)

        every { customerService.findById(id) } throws NotFoundException(Errors.ML201.message.format(id), Errors.ML201.code)


        val error = assertThrows(NotFoundException::class.java) { customerService.deleteCustomer(id) }

        assertEquals("Customer Id $id not exists" , error.message)
        assertEquals("ML-201" , error.errorCode)

        verify(exactly = 1) { customerService.findById(id) }
        verify(exactly = 0) { bookService.deleteByCustomer(any()) }
        verify(exactly = 0) { repository.save(any()) }
    }


    @Test
    fun testEmailAvailavle(){
        val email = Random().nextInt().toString()

        every { repository.existsByEmail(email) } returns false

        val emailAvailable = customerService.emailAvailable(email)

        assertTrue(emailAvailable)

        verify(exactly = 1) { repository.existsByEmail(email) }
    }

    @Test
    fun testEmailUnavailavle(){
        val email = Random().nextInt().toString()

        every { repository.existsByEmail(email) } returns true

        val emailAvailable = customerService.emailAvailable(email)

        assertFalse(emailAvailable)

        verify(exactly = 1) { repository.existsByEmail(email) }
    }


}


