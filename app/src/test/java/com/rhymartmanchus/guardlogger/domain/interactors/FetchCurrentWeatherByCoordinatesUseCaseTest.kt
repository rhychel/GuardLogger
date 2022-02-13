package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.WeathersGateway
import com.rhymartmanchus.guardlogger.domain.models.Weather
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import retrofit2.HttpException
import retrofit2.Response

class FetchCurrentWeatherByCoordinatesUseCaseTest {

    @MockK
    lateinit var gateway: WeathersGateway

    lateinit var useCase: FetchCurrentWeatherByCoordinatesUseCase

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        useCase = FetchCurrentWeatherByCoordinatesUseCase(gateway)
    }

    @Test
    fun `should fetch the current weather best on coordinates`() = runBlocking {
        val mockWeather = mockk<Weather>()
        coEvery {
            gateway.fetchWeatherByCoordinates(any(), any())
        } returns mockWeather

        val result = useCase.execute(
            FetchCurrentWeatherByCoordinatesUseCase.Params(123.0, 23.0)
        )


        val coordinatesCaptor = mutableListOf<Double>()
        coVerify { gateway.fetchWeatherByCoordinates(capture(coordinatesCaptor), capture(coordinatesCaptor)) }
        assertEquals(mockWeather, result.currentWeather)
        assertEquals(123.0, coordinatesCaptor.first(), 0.0)
        assertEquals(23.0, coordinatesCaptor.last(), 0.0)
    }

    @Test(expected = HttpException::class)
    fun `should throw an exception when request failed`() = runBlocking {
        coEvery {
            gateway.fetchWeatherByCoordinates(any(), any())
        } throws HttpException(mockk<Response<*>>(relaxed = true))

        useCase.execute(
            FetchCurrentWeatherByCoordinatesUseCase.Params(1.0, 2.0)
        )

        Unit
    }
}