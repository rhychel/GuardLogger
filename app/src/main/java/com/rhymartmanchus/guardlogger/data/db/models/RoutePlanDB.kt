package com.rhymartmanchus.guardlogger.data.db.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "route_plans"
)
data class RoutePlanDB (
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    val id: Int,

    @ColumnInfo(name = "hasStarted")
    val hasStarted: Boolean,

    @ColumnInfo(name = "isDone")
    val isDone: Boolean,

    @ColumnInfo(name = "startTime")
    val startTime: String,

    @ColumnInfo(name = "endTime")
    val endTime: String,

    @ColumnInfo(name = "userId")
    val userId: String
)