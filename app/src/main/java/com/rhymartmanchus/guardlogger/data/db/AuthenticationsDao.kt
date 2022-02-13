package com.rhymartmanchus.guardlogger.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query
import com.rhymartmanchus.guardlogger.data.db.models.UserDB

@Dao
interface AuthenticationsDao {

    @Query("SELECT * FROM users WHERE employeeId = :employeeId AND pin = :pin")
    suspend fun authenticate(employeeId: String, pin: String): UserDB?

    @Query("SELECT * FROM users WHERE employeeId = :employeeId")
    suspend fun getUserByEmployeeId(employeeId: String): UserDB?

    @Insert(onConflict = REPLACE)
    suspend fun saveUser(userDB: UserDB)

}