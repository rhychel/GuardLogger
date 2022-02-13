package com.rhymartmanchus.guardlogger.data

import android.content.SharedPreferences
import com.rhymartmanchus.guardlogger.data.db.AuthenticationsDao
import com.rhymartmanchus.guardlogger.data.db.models.UserDB
import com.rhymartmanchus.guardlogger.domain.ShiftsGateway
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeAlreadyRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.EmployeeNotRegisteredException
import com.rhymartmanchus.guardlogger.domain.exceptions.NoDataException
import com.rhymartmanchus.guardlogger.domain.models.Session
import javax.inject.Inject

class ShiftsRepository @Inject constructor(
    private val authenticationsDao: AuthenticationsDao,
    private val sharedPreferences: SharedPreferences
) : ShiftsGateway {

    override suspend fun getCurrentSession(): Session {
        val userId = sharedPreferences.getInt("USER_ID", -1)
        if(userId == -1) {
            throw NoDataException()
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

    override suspend fun login(employeeId: String, pin: String): Session {
        val user = authenticationsDao.authenticate(employeeId, pin)
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
                    0,
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
            .putInt("USER_ID", session.userId)
            .commit()
    }

}