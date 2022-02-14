package com.rhymartmanchus.guardlogger.screens.logbook

import com.rhymartmanchus.guardlogger.TestAppDispatcher
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.interactors.GetCheckInLogsUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.SaveCheckInLogUseCase
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class LogBookPresenterTest {

    @MockK
    lateinit var saveCheckInLogUseCase: SaveCheckInLogUseCase

    @MockK
    lateinit var getCheckInLogsUseCase: GetCheckInLogsUseCase

    @MockK(relaxUnitFun = true)
    lateinit var view: LogBookContract.View

    lateinit var presenter: LogBookPresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)


        presenter = LogBookPresenter(
            TestAppDispatcher(),
            getCheckInLogsUseCase,
            saveCheckInLogUseCase
        )
        presenter.takeView(view)
    }

    @Test
    fun `should be able to render existing logs`() = runBlocking {
        val checkInLogs = listOf<CheckInLog>(
            mockk(),
            mockk(),
            mockk()
        )
        coEvery { getCheckInLogsUseCase.execute(Unit)
            } returns GetCheckInLogsUseCase.Response(checkInLogs)

        presenter.onViewCreated()

        val captor = slot<List<CheckInLog>>()
        coVerifySequence {
            getCheckInLogsUseCase.execute(Unit)
            view.renderCheckInLogs(capture(captor))
            view.hideNoLogsAvailable()
        }
        assertEquals(checkInLogs, captor.captured)
    }

    @Test
    fun `show no logs available label when there is no checkin logs`() = runBlocking {
        coEvery { getCheckInLogsUseCase.execute(Unit)
            } throws NoDataException()

        presenter.onViewCreated()

        coVerifySequence {
            getCheckInLogsUseCase.execute(Unit)
        }
        confirmVerified(view)
    }

    @Test
    fun `should be able to add a log successfully`() = runBlocking {
        coEvery { saveCheckInLogUseCase.execute(any())
            } returns SaveCheckInLogUseCase.Response(CheckInLog(
            "Rhy",
            "Feb 14, 2022 - 11:04 AM",
            "Feb 14, 2022 - 11:09 AM",
            "Logs"
        ))
        val checkInLogs = listOf<CheckInLog>(
            mockk(),
            mockk(),
            mockk()
        )
        coEvery { getCheckInLogsUseCase.execute(Unit)
        } returns GetCheckInLogsUseCase.Response(checkInLogs)
        presenter.onViewCreated()


        presenter.onAddLogClicked()


        val captor = slot<suspend (String, String, String) -> Unit>()
        val captorParams = slot<SaveCheckInLogUseCase.Params>()
        val captorCheckInLogs = slot<CheckInLog>()
        verify {
            view.showAddLogDialog(capture(captor))
        }
        captor.captured.invoke(
            "Feb 14, 2022 - 11:04 AM",
            "Feb 14, 2022 - 11:09 AM",
            "Logs")
        coVerify {
            saveCheckInLogUseCase.execute(capture(captorParams))
            view.appendCheckInLog(capture(captorCheckInLogs))
            view.hideNoLogsAvailable()
        }
        assertEquals("Feb 14, 2022 - 11:04 AM", captorParams.captured.startTime)
        assertEquals("Feb 14, 2022 - 11:09 AM", captorParams.captured.endTime)
        assertEquals("Logs", captorParams.captured.description)

        assertEquals("Feb 14, 2022 - 11:04 AM", captorCheckInLogs.captured.startTime)
        assertEquals("Feb 14, 2022 - 11:09 AM", captorCheckInLogs.captured.endTime)
        assertEquals("Logs", captorCheckInLogs.captured.log)
        assertEquals("Rhy", captorCheckInLogs.captured.employeeName)
    }

    @Test
    fun `should be able to show checkin log details`() = runBlocking {
        val checkInLogs = listOf<CheckInLog>(
            mockk {
                    every { employeeName } returns "Rhy"
            },
            mockk(),
            mockk()
        )
        coEvery { getCheckInLogsUseCase.execute(Unit)
        } returns GetCheckInLogsUseCase.Response(checkInLogs)
        presenter.onViewCreated()

        presenter.onLogClicked(0)

        val captor = slot<CheckInLog>()
        verify { view.showLogDetailsDialog(capture(captor)) }
        assertEquals("Rhy", captor.captured.employeeName)
    }
}