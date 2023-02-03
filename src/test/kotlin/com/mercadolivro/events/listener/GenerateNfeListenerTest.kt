package com.mercadolivro.events.listener

import com.mercadolivro.events.PurchaseEvent
import com.mercadolivro.helper.buildPurchase
import com.mercadolivro.service.PurchaseService
import io.mockk.*
import io.mockk.impl.annotations.InjectMockKs
import io.mockk.impl.annotations.MockK
import io.mockk.junit5.MockKExtension
import org.junit.jupiter.api.Test

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.extension.ExtendWith
import java.util.UUID

@ExtendWith(MockKExtension::class)
class GenerateNfeListenerTest {

    @MockK
    private lateinit var  purchaseService: PurchaseService

    @InjectMockKs
    private lateinit var generateNfeListener: GenerateNfeListener

    @Test
    fun testListenerEvent() {
        val fakeNfe = UUID.randomUUID()
        val purchase = buildPurchase(nfe = null)

        val exptectedPurchase = purchase.copy(nfe = fakeNfe.toString())
        mockkStatic(UUID::class)

        every {UUID.randomUUID() } returns fakeNfe
        every {purchaseService.update(exptectedPurchase)} just runs

        generateNfeListener.listener(PurchaseEvent(this,  purchase))

        verify(exactly = 1) { purchaseService.update(exptectedPurchase)}

    }
}