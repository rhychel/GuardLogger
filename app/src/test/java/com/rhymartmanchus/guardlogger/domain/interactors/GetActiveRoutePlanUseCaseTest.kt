package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.NoActiveRoutePlanException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.models.Session
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetActiveRoutePlanUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    @MockK
    lateinit var shiftsGateway: ShiftsGateway

    lateinit var useCase: GetActiveRoutePlanUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetActiveRoutePlanUseCase(gateway, shiftsGateway)
    }

    @Test
    fun `should be able to get the active route plan for the user`() = runBlocking {
        val session = mockk<Session>(relaxed = true)
        val routePlan = mockk<RoutePlan>()
        coEvery { shiftsGateway.getCurrentSession()
        } returns session
        coEvery { gateway.getPatrolRoutePlan(any())
        } returns routePlan

        val result = useCase.execute(Unit)

        coVerifySequence {
            shiftsGateway.getCurrentSession()
            gateway.getPatrolRoutePlan(session.userId)
        }
        assertEquals(routePlan, result.routePlan)
    }

    @Test(expected = NoActiveRoutePlanException::class)
    fun `should throw an exception when there is no active route plan for the user`() = runBlocking {
        coEvery { shiftsGateway.getCurrentSession()
            } returns  mockk(relaxed = true)
        coEvery { gateway.getPatrolRoutePlan(any())
            } throws NoDataException()


        useCase.execute(Unit)

        Unit
    }


    @Test(expected = NoDataException::class)
    fun `should throw an exception when current session is not available`() = runBlocking {
        coEvery { shiftsGateway.getCurrentSession()
        } throws NoDataException()

        useCase.execute(Unit)

        Unit
    }
}