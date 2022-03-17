package com.rhymartmanchus.guardlogger.domain

import com.rhymartmanchus.guardlogger.domain.models.Session

interface ShiftsGateway {

    suspend fun getCurrentSession(): Session

    suspend fun login(email: String, password: String): Session
    suspend fun signup(employeeId: String, name: String, pin: String)
    suspend fun saveSession(session: Session)

}