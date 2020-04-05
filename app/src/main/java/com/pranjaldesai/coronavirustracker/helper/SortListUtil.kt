package com.pranjaldesai.coronavirustracker.helper

import com.pranjaldesai.coronavirustracker.data.ListSortStyle
import com.pranjaldesai.coronavirustracker.data.models.SortItem

const val ALPHABETICAL_AZ = "Alphabetical (A-Z)"
const val INFECTED_MOST = "Most Infected"
const val INFECTED_LEAST = "Least Infected"
const val RECOVERED_MOST = "Most Recovered"
const val RECOVERED_LEAST = "Least Recovered"
const val DEATH_MOST = "Most Deaths"
const val DEATH_LEAST = "Least Deaths"

fun generateCountrySortList(): ArrayList<SortItem> = ArrayList<SortItem>().apply {
    add(SortItem(ALPHABETICAL_AZ, ListSortStyle.ALPHABETICAL_AZ))
    add(SortItem(INFECTED_MOST, ListSortStyle.INFECTED_LS, true))
    add(SortItem(INFECTED_LEAST, ListSortStyle.INFECTED_SL))
    add(SortItem(RECOVERED_MOST, ListSortStyle.RECOVERED_LS))
    add(SortItem(RECOVERED_LEAST, ListSortStyle.RECOVERED_SL))
    add(SortItem(DEATH_MOST, ListSortStyle.DEATH_LS))
    add(SortItem(DEATH_LEAST, ListSortStyle.DEATH_SL))
}
