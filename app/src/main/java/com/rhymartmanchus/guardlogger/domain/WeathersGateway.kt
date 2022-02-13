package com.rhymartmanchus.guardlogger.domain

import com.rhymartmanchus.guardlogger.domain.models.Weather

interface WeathersGateway {

    suspend fun fetchWeatherByCoordinates(
        latitude: Double,
        longitude: Double
    ): Weather

}