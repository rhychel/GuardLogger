package com.rhymartmanchus.guardlogger.screens.home

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationRequest
import com.google.android.gms.location.LocationServices
import com.google.android.gms.tasks.CancellationTokenSource
import com.rhymartmanchus.guardlogger.databinding.ActivityHomeBinding
import com.rhymartmanchus.guardlogger.domain.models.Weather
import com.rhymartmanchus.guardlogger.screens.adapters.FlexiItem
import com.rhymartmanchus.guardlogger.screens.logbook.LogBookActivity
import com.rhymartmanchus.guardlogger.screens.routeplan.RoutePlanActivity
import dagger.hilt.android.AndroidEntryPoint
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class HomeActivity : AppCompatActivity(), HomeContract.View, FlexibleAdapter.OnItemClickListener {

    @Inject lateinit var presenter: HomeContract.Presenter
    private lateinit var optionsAdapter: FlexibleAdapter<FlexiItem>

    lateinit var binding: ActivityHomeBinding

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private val locationRequest = registerForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        when {
            permissions[Manifest.permission.ACCESS_FINE_LOCATION] ?: false -> {
                presenter.onRequestLocationPermitted()
            }
            permissions[Manifest.permission.ACCESS_COARSE_LOCATION] ?: false -> {
                presenter.onRequestLocationPermitted()
            } else -> {
                presenter.onShouldRequestLocationPermission()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(binding.root)
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)


        optionsAdapter = FlexibleAdapter(mutableListOf())
        binding.rvHome.layoutManager = LinearLayoutManager(this)
        binding.rvHome.adapter = optionsAdapter

        optionsAdapter.addItems(0,
            mutableListOf(WeatherItem(null),
                OptionsItem(OptionsItem.Type.LogBook),
                OptionsItem(OptionsItem.Type.RoutePlan)))
        optionsAdapter.notifyItemInserted(0)
        optionsAdapter.addListener(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.takeView(this)
        presenter.onViewCreated()
    }

    override fun onPause() {
        super.onPause()
        presenter.detachView()
    }

    override fun renderCurrentWeather(weather: Weather) {
        optionsAdapter.updateItem(0, WeatherItem(weather, false), null)
        optionsAdapter.notifyItemChanged(0)
    }

    override fun requestLocationPermission() {
        locationRequest.launch(
            arrayOf(
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            )
        )
    }

    override fun getCurrentLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            presenter.onShouldRequestLocationPermission()
            return
        }
        fusedLocationClient.getCurrentLocation(LocationRequest.PRIORITY_BALANCED_POWER_ACCURACY, CancellationTokenSource().token)
            .addOnSuccessListener {
                it?.let {
                    presenter.onCurrentLocationGathered(it)
                }
            }
    }

    override fun showNeedsLocationPermission() {
        Toast.makeText(this, "Weather needs location permission", Toast.LENGTH_LONG).show()
    }

    override fun showCreateRoutePlan(onYesClicked: suspend () -> Unit) {
        AlertDialog.Builder(this)
            .setTitle("No Active Route Plan")
            .setMessage("Create a new one?")
            .setPositiveButton("Yes") { _,_ ->
                lifecycleScope.launch {
                    onYesClicked.invoke()
                }
            }
            .setNegativeButton("Later") { _,_-> }
            .show()
    }

    override fun navigateToRoutePlan() {
        startActivity(Intent(this, RoutePlanActivity::class.java))
    }

    override fun navigateToLogBook() {
        startActivity(Intent(this, LogBookActivity::class.java))
    }

    override fun toastError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        optionsAdapter.getItem(position)?.let {
            if(it is OptionsItem) {
                when(it.optionType) {
                    OptionsItem.Type.LogBook -> presenter.onLogBookClicked()
                    OptionsItem.Type.RoutePlan -> presenter.onRoutePlanClicked()
                }
            }
        }
        return true
    }

}