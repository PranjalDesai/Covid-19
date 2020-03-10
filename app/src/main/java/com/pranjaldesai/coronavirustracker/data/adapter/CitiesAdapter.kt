package com.pranjaldesai.coronavirustracker.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.data.viewholder.CityItemViewHolder
import com.pranjaldesai.coronavirustracker.databinding.ViewCityListItemBinding
import com.pranjaldesai.coronavirustracker.extension.launchOnMain
import kotlinx.coroutines.GlobalScope
import java.util.*
import kotlin.collections.ArrayList

class CitiesAdapter(
    data: ArrayList<OverallCity>,
    private var isDarkMode: Boolean,
    private val clickListener: (Int) -> Unit
) : CoreDataAdapter<OverallCity, CityItemViewHolder>(data) {

    private var workingData: ArrayList<OverallCity> = ArrayList(data)

    private val alphabeticalAZComparator: Comparator<OverallCity>
        get() = compareBy<OverallCity> { it.infectedProvince?.toLowerCase(Locale.getDefault()) }
            .thenBy { it.totalInfectedCount }

    init {
        GlobalScope.launchOnMain { sort() }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CityItemViewHolder {
        val cityView =
            ViewCityListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CityItemViewHolder(cityView, clickListener)
    }

    override fun onBindViewHolder(holder: CityItemViewHolder, position: Int) =
        holder.bind(data[position], position, isDarkMode)

    override fun updateData(updatedData: List<OverallCity>) {
        workingData.clear()
        workingData.addAll(updatedData)
        super.updateData(updatedData)
        GlobalScope.launchOnMain {
            sort()
        }
    }

    fun updateItem(position: Int, updatedItem: OverallCity) {
        workingData[position] = updatedItem
        data[position] = updatedItem
        notifyItemChanged(position)
    }

    fun updateTheme(isDarkMode: Boolean) {
        this.isDarkMode = isDarkMode
    }

    private fun sort() {
        val sortedData = workingData.sortedWith(alphabeticalAZComparator).toList()
        with(data) {
            clear()
            addAll(sortedData)
        }
        notifyDataSetChanged()
    }
}