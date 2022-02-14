package com.rhymartmanchus.guardlogger.utils

import java.text.SimpleDateFormat
import java.util.*

object DateTimeFormatter {

    fun toDateTime(date: Date): String {
        val formatter = SimpleDateFormat("LLL d, yyyy - hh:mm a", Locale.getDefault())
        return formatter.format(date)
    }

}