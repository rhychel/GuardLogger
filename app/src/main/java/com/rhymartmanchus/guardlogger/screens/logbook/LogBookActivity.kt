package com.rhymartmanchus.guardlogger.screens.logbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rhymartmanchus.guardlogger.R

class LogBookActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_log_book)

        title = "Log Book"
    }
}