package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import javax.inject.Inject

class SavePatrolLocationSortingUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway
) : UseCase<SavePatrolLocationSortingUseCase.Params, Unit>() {

    data class Params(
        val patrolLocationId: Int,
        val sorting: String
    )

    override suspend fun execute(params: Params) {
        gateway.saveRoutePlan(
            RoutePlanRequests.SaveArrangementLocation(
                params.patrolLocationId,
                params.sorting
            )
        )
    }

}