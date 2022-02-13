package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetCheckInLogsUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    lateinit var useCase: GetCheckInLogsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetCheckInLogsUseCase(gateway)
    }

    @Test
    fun `should be able to get check in logs`() = runBlocking {
        val checkInLogs = listOf<CheckInLog>(
            mockk(),
            mockk(),
            mockk()
        )
        coEvery { gateway.getCheckInLogs()
        } returns checkInLogs

        val result = useCase.execute(Unit)

        coVerify { gateway.getCheckInLogs() }
        assertEquals(checkInLogs, result.logs)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when there is not check in logs`() = runBlocking {
        coEvery { gateway.getCheckInLogs()
        } throws NoDataException()

        useCase.execute(Unit)

        Unit
    }
}