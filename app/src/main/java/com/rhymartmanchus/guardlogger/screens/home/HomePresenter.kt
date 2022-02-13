package com.rhymartmanchus.guardlogger.screens.home

import android.location.Location
import com.rhymartmanchus.guardlogger.domain.IAppDispatchers
import com.rhymartmanchus.guardlogger.domain.exceptions.NoActiveRoutePlanException
import com.rhymartmanchus.guardlogger.domain.interactors.CreateRoutePlanUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.FetchCurrentWeatherByCoordinatesUseCase
import com.rhymartmanchus.guardlogger.domain.interactors.GetActiveRoutePlanUseCase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext

class HomePresenter @Inject constructor(
    private val appDispatcher: IAppDispatchers,
    private val fetchCurrentWeatherByCoordinatesUseCase: FetchCurrentWeatherByCoordinatesUseCase,
    private val getActiveRoutePlanUseCase: GetActiveRoutePlanUseCase,
    private val createRoutePlanUseCase: CreateRoutePlanUseCase
) : HomeContract.Presenter, CoroutineScope {

    val job = SupervisorJob()
    override val coroutineContext: CoroutineContext
        get() = appDispatcher.ui() + job

    private var view: HomeContract.View? = null

    override fun takeView(view: HomeContract.View) {
        this.view = view
    }

    override fun onCurrentLocationGathered(location: Location) {
        launch {
            try {
                val weather = withContext(appDispatcher.io()) {
                    fetchCurrentWeatherByCoordinatesUseCase.execute(
                        FetchCurrentWeatherByCoordinatesUseCase.Params(
                            location.latitude,
                            location.longitude
                        )
                    )
                }.currentWeather
                view?.renderCurrentWeather(weather)
            } catch (e: Exception) {
                view?.toastError(e.message ?: "Unknown error: ${e.cause}")
            }
        }
    }

    override fun onViewCreated() {
        view?.requestLocationPermission()
    }

    override fun onRoutePlanClicked() {
        launch {
            try {
                withContext(appDispatcher.io()) {
                    getActiveRoutePlanUseCase.execute(Unit)
                }
                view?.navigateToRoutePlan()
            } catch (e: NoActiveRoutePlanException) {
                view?.showCreateRoutePlan {
                    createRoutePlanUseCase.execute(Unit)
                    view?.navigateToRoutePlan()
                }
            } catch (e: Exception) {
                view?.toastError(e.message ?: "Unknown error: ${e.cause}")
            }
        }
    }

    override fun onLogBookClicked() {
        view?.navigateToLogBook()
    }

    override fun onRequestLocationPermitted() {
        view?.getCurrentLocation()
    }

    override fun onShouldRequestLocationPermission() {
        view?.showNeedsLocationPermission()
    }

    override fun detachView() {
        this.view = null
    }

}