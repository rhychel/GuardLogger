package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.Session
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class SaveCheckInLogUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    @MockK
    lateinit var shiftsGateway: ShiftsGateway

    lateinit var useCase: SaveCheckInLogUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = SaveCheckInLogUseCase(gateway, shiftsGateway)
    }

    @Test
    fun `should be able to save check in log`() = runBlocking {
        val session = mockk<Session>(relaxed = true)
        coEvery { shiftsGateway.getCurrentSession()
        } returns session
        coEvery { gateway.saveCheckInLog(any())
        } returns Unit

        useCase.execute(
            SaveCheckInLogUseCase.Params(
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:05 AM",
                "This is a description"
            )
        )

        val captor = slot<CheckInRequest>()
        coVerifySequence {
            shiftsGateway.getCurrentSession()
            gateway.saveCheckInLog(capture(captor))
        }
        assertEquals(
            CheckInRequest(session.userId,
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:05 AM",
                "This is a description"),
            captor.captured
        )
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when current session is not available`() = runBlocking {
        coEvery { shiftsGateway.getCurrentSession()
        } throws NoDataException()


        useCase.execute(
            SaveCheckInLogUseCase.Params(
                "Feb 12, 2022 - 11:00 AM",
                "Feb 12, 2022 - 11:05 AM",
                "This is a description"
            )
        )
    }

    @Test
    fun `should throw an exception when start time is empty`() = runBlocking {
        try {
            useCase.execute(
                SaveCheckInLogUseCase.Params(
                    "",
                    "Feb 12, 2022 - 11:05 AM",
                    "This is a description"
                )
            )
            fail("Use case succeeded")
        } catch (e: Exception) {
            if(e is IllegalArgumentException) {
                if(e.message != "Start Time cannot be empty") {
                    fail("Exception message is incorrect")
                } else {
                    assert(true)
                }
            } else {
                fail("Exception is not IllegalArgumentException")
            }
        }
    }

    @Test
    fun `should throw an exception when end time is empty`() = runBlocking {
        try {
            useCase.execute(
                SaveCheckInLogUseCase.Params(
                    "Feb 12, 2022 - 11:00 AM",
                    "",
                    "This is a description"
                )
            )
            fail("Use case succeeded")
        } catch (e: Exception) {
            if(e is IllegalArgumentException) {
                if(e.message != "End Time cannot be empty") {
                    fail("Exception message is incorrect")
                } else {
                    assert(true)
                }
            } else {
                fail("Exception is not IllegalArgumentException")
            }
        }
    }

    @Test
    fun `should throw an exception when description is empty`() = runBlocking {
        try {
            useCase.execute(
                SaveCheckInLogUseCase.Params(
                    "Feb 12, 2022 - 11:00 AM",
                    "Feb 12, 2022 - 11:05 AM",
                    ""
                )
            )
            fail("Use case succeeded")
        } catch (e: Exception) {
            if(e is IllegalArgumentException) {
                if(e.message != "Description cannot be empty") {
                    fail("Exception message is incorrect")
                } else {
                    assert(true)
                }
            } else {
                fail("Exception is not IllegalArgumentException")
            }
        }
    }
}