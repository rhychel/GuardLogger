package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import javax.inject.Inject

class EndRouteUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway
) : UseCase<EndRouteUseCase.Params, Unit>() {

    data class Params(
        val routePlanId: Int,
        val time: String
    )

    override suspend fun execute(params: Params) {
        gateway.saveRoutePlan(
            RoutePlanRequests.End(
                params.routePlanId,
                params.time
            )
        )
    }
}