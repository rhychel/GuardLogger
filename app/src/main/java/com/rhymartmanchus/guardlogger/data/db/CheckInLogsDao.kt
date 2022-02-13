package com.rhymartmanchus.guardlogger.data.db

import androidx.room.*
import com.rhymartmanchus.guardlogger.data.db.models.CheckInLogDB
import com.rhymartmanchus.guardlogger.data.db.models.CheckInLogWithUserDB

@Dao
interface CheckInLogsDao {

    @Transaction
    @Query("SELECT * FROM check_in_logs ORDER BY id DESC")
    suspend fun getAllCheckInLogs(): List<CheckInLogWithUserDB>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveCheckInLog(checkInLogDB: CheckInLogDB)

}