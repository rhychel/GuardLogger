package com.rhymartmanchus.guardlogger.screens.adapters

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.AbstractFlexibleItem
import eu.davidea.flexibleadapter.items.IFlexible
import eu.davidea.viewholders.FlexibleViewHolder

abstract class FlexiItem: AbstractFlexibleItem<FlexiItem.FlexiItemViewHolder>() {

    abstract class FlexiItemViewHolder(
        private val itemView: View,
        private val flexibleAdapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>,
        private val stickyHeader: Boolean = true
    ): FlexibleViewHolder(itemView, flexibleAdapter, stickyHeader)

}
