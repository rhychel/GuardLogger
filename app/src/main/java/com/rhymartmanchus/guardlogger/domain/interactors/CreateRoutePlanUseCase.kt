package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import javax.inject.Inject

class CreateRoutePlanUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway,
    private val shiftsGateway: ShiftsGateway
) : UseCase<Unit, CreateRoutePlanUseCase.Response>() {

    data class Response (
        val routePlanId: Int
    )

    override suspend fun execute(params: Unit): Response {
        val session = shiftsGateway.getCurrentSession()
        val locations = gateway.getLocations()
        val patrolLocations = mutableListOf<PatrolLocation>()
        locations.forEach {
            patrolLocations.add(
                PatrolLocation(
                    0,
                    it.id,
                    "...",
                    it.name,
                    "",
                    "",
                    false,
                    false,
                    null
                )
            )
        }

        return Response(
            gateway.saveRoutePlan(
                RoutePlanRequests.Create(
                    session.userId,
                    patrolLocations
                )
            )
        )
    }

}