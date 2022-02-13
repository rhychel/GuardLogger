package com.rhymartmanchus.guardlogger.screens.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.guardlogger.R
import com.rhymartmanchus.guardlogger.databinding.ItemOptionsBinding
import com.rhymartmanchus.guardlogger.screens.adapters.FlexiItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

data class OptionsItem(
    val optionType: Type
) : FlexiItem() {

    sealed class Type {
        object RoutePlan : Type()
        object LogBook : Type()
    }

    override fun getLayoutRes(): Int = R.layout.item_options

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
        when(optionType) {
            Type.LogBook -> holder.binding.text1.text = "Log Book"
            Type.RoutePlan -> holder.binding.text1.text = "Route Plan"
        }
    }

    inner class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): FlexiItemViewHolder(view, adapter) {

        val binding: ItemOptionsBinding by lazy { ItemOptionsBinding.bind(view) }

    }
}