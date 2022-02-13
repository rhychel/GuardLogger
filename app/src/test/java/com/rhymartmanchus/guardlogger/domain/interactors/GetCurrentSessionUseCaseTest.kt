package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.Session
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetCurrentSessionUseCaseTest {

    @MockK
    lateinit var gateway: ShiftsGateway

    lateinit var useCase: GetCurrentSessionUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetCurrentSessionUseCase(gateway)
    }

    @Test
    fun `should be able to get current session`() = runBlocking {
        val session = mockk<Session>()
        coEvery { gateway.getCurrentSession()
            } returns session

        val result = useCase.execute(Unit)

        coVerify { gateway.getCurrentSession() }
        assertEquals(session, result.session)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when there is no session`() = runBlocking {
        coEvery { gateway.getCurrentSession()
            } throws NoDataException()

        useCase.execute(Unit)

        Unit
    }
}