package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.AreaLocation
import javax.inject.Inject

class GetLocationsUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway
) : UseCase<Unit, GetLocationsUseCase.Response>() {

    data class Response (
        val locations: List<AreaLocation>
    )

    override suspend fun execute(params: Unit): Response =
        Response(
            gateway.getLocations()
        )
}