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

class SavePatrolLogUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    lateinit var useCase: SavePatrolLogUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = SavePatrolLogUseCase(gateway)
    }

    @Test
    fun `should be able to save a patrol log for the route`() = runBlocking {
        coEvery { gateway.saveRoutePlan(any()) } returns 1234

        useCase.execute(
            SavePatrolLogUseCase.Params(
                1234,
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:05 AM",
                true,
                "This is a description"
            )
        )

        val captor = slot<RoutePlanRequests.SaveLog>()
        coVerify { gateway.saveRoutePlan(capture(captor)) }
        assertEquals("Feb 12, 2022 - 11:00 AM", captor.captured.startTime)
        assertEquals("Feb 12, 2022 - 11:05 AM", captor.captured.endTime)
        assertEquals(1234, captor.captured.patrolLocationId)
        assertEquals(true, captor.captured.isCleared)
        assertEquals("This is a description", captor.captured.remarks)
    }

}