package com.rhymartmanchus.guardlogger.screens.home

import android.location.Location
import com.rhymartmanchus.guardlogger.TestAppDispatcher
import com.rhymartmanchus.guardlogger.domain.exceptions.NoActiveRoutePlanException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.interactors.CreateRoutePlanUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.FetchCurrentWeatherByCoordinatesUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.GetActiveRoutePlanUseCase
import com.rhymartmanchus.guardlogger.domain.models.Weather
import io.mockk.*
import io.mockk.impl.annotations.MockK
import kotlinx.coroutines.runBlocking
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class HomePresenterTest {

    @MockK
    lateinit var fetchCurrentWeatherByCoordinatesUseCase: FetchCurrentWeatherByCoordinatesUseCase

    @MockK
    lateinit var getActiveRoutePlanUseCase: GetActiveRoutePlanUseCase

    @MockK
    lateinit var createRoutePlanUseCase: CreateRoutePlanUseCase

    @MockK(relaxUnitFun = true)
    lateinit var view: HomeContract.View

    lateinit var presenter: HomePresenter

    @Before
    fun setUp() {
        MockKAnnotations.init(this)

        presenter = HomePresenter(
            TestAppDispatcher(),
            fetchCurrentWeatherByCoordinatesUseCase,
            getActiveRoutePlanUseCase,
            createRoutePlanUseCase
        )
        presenter.takeView(view)
    }

    @Test
    fun onCurrentLocationGathered() = runBlocking {
        val weather = mockk<Weather> {
            every { city } returns "Magarao"
        }
        val fakeLocation = mockk<Location>(relaxUnitFun = true) {
            every { latitude } returns 13.6610091
            every { longitude } returns 123.1801302
        }
        coEvery { fetchCurrentWeatherByCoordinatesUseCase.execute(any())
            } returns FetchCurrentWeatherByCoordinatesUseCase.Response(weather)

        presenter.onCurrentLocationGathered(fakeLocation)

        val captor = slot<FetchCurrentWeatherByCoordinatesUseCase.Params>()
        coVerifySequence {
            fetchCurrentWeatherByCoordinatesUseCase.execute(capture(captor))
            view.renderCurrentWeather(weather)
        }
        assertEquals(fakeLocation.latitude, captor.captured.latitude, 0.0)
        assertEquals(fakeLocation.longitude, captor.captured.longitude, 0.0)
    }

    @Test
    fun onViewCreated() {

        presenter.onViewCreated()

        verify {
            view.requestLocationPermission()
        }
    }

    @Test
    fun onRequestLocationPermitted() {

        presenter.onRequestLocationPermitted()

        verify { view.getCurrentLocation() }
    }

    @Test
    fun onRoutePlanClicked() {
        coEvery { getActiveRoutePlanUseCase.execute(Unit)
            } returns GetActiveRoutePlanUseCase.Response(mockk())

        presenter.onRoutePlanClicked()

        coVerify {
            getActiveRoutePlanUseCase.execute(Unit)
            view.navigateToRoutePlan()
        }
    }

    @Test
    fun showCreateRoutePlanDialogWhenThereIsNoActiveRoutePlan() = runBlocking {
        coEvery { getActiveRoutePlanUseCase.execute(Unit)
            } throws NoActiveRoutePlanException()
        coEvery { createRoutePlanUseCase.execute(Unit)
            } returns CreateRoutePlanUseCase.Response(1234)

        presenter.onRoutePlanClicked()

        val onYesCaptor = slot<suspend () -> Unit>()
        coVerify {
            getActiveRoutePlanUseCase.execute(Unit)
            view.showCreateRoutePlan(capture(onYesCaptor))
        }
        onYesCaptor.captured.invoke()
        coVerify {
            createRoutePlanUseCase.execute(Unit)
            view.navigateToRoutePlan()
        }
    }

    @Test
    fun onLogBookClicked() {

        presenter.onLogBookClicked()

        verify { view.navigateToLogBook() }
    }

    @Test
    fun onShouldRequestLocationPermission() {

        presenter.onShouldRequestLocationPermission()

        verify { view.requestLocationPermission() }
    }

}