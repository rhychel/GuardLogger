package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import javax.inject.Inject

class SavePatrolLogUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway
) : UseCase<SavePatrolLogUseCase.Params, Unit>() {

    data class Params(
        val patrolLocationId: Int,
        val startTime: String,
        val endTime: String,
        val isCleared: Boolean,
        val remarks: String?
    )

    override suspend fun execute(params: Params) {
        gateway.saveRoutePlan(
            RoutePlanRequests.SaveLog(
                params.startTime,
                params.endTime,
                params.patrolLocationId,
                params.isCleared,
                params.remarks
            )
        )
    }
}