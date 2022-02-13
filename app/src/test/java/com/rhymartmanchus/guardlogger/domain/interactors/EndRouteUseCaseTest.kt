package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.slot
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class EndRouteUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    lateinit var useCase: EndRouteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = EndRouteUseCase(gateway)
    }

    @Test
    fun `should end the route successfully`() = runBlocking {
        coEvery { gateway.saveRoutePlan(any()) } returns 1234

        useCase.execute(
            EndRouteUseCase.Params(1234, "Feb 12, 2022 - 11:01 AM")
        )

        val captor = slot<RoutePlanRequests.End>()
        coVerify { gateway.saveRoutePlan(capture(captor)) }
        assertEquals(1234, captor.captured.routePlanId)
        assertEquals("Feb 12, 2022 - 11:01 AM", captor.captured.time)
    }
}