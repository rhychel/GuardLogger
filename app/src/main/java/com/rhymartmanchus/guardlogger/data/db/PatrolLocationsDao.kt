package com.rhymartmanchus.guardlogger.data.db

import androidx.room.*
import com.rhymartmanchus.guardlogger.data.db.models.PatrolLocationDB

@Dao
interface PatrolLocationsDao {

    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun savePatrolLocations(patrolLocations: List<PatrolLocationDB>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun savePartolLocationLog(patrolLocationDB: PatrolLocationDB)

    @Query("UPDATE patrol_locations SET sorting = :sorting WHERE id = :patrolLocationId")
    suspend fun savePatrolLocationSorting(patrolLocationId: Int, sorting: String)

    @Query("DELETE FROM patrol_locations WHERE routeId = :routePlanId")
    suspend fun deletePatrolLocations(routePlanId: Int)

    @Query("SELECT * FROM patrol_locations WHERE id = :id")
    suspend fun getPatrolLocationById(id: Int): PatrolLocationDB?

}