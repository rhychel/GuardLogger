package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.Session
import javax.inject.Inject

class LoginEmployeeUseCase @Inject constructor(
    private val gateway: ShiftsGateway
) : UseCase<LoginEmployeeUseCase.Params, LoginEmployeeUseCase.Response>() {

    data class Params (
        val employeeId: String,
        val pin: String
    )

    data class Response (
        val session: Session
    )

    override suspend fun execute(params: Params): Response {

        if(params.employeeId.isEmpty()) {
            throw IllegalArgumentException("Employee ID cannot be empty")
        }

        if(params.pin.isEmpty()) {
            throw IllegalArgumentException("PIN cannot be empty")
        }

        val session = gateway.login(params.employeeId, params.pin)
        gateway.saveSession(session)

        return Response(
            session
        )
    }

}