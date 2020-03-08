package com.pranjaldesai.coronavirustracker.data.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.databinding.ViewCityListItemBinding

class CityItemViewHolder(
    val binding: ViewCityListItemBinding
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(city: OverallCity, position: Int) = with(itemView) {
        binding.data = CityViewData(city)
        setOnClickListener { }
        binding.executePendingBindings()
    }

    class CityViewData(private val overallCity: OverallCity) {
        val title: String = generateTitle(overallCity.infectedProvince)
        val totalInfected: String = overallCity.totalInfectedCount.toString()
        val totalDeath: String = overallCity.totalDeathCount.toString()
        val totalRecovered: String = overallCity.totalRecoveredCount.toString()

        private fun generateTitle(cityName: String?): String {
            return if (cityName != null && cityName != "empty") {
                cityName
            } else {
                overallCity.countryName
            }
        }
    }
}