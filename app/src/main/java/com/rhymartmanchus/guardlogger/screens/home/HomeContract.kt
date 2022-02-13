package com.rhymartmanchus.guardlogger.screens.home

import com.rhymartmanchus.guardlogger.domain.models.Weather

sealed interface HomeContract {

    interface View {

        fun renderCurrentWeather(
            weather: Weather
        )

    }

    interface Presenter {

        fun onViewCreated()
        fun onCurrentLocationDetermined(
            latitude: Double,
            longitude: Double
        )
        fun onRoutePlanClicked()
        fun onLogBookClicked()

    }

}