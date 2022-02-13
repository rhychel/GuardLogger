package com.rhymartmanchus.guardlogger.domain.models

data class CheckInLog (
    val employeeName: String,
    val startTime: String,
    val endTime: String,
    val log: String
)