package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import javax.inject.Inject

class SavePatrolLocationSortingUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway
) : UseCase<SavePatrolLocationSortingUseCase.Params, Unit>() {

    data class Params(
        val fromLocationId: Int,
        val fromSorting: String,
        val toLocationId: Int,
        val toSorting: String
    )

    override suspend fun execute(params: Params) {
        gateway.saveRoutePlan(
            RoutePlanRequests.ArrangeRouteLocation(
                params.fromLocationId,
                params.fromSorting,
                params.toLocationId,
                params.toSorting
            )
        )
    }

}