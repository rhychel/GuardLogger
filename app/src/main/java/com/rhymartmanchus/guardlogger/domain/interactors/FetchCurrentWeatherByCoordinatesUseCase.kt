package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.WeathersGateway
import com.rhymartmanchus.guardlogger.domain.models.Weather
import javax.inject.Inject

class FetchCurrentWeatherByCoordinatesUseCase @Inject constructor(
    private val gateway: WeathersGateway
) : UseCase<FetchCurrentWeatherByCoordinatesUseCase.Params,
        FetchCurrentWeatherByCoordinatesUseCase.Response>() {

    data class Params (
        val latitude: Double,
        val longitude: Double
    )

    data class Response (
        val currentWeather: Weather
    )

    override suspend fun execute(params: Params): Response =
        Response(
            gateway.fetchWeatherByCoordinates(params.latitude, params.longitude)
        )

}