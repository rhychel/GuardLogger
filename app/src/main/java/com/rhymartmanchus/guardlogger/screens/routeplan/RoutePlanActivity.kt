package com.rhymartmanchus.guardlogger.screens.routeplan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.rhymartmanchus.guardlogger.R

class RoutePlanActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_route_plan)
        title = "Route Plan"
    }
}