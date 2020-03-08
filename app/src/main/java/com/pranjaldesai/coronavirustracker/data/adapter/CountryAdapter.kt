package com.pranjaldesai.coronavirustracker.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.data.viewholder.CountryItemViewHolder
import com.pranjaldesai.coronavirustracker.databinding.ViewCountryListItemBinding
import com.pranjaldesai.coronavirustracker.extension.launchOnMain
import kotlinx.coroutines.GlobalScope

class CountryAdapter(
    data: ArrayList<OverallCountry>,
    private val clickListener: (Int) -> Unit
) : CoreDataAdapter<OverallCountry, CountryItemViewHolder>(data) {

    private var workingData: ArrayList<OverallCountry> = ArrayList(data)

    init {
        GlobalScope.launchOnMain {
            with(data) {
                clear()
                addAll(workingData)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CountryItemViewHolder {
        val countryView =
            ViewCountryListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CountryItemViewHolder(countryView, clickListener)
    }

    override fun onBindViewHolder(holder: CountryItemViewHolder, position: Int) =
        holder.bind(data[position], position)

    override fun updateData(updatedData: List<OverallCountry>) {
        workingData.clear()
        workingData.addAll(updatedData)
        super.updateData(updatedData)
        with(data) {
            clear()
            addAll(workingData)
        }
        notifyDataSetChanged()
    }
}