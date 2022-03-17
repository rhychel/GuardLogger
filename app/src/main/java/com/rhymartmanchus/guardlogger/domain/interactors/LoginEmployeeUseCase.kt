package com.rhymartmanchus.guardlogger.domain.interactors

import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.UseCase
import com.rhymartmanchus.guardlogger.domain.models.Session
import com.rhymartmanchus.guardlogger.utils.Validator
import javax.inject.Inject

class LoginEmployeeUseCase @Inject constructor(
    private val gateway: ShiftsGateway
) : UseCase<LoginEmployeeUseCase.Params, LoginEmployeeUseCase.Response>() {

    data class Params (
        val email: String,
        val password: String
    )

    data class Response (
        val session: Session
    )

    override suspend fun execute(params: Params): Response {

        if(params.email.isEmpty()) {
            throw IllegalArgumentException("Email cannot be empty")
        }

        if(!Validator.isEmailValid(params.email)) {
            throw IllegalArgumentException("Email is invalid")
        }

        if(params.password.isEmpty()) {
            throw IllegalArgumentException("Password cannot be empty")
        }

        val session = gateway.login(params.email, params.password)
        gateway.saveSession(session)

        return Response(
            session
        )
    }

}