package com.pranjaldesai.coronavirustracker.data.models

import com.pranjaldesai.coronavirustracker.data.ListSortStyle

data class SortItem(
    val text: String,
    val sortStyle: ListSortStyle,
    var isSortSelected: Boolean = false
)