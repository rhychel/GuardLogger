package com.rhymartmanchus.guardlogger.data.db.models

import androidx.room.Embedded
import androidx.room.Relation

data class UserWithCheckInLogsDB (
    @Embedded
    val user: UserDB,

    @Relation(
        parentColumn = "id",
        entityColumn = "userId"
    )
    val checkInLogs: List<CheckInLogDB>
)