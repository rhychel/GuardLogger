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

class SavePatrolLocationSortingUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    lateinit var useCase: SavePatrolLocationSortingUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = SavePatrolLocationSortingUseCase(gateway)
    }

    @Test
    fun `should be able to save the new sorting of locations`() = runBlocking {
        coEvery { gateway.saveRoutePlan(any()) } returns 1234

        useCase.execute(
            SavePatrolLocationSortingUseCase.Params(
                1234,
                "1.1.",
                543,
                "1.1.1."
            )
        )

        val captor = slot<RoutePlanRequests.ArrangeRouteLocation>()
        coVerify { gateway.saveRoutePlan(capture(captor)) }
        assertEquals(
            1234,
            captor.captured.fromPatrolLocationId
        )
        assertEquals(
            "1.1.",
            captor.captured.fromSorting
        )
        assertEquals(
            543,
            captor.captured.toPatrolLocationId
        )
        assertEquals(
            "1.1.1.",
            captor.captured.toSorting
        )
    }
}