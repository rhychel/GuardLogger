package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.SecurityLogsGateway
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.domain.requests.CheckInRequest
import javax.inject.Inject

class SaveCheckInLogUseCase @Inject constructor(
    private val gateway: SecurityLogsGateway,
    private val shiftsGateway: ShiftsGateway
) : UseCase<SaveCheckInLogUseCase.Params, SaveCheckInLogUseCase.Response>() {

    data class Params (
        val startTime: String,
        val endTime: String,
        val description: String
    )

    data class Response(
        val checkInLog: CheckInLog
    )

    override suspend fun execute(params: Params): Response {

        if(params.startTime.isEmpty()) {
            throw IllegalArgumentException("Start Time cannot be empty")
        }
        if(params.endTime.isEmpty()) {
            throw IllegalArgumentException("End Time cannot be empty")
        }
        if(params.description.isEmpty()) {
            throw IllegalArgumentException("Description cannot be empty")
        }

        val session = shiftsGateway.getCurrentSession()
        gateway.saveCheckInLog(
            CheckInRequest(
                session.userId,
                params.startTime,
                params.endTime,
                params.description
            )
        )
        return Response(
            CheckInLog(
                session.name,
                params.startTime,
                params.endTime,
                params.description
            )
        )
    }

}