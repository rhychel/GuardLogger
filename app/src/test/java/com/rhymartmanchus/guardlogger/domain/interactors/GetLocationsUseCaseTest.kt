package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.AreaLocation
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class GetLocationsUseCaseTest {

    @MockK
    lateinit var gateway: SecurityLogsGateway

    lateinit var useCase: GetLocationsUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = GetLocationsUseCase(gateway)
    }

    @Test
    fun `should be able to get the locations`() = runBlocking {
        val mockLocations = listOf<AreaLocation>(
            mockk(),
            mockk(),
            mockk()
        )
        coEvery { gateway.getLocations()
        } returns mockLocations

        val result = useCase.execute(Unit)

        coVerify { gateway.getLocations() }
        assertEquals(mockLocations, result.locations)
    }

    @Test(expected = NoDataException::class)
    fun `should throw an exception when there is not locations saved`() = runBlocking {
        coEvery { gateway.getLocations()
        } throws NoDataException()

        useCase.execute(Unit)

        Unit
    }
}