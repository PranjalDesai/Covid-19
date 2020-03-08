package com.pranjaldesai.coronavirustracker.data.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.databinding.ViewCountryListItemBinding

class CountryItemViewHolder(
    val binding: ViewCountryListItemBinding,
    private val listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(country: OverallCountry, position: Int) = with(itemView) {
        binding.data = CountryViewData(country)
        setOnClickListener { listener(position) }
        binding.executePendingBindings()
    }

    class CountryViewData(private val overallCountry: OverallCountry) {
        val title: String = overallCountry.countryName
        val totalInfected: String = overallCountry.totalInfected.toString()
        val totalDeath: String = overallCountry.totalDeath.toString()
        val totalRecovered: String = overallCountry.totalRecovered.toString()
    }
}