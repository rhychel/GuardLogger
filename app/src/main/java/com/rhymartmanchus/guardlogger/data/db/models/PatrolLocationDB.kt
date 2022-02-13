package com.rhymartmanchus.guardlogger.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "patrol_locations"
)
data class PatrolLocationDB (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "routeId")
    val routeId: Int,

    @ColumnInfo(name = "areaLocationId")
    val areaLocationId: Int,

    @ColumnInfo(name = "name")
    val name: String,

    @ColumnInfo(name = "startTime")
    val startTime: String,

    @ColumnInfo(name = "endTime")
    val endTime: String,

    @ColumnInfo(name = "isVisited")
    val isVisited: Boolean,

    @ColumnInfo(name = "isCleared")
    val isCleared: Boolean,

    @ColumnInfo(name = "sorting")
    val sorting: String,

    @ColumnInfo(name = "remarks")
    val remarks: String?
)