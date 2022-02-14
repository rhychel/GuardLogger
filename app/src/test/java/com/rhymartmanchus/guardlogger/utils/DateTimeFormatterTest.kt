package com.rhymartmanchus.guardlogger.utils

import org.junit.Assert.*

import org.junit.Test
import java.util.*

class DateTimeFormatterTest {

    @Test
    fun `Should have the format of datetime`() {
        val expected = "Feb 14, 2022 - 11:04 AM"

        val dateTime = Calendar.getInstance()
        dateTime.set(Calendar.MONTH, 1)
        dateTime.set(Calendar.DATE, 14)
        dateTime.set(Calendar.YEAR, 2022)
        dateTime.set(Calendar.HOUR, 11)
        dateTime.set(Calendar.MINUTE, 4)
        dateTime.set(Calendar.AM_PM, Calendar.AM)
        
        assertEquals(expected, DateTimeFormatter.toDateTime(dateTime.time))
    }
}