package com.rhymartmanchus.guardlogger.data

import com.rhymartmanchus.guardlogger.data.api.OpenWeatherEndpoints
import com.rhymartmanchus.guardlogger.domain.WeathersGateway
import com.rhymartmanchus.guardlogger.domain.models.Weather
import retrofit2.Retrofit
import javax.inject.Inject

class WeathersRepository @Inject constructor(
    private val retrofit: Retrofit
) : WeathersGateway {

    override suspend fun fetchWeatherByCoordinates(latitude: Double, longitude: Double): Weather =
        retrofit.create(OpenWeatherEndpoints::class.java)
            .fetchCurrentWeather(
                latitude,
                longitude,
                "ef179075dd06b7dfe4cdfa6ee094104d"
            )
            .let {
                Weather(
                    it.weather[0].main,
                    it.weather[0].description,
                    it.weather[0].iconCode,
                    it.cityName,
                    it.country,
                    it.temperature,
                    it.minimumTemperature,
                    it.maximumTemperature,
                    it.cloudsPercentage
                )
            }

}