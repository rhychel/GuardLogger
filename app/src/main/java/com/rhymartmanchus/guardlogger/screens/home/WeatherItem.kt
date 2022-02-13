package com.rhymartmanchus.guardlogger.screens.home

import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.rhymartmanchus.guardlogger.R
import com.rhymartmanchus.guardlogger.databinding.ItemCurrentlocationWeatherBinding
import com.rhymartmanchus.guardlogger.domain.models.Weather
import com.rhymartmanchus.guardlogger.screens.adapters.FlexiItem
import eu.davidea.flexibleadapter.FlexibleAdapter
import eu.davidea.flexibleadapter.items.IFlexible
import android.graphics.Bitmap
import com.rhymartmanchus.guardlogger.Constants
import com.rhymartmanchus.guardlogger.utils.TemperatureConverter

import com.squareup.picasso.Picasso
import java.math.BigDecimal


data class WeatherItem (
    val weather: Weather?,
    val isLoading: Boolean = true
) : FlexiItem() {

    override fun getLayoutRes(): Int = R.layout.item_currentlocation_weather

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

        if(!isLoading) {
            with(holder.binding) {
                weather?.let {
                    Picasso.get()
                        .load(Constants.IMAGE_BASE_URL + weather.icon + "@4x.png")
                        .resize(100, 100)
                        .config(Bitmap.Config.ARGB_8888)
                        .centerInside()
                        .into(ivTempIcon)
                    tvMain.text = weather.main
                    tvLocation.text = "${weather.city}, ${weather.country}"
                    tvDescription.text = weather.description
                    tvTemp.text = "${BigDecimal(TemperatureConverter.kelvinToCelsius(weather.temperature)).toInt()}°"
                    tvTempMin.text = "${BigDecimal(TemperatureConverter.kelvinToCelsius(weather.minimumTemperature)).toInt()}°C"
                    tvTempMax.text = "${BigDecimal(TemperatureConverter.kelvinToCelsius(weather.maximumTemperature)).toInt()}°C"

                    ivTempIcon.visibility = View.VISIBLE
                    tvMain.visibility = View.VISIBLE
                    tvLocation.visibility = View.VISIBLE
                    tvDescription.visibility = View.VISIBLE
                    tvTemp.visibility = View.VISIBLE
                    tvTempMin.visibility = View.VISIBLE
                    tvTempMax.visibility = View.VISIBLE
                }
                pbLoading.visibility = View.GONE
            }
        }

    }

    inner class ViewHolder(
        view: View,
        adapter: FlexibleAdapter<IFlexible<RecyclerView.ViewHolder>>
    ): FlexiItemViewHolder(view, adapter) {

        val binding: ItemCurrentlocationWeatherBinding by lazy { ItemCurrentlocationWeatherBinding.bind(view) }

    }
}