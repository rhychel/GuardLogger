package com.rhymartmanchus.guardlogger.screens.logbook

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.guardlogger.R
import com.rhymartmanchus.guardlogger.databinding.ItemLogbookBinding
import com.rhymartmanchus.guardlogger.domain.models.CheckInLog
import com.rhymartmanchus.guardlogger.screens.adapters.FlexiItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

data class CheckInLogItem(
    private val checkInLog: CheckInLog
) : FlexiItem() {

    override fun getLayoutRes(): Int = R.layout.item_logbook

    override fun createViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): FlexiItemViewHolder = ViewHolder(view, adapter)

    override fun bindViewHolder(
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>?,
        holder: FlexiItemViewHolder,
        position: Int,
        payloads: MutableList<Any>?
    ) {

        holder as ViewHolder

        with(holder.binding) {
            tvTime.text = "${checkInLog.startTime} - ${checkInLog.endTime}"
            tvDescription.text = checkInLog.log
            tvEmployeeName.setText(tvEmployeeName.context.getString(R.string.logged_by, checkInLog.employeeName))
        }
    }

    inner class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): FlexiItemViewHolder(view, adapter) {

        val binding: ItemLogbookBinding by lazy { ItemLogbookBinding.bind(view) }

    }
}