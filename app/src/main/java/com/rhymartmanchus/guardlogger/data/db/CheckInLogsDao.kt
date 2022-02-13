package com.rhymartmanchus.guardlogger.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.rhymartmanchus.guardlogger.data.db.models.CheckInLogDB
import com.rhymartmanchus.guardlogger.data.db.models.CheckInLogWithUserDB

@Dao
interface CheckInLogsDao {

    @Query("SELECT * FROM check_in_logs ORDER BY id DESC")
    suspend fun getAllCheckInLogs(): List<CheckInLogWithUserDB>

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun saveCheckInLog(checkInLogDB: CheckInLogDB)

}