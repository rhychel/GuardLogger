package com.rhymartmanchus.guardlogger.data

import android.content.SharedPreferences
import com.google.firebase.auth.FirebaseAuth
import com.rhymartmanchus.guardlogger.data.db.AuthenticationsDao
import com.rhymartmanchus.guardlogger.data.db.models.UserDB
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeAlreadyRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeNotRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.Session
import java.util.*
import javax.inject.Inject

class ShiftsRepository @Inject constructor(
    private val authenticationsDao: AuthenticationsDao,
    private val sharedPreferences: SharedPreferences,
    private val firebaseAuth: FirebaseAuth
) : ShiftsGateway {

    override suspend fun getCurrentSession(): Session {
        val userId = sharedPreferences.getString("USER_ID", "")
            ?: throw IllegalStateException("USER_ID is empty")
        if(userId.isEmpty()) {
            throw IllegalStateException("USER_ID is empty")
        }
        val name = sharedPreferences.getString("NAME", "")
            ?: throw IllegalStateException("NAME is empty")
        if(name.isEmpty())
            throw IllegalStateException("NAME is empty")
        return Session(
            userId,
            name
        )
    }

    override suspend fun login(email: String, password: String): Session {
        val user = authenticationsDao.authenticate(email, password)
            ?: throw EmployeeNotRegisteredException()
        return Session(
            user.id,
            user.name
        )
    }

    override suspend fun signup(employeeId: String, name: String, pin: String) {
        val user = authenticationsDao.getUserByEmployeeId(employeeId)
        if(user == null) {
            authenticationsDao.saveUser(
                UserDB(
                    UUID.randomUUID().toString(),
                    name, employeeId, pin
                )
            )
        } else {
            throw EmployeeAlreadyRegisteredException()
        }
    }

    override suspend fun saveSession(session: Session) {
        sharedPreferences.edit()
            .putString("NAME", session.name)
            .putString("USER_ID", session.userId)
            .commit()
    }

}