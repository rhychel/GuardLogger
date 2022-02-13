package com.rhymartmanchus.guardlogger.data.db.models

import androidx.room.Embedded
import androidx.room.Relation

data class CheckInLogWithUserDB (
    @Embedded
    val checkInLog: CheckInLogDB,
    @Relation(
        parentColumn = "userId",
        entityColumn = "id"
    )
    val user: UserDB
)
