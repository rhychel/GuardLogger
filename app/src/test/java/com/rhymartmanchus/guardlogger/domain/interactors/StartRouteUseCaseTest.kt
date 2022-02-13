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

class StartRouteUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    lateinit var useCase: StartRouteUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = StartRouteUseCase(gateway)
    }

    @Test
    fun `should start the route successfully`() = runBlocking {
        coEvery { gateway.saveRoutePlan(any()) } returns 1234

        useCase.execute(
            StartRouteUseCase.Params(1234, "Feb 12, 2022 - 11:01 AM")
        )

        val captor = slot<RoutePlanRequests.Start>()
        coVerify { gateway.saveRoutePlan(capture(captor)) }
        assertEquals(1234, captor.captured.routePlanId)
        assertEquals("Feb 12, 2022 - 11:01 AM", captor.captured.time)
    }
}