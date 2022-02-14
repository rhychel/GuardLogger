package com.rhymartmanchus.guardlogger.screens.routeplan

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.guardlogger.R
import com.rhymartmanchus.guardlogger.databinding.ActivityRoutePlanBinding
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.domain.models.RoutePlan
import com.rhymartmanchus.guardlogger.domain.requests.RoutePlanRequests
import dagger.hilt.android.AndroidEntryPoint
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.resume
import kotlin.coroutines.suspendCoroutine
import kotlin.math.log

@AndroidEntryPoint
class RoutePlanActivity : AppCompatActivity(), RoutePlanContract.View, FlexibleAdapter.OnItemMoveListener, FlexibleAdapter.OnItemClickListener {

    @Inject lateinit var presenter: RoutePlanContract.Presenter
    lateinit var binding: ActivityRoutePlanBinding
    lateinit var adapter: FlexibleAdapter<RoutePlanItem>
    var fromLocationIndex: Int = -1
    var toLocationIndex: Int = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRoutePlanBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Route Plan"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = FlexibleAdapter(mutableListOf())

        binding.rvRoutePlan.layoutManager = LinearLayoutManager(this)
        binding.rvRoutePlan.adapter = adapter
        binding.rvRoutePlan.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        adapter.isLongPressDragEnabled = true
        adapter.addListener(this)

        presenter.takeView(this)
        presenter.onViewCreated()

        registerListener()
    }

    private fun registerListener() {
        with(binding) {
            efaReset.setOnClickListener {
                presenter.onResetPatrolLocationArrangementClicked()
            }
            efabOperation.setOnClickListener {
                val state = it.tag as String
                if(state == "1") {
                    presenter.onStartRouteClicked()
                } else {
                    presenter.onEndRouteClicked()
                }
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showFinishButton() {
        with(binding) {
            efabOperation.icon = ContextCompat.getDrawable(
                this@RoutePlanActivity,
                R.drawable.outline_check_circle_outline_black_24
            )
            efabOperation.text = getString(R.string.finish)
            efabOperation.tag = "0"
            efaReset.hide()
        }
    }

    override fun toastRoutePlanIsCompleted() {
        Toast.makeText(this, "Route plan is completed.", Toast.LENGTH_LONG).show()
    }

    override fun refreshLocations(hasStarted: Boolean, locations: List<PatrolLocation>) {
        adapter.clear()
        adapter.addItems(0, locations.map {
            RoutePlanItem(it, hasStarted)
        })
    }

    override fun renderRoutePlan(routePlan: RoutePlan, locations: List<PatrolLocation>) {
        adapter.addItems(0, locations.map {
            RoutePlanItem(it, routePlan.hasStarted)
        })
    }

    override fun resetRoutePlanLocations(locations: List<PatrolLocation>) {
        adapter.clear()
        adapter.addItems(0, locations.map {
            RoutePlanItem(it)
        })
        Toast.makeText(this, "Location sorting has been reset.", Toast.LENGTH_LONG).show()
    }

    override suspend fun showPatrolLogDialog(
        locationName: String,
        onSaveClicked: suspend (startTime: String, endTime: String, isCleared: Boolean, logs: String) -> Unit
    ) {
        suspendCoroutine<Unit> {
            val dialog = PatrolLogDialogFragment()
            dialog.arguments = Bundle().apply {
                putString("LOCATION_NAME", locationName)
            }
            dialog.onSaveLogClickedListener = PatrolLogDialogFragment.OnSaveLogClickedListener { starTime, endTime, isCleared, description ->
                lifecycleScope.launch {
                    onSaveClicked.invoke(starTime, endTime, isCleared, description)
                }
                it.resume(Unit)
                dialog.dismiss()
            }
            dialog.onDismissClickedListener = PatrolLogDialogFragment.OnDismissClickedListener {
                it.resume(Unit)
            }
            dialog.show(supportFragmentManager, "PATROL_LOG")
        }
    }

    override fun refreshPatrolLocation(patrolLocation: PatrolLocation) {
        adapter.updateItem(
            RoutePlanItem(patrolLocation, true)
        )
    }

    override fun navigateToHome() {
        finish()
    }

    override fun onActionStateChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {
        if(actionState == ItemTouchHelper.ACTION_STATE_IDLE) {
            val fromLocation = adapter.getItem(fromLocationIndex) ?: throw IllegalStateException("Item is null")
            val toLocation = adapter.getItem(toLocationIndex) ?: throw IllegalStateException("Item is null")
            presenter.rearrangePatrolLocation(
                fromLocation.patrolLocation,
                toLocation.patrolLocation
            )
        }
    }

    override fun shouldMoveItem(fromPosition: Int, toPosition: Int): Boolean = true

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        fromLocationIndex = fromPosition
        toLocationIndex = toPosition
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        presenter.onLocationClicked(
            adapter.getItem(position)?.patrolLocation ?: throw IllegalArgumentException("Patrol location cannot be null")
        )
        return true
    }
}