package com.rhymartmanchus.guardlogger.screens.logbook

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.rhymartmanchus.guardlogger.databinding.ActivityLogBookBinding
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import dagger.hilt.android.AndroidEntryPoint
import eu.davidea.flexibleadapter.FlexibleAdapter
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LogBookActivity : AppCompatActivity(), LogBookContract.View, FlexibleAdapter.OnItemClickListener {

    @Inject lateinit var presenter: LogBookContract.Presenter
    lateinit var binding: ActivityLogBookBinding
    lateinit var adapter: FlexibleAdapter<CheckInLogItem>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLogBookBinding.inflate(layoutInflater)
        setContentView(binding.root)

        title = "Log Book"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        adapter = FlexibleAdapter(mutableListOf())
        adapter.addListener(this)
        binding.rvLogs.layoutManager = LinearLayoutManager(this)
        binding.rvLogs.adapter = adapter
        binding.rvLogs.addItemDecoration(DividerItemDecoration(this, DividerItemDecoration.VERTICAL))

        presenter.takeView(this)
        presenter.onViewCreated()
        registerListener()
    }

    private fun registerListener() {
        with(binding) {
            efabAddLog.setOnClickListener {
                presenter.onAddLogClicked()
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if(item.itemId == android.R.id.home) {
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    override fun showLogDetailsDialog(checkInLog: CheckInLog) {
        val dialog = LogDetailsDialogFragment(checkInLog)
        dialog.show(supportFragmentManager, "SHOW_LOG")
    }

    override fun showAddLogDialog(onSaveClicked: suspend (startTime: String, endTime: String, logs: String) -> Unit) {
        val dialog = AddLogDialogFragment()
        dialog.onSaveLogClickedListener = AddLogDialogFragment.OnSaveLogClickedListener { starTime, endTime, description ->
            lifecycleScope.launch {
                onSaveClicked.invoke(starTime, endTime, description)
            }
            dialog.dismiss()
        }
        dialog.show(supportFragmentManager, "ADD_LOG")
    }

    override fun renderCheckInLogs(checkInLogs: List<CheckInLog>) {
        adapter.addItems(0, checkInLogs.map {
            CheckInLogItem(it)
        })
    }

    override fun hideNoLogsAvailable() {
        with(binding) {
            tvNoItems.visibility = View.GONE
        }
    }

    override fun appendCheckInLog(checkInLog: CheckInLog) {
        adapter.addItem(0, checkInLog.let {
            CheckInLogItem(it)
        })
    }

    override fun onItemClick(view: View?, position: Int): Boolean {
        presenter.onLogClicked(position)
        return true
    }
}