package com.pranjaldesai.coronavirustracker.data.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.databinding.ViewCountryListItemBinding
import java.text.NumberFormat

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
        val totalInfected: String =
            "$INFECTED_LABEL ${numberFormat.format(overallCountry.totalInfected)}"
        val totalDeath: String = "$DEATH_LABEL ${numberFormat.format(overallCountry.totalDeath)}"
        val totalRecovered: String =
            "$RECOVERED_LABEL ${numberFormat.format(overallCountry.totalRecovered)}"
    }

    companion object {
        const val INFECTED_LABEL = "Infected:"
        const val DEATH_LABEL = "Death:"
        const val RECOVERED_LABEL = "Recovered:"
        val numberFormat: NumberFormat = NumberFormat.getInstance()
    }
}