package com.rhymartmanchus.guardlogger.data.db

import androidx.room.Dao
import androidx.room.Query
import com.rhymartmanchus.guardlogger.data.db.models.AreaLocationDB

@Dao
interface AreaLocationsDao {

    @Query("SELECT * FROM area_locations ORDER BY sorting")
    suspend fun getAllAreaLocations(): List<AreaLocationDB>

}