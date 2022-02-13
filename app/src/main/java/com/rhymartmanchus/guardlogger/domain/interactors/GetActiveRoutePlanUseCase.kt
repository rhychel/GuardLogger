package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.exceptions.NoActiveRoutePlanException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import javax.inject.Inject

class GetActiveRoutePlanUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway,
    private val shiftsGateway: ShiftsGateway
) : UseCase<Unit, GetActiveRoutePlanUseCase.Response>() {

    data class Response (
        val routePlan: RoutePlan
    )

    override suspend fun execute(params: Unit): Response {
        val session = shiftsGateway.getCurrentSession()
        return Response(
            try {
                gateway.getPatrolRoutePlan(session.userId)
            } catch (e: NoDataException) {
                throw NoActiveRoutePlanException()
            }
        )
    }

}