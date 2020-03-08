package com.pranjaldesai.coronavirustracker.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class InfectedLocation(
    val coordinates: Coordinates?,
    val infectedCountry: String?,
    val infectedHistory: Map<String, String>?,
    val infectedProvince: String?,
    val totalCount: Int = 0
) {
    constructor() : this(null, null, null, null, 0)

}