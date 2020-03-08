package com.pranjaldesai.coronavirustracker.ui.dialog

import android.content.Context
import com.pranjaldesai.coronavirustracker.data.ListSortStyle
import com.pranjaldesai.coronavirustracker.data.adapter.CountryAdapter
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import java.util.*
import kotlin.collections.ArrayList

class CountrySearchDialog(context: Context) : CoreSearchDialog<OverallCountry>(context) {

    override val searchAdapter =
        CountryAdapter(ArrayList(), ListSortStyle.ALPHABETICAL_AZ, clickListener = clickListener)

    override fun searchPredicateValidator(item: OverallCountry, userInput: String): Boolean {
        val locale = Locale.getDefault()

        val formattedInput = userInput.toLowerCase(locale)

        val itemName = item.countryName.toLowerCase(locale)

        return itemName.contains(formattedInput)
    }

    override fun loadItems(items: ArrayList<OverallCountry>) {
        this.items = items
    }
}