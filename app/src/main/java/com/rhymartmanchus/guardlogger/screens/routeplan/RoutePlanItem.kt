package com.rhymartmanchus.guardlogger.screens.routeplan

import android.view.View
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.guardlogger.R
import com.rhymartmanchus.guardlogger.databinding.ItemRouteLocationBinding
import com.rhymartmanchus.guardlogger.domain.models.PatrolLocation
import com.rhymartmanchus.guardlogger.screens.adapters.FlexiItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible

data class RoutePlanItem(
    val patrolLocation: PatrolLocation,
    val hasStarted: Boolean = false
) : FlexiItem() {
    override fun getLayoutRes(): Int = R.layout.item_route_location

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
            tvLocation.text = patrolLocation.name

            if(hasStarted) {
                tvLocation.text = when {
                    patrolLocation.isCleared && patrolLocation.isVisited -> {
                        "${patrolLocation.name} (CLEARED)"
                    }
                    !patrolLocation.isCleared && patrolLocation.isVisited -> {
                        "${patrolLocation.name} (WITH ISSUE)"
                    }
                    else -> patrolLocation.name
                }
                ivState.setImageDrawable(ContextCompat.getDrawable(ivState.context, R.drawable.outline_circle_black_24))
                if(patrolLocation.isVisited) {
                    ivState.setImageDrawable(ContextCompat.getDrawable(ivState.context, R.drawable.outline_check_circle_outline_black_24))
                }
            }
        }

    }

    override fun isEnabled(): Boolean = !patrolLocation.isVisited
    override fun isDraggable(): Boolean = !hasStarted

    inner class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): FlexiItemViewHolder(view, adapter) {

        val binding: ItemRouteLocationBinding by lazy { ItemRouteLocationBinding.bind(view) }

    }
}