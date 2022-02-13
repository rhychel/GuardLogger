package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.AreaLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.models.Session
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ResetRoutePlanUseCaseTest {

    @MockK(relaxed = true)
    lateinit var gateway: SecurityLogsGateway

    @MockK(relaxed = true)
    lateinit var shiftsGateway: ShiftsGateway

    lateinit var useCase: ResetRoutePlanUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = ResetRoutePlanUseCase(gateway, shiftsGateway)
    }

    @Test
    fun `should be able to reset the route plan for current session`() = runBlocking {
        val session = mockk<Session>(relaxed = true)
        val locations = listOf<AreaLocation>(
            mockk(relaxed = true),
            mockk(relaxed = true),
            mockk(relaxed = true)
        )
        val oldRoutePlan = mockk<RoutePlan>(relaxed = true) {
            every { id } returns 1
        }
        val routePlan = mockk<RoutePlan>(relaxed = true) {
            every { id } returns 1
        }

        coEvery { shiftsGateway.getCurrentSession()
            } returns session
        coEvery { gateway.getLocations()
            } returns locations
        coEvery { gateway.saveRoutePlan(any()) } returns 1234
        coEvery { gateway.getPatrolRoutePlan(any())
            } answers { oldRoutePlan
            } andThenAnswer { routePlan }

        val result = useCase.execute(Unit)

        val createCaptor = slot<RoutePlanRequests.Reset>()
        coVerifySequence {
            shiftsGateway.getCurrentSession()
            gateway.getPatrolRoutePlan(session.userId)
            gateway.getLocations()
            gateway.saveRoutePlan(capture(createCaptor))
            gateway.getPatrolRoutePlan(session.userId)
        }
        assertEquals(
            locations.map {
                it.id
            },
            createCaptor.captured.patrolLocations.map {
                it.areaLocationId
            }
        )
        assertEquals(
            locations.map {
                it.name
            },
            createCaptor.captured.patrolLocations.map {
                it.name
            }
        )
        assertEquals(
            oldRoutePlan.id,
            createCaptor.captured.routePlanId
        )
        assertEquals(routePlan, result.resetRoutePlan)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when current session is not available`() = runBlocking {
        coEvery { shiftsGateway.getCurrentSession()
        } throws NoDataException()

        useCase.execute(Unit)

        Unit
    }
}