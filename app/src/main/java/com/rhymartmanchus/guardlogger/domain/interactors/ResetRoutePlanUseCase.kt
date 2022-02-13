package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import javax.inject.Inject

class ResetRoutePlanUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway,
    private val shiftsGateway: ShiftsGateway
) : UseCase<Unit, ResetRoutePlanUseCase.Response>() {

    data class Response (
        val resetRoutePlan: RoutePlan
    )

    override suspend fun execute(params: Unit): Response {
        val session = shiftsGateway.getCurrentSession()
        val currentRoutePlan = gateway.getPatrolRoutePlan(session.userId)
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
        gateway.saveRoutePlan(
            RoutePlanRequests.Reset(
                currentRoutePlan.id,
                patrolLocations
            )
        )
        return Response(
            gateway.getPatrolRoutePlan(session.userId)
        )
    }

}