package com.rhymartmanchus.guardlogger.screens.home

import android.location.Location
import com.rhymartmanchus.guardlogger.domain.models.Weather

sealed interface HomeContract {

    interface View {

        fun renderCurrentWeather(
            weather: Weather
        )

        fun requestLocationPermission()
        fun getCurrentLocation()
        fun showNeedsLocationPermission()
        fun showCreateRoutePlan(
            onYesClicked: suspend () -> Unit
        )

        fun navigateToRoutePlan()
        fun navigateToLogBook()

        fun toastError(message: String)
    }

    interface Presenter {

        fun takeView(view: View)
        fun onCurrentLocationGathered(location: Location)
        fun onViewCreated()
        fun onRoutePlanClicked()
        fun onLogBookClicked()
        fun onRequestLocationPermitted()
        fun onShouldRequestLocationPermission()
        fun detachView()

    }

}