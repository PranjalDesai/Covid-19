package com.pranjaldesai.coronavirustracker.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranjaldesai.coronavirustracker.data.ListSortStyle
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.data.viewholder.CountryItemViewHolder
import com.pranjaldesai.coronavirustracker.databinding.ViewCountryListItemBinding
import com.pranjaldesai.coronavirustracker.extension.launchOnMain
import kotlinx.coroutines.GlobalScope
import java.util.*
import kotlin.collections.ArrayList

class CountryAdapter(
    data: ArrayList<OverallCountry>, sortStyle: ListSortStyle,
    private val clickListener: (Int) -> Unit
) : CoreDataAdapter<OverallCountry, CountryItemViewHolder>(data) {

    private var workingData: ArrayList<OverallCountry> = ArrayList(data)
    private var currentSortStyle: ListSortStyle = sortStyle

    private val mostInfectedComparator: Comparator<OverallCountry>
        get() = compareByDescending<OverallCountry> { it.totalInfected }
            .thenBy { it.countryName.toLowerCase(Locale.getDefault()) }

    private val leastInfectedComparator: Comparator<OverallCountry>
        get() = compareBy<OverallCountry> { it.totalInfected }
            .thenBy { it.countryName.toLowerCase(Locale.getDefault()) }

    private val mostDeathComparator: Comparator<OverallCountry>
        get() = compareByDescending<OverallCountry> { it.totalDeath }
            .thenBy { it.countryName.toLowerCase(Locale.getDefault()) }

    private val leastDeathComparator: Comparator<OverallCountry>
        get() = compareBy<OverallCountry> { it.totalDeath }
            .thenBy { it.countryName.toLowerCase(Locale.getDefault()) }

    private val alphabeticalAZComparator: Comparator<OverallCountry>
        get() = compareBy<OverallCountry> { it.countryName.toLowerCase(Locale.getDefault()) }
            .thenBy { it.infectedLocations?.size }

    init {
        GlobalScope.launchOnMain {
            sort(currentSortStyle)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        val countriesView =
            ViewCountryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryItemViewHolder(countriesView, clickListener)
    }

    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) =
        holder.bind(data[position], position)

    override fun updateData(updatedData: List<OverallCountry>) {
        workingData.clear()
        workingData.addAll(updatedData)
        super.updateData(updatedData)
        GlobalScope.launchOnMain {
            sort(currentSortStyle)
        }
    }

    fun sort(sortStyle: ListSortStyle) {
        currentSortStyle = sortStyle
        val comparator = selectComparatorBasedOnSavedPreference()
        val sortedData = workingData.sortedWith(comparator).toList()
        with(data) {
            clear()
            addAll(sortedData)
        }
        notifyDataSetChanged()
    }

    private fun selectComparatorBasedOnSavedPreference(): Comparator<OverallCountry> {
        return when (currentSortStyle) {
            ListSortStyle.INFECTED_LS -> mostInfectedComparator
            ListSortStyle.INFECTED_SL -> leastInfectedComparator
            ListSortStyle.DEATH_LS -> mostDeathComparator
            ListSortStyle.DEATH_SL -> leastDeathComparator
            ListSortStyle.ALPHABETICAL_AZ -> alphabeticalAZComparator
        }
    }
}