package com.mercadolivro.service

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helper.buildCustomer
import com.mercadolivro.helper.buildPurchase
import com.mercadolivro.model.BookModel
import com.mercadolivro.model.CustomerModel
import com.mercadolivro.model.PurchaseModel
import com.mercadolivro.repository.PurchaseRepositoty
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.springframework.context.ApplicationEventPublisher
import java.math.BigDecimal
import java.util.UUID

@ExtendWith(MockKExtension::class)
class PurchaseServiceTest {

    @MockK
    private lateinit var  repository: PurchaseRepositoty

    @MockK
    private lateinit var  applicationEventPublisher: ApplicationEventPublisher

    @InjectMockKs
    private lateinit var purchaseService: PurchaseService

    val purchaseEventSlot = slot<PurchaseEvent>()

    @Test
    fun testCreate() {
        val purchase = buildPurchase()

        every { repository.save(purchase) } returns purchase
        every { applicationEventPublisher.publishEvent(any()) } just runs

        purchaseService.create(purchase)

        verify(exactly = 1) { repository.save(purchase) }
        verify(exactly = 1) { applicationEventPublisher.publishEvent(capture(purchaseEventSlot)) }

        assertEquals(purchase, purchaseEventSlot.captured.purchaseModel)
    }

    @Test
    fun testUpdate() {
        val purchase = buildPurchase()

        every { repository.save(purchase) } returns  purchase

        purchaseService.update(purchase)

        verify(exactly = 1) { repository.save(purchase) }
    }

}