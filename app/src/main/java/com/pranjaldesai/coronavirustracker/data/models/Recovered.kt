package com.pranjaldesai.coronavirustracker.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Recovered(
    val infectedLocations: List<InfectedLocation>?,
    val overallTotalCount: Int?
) {
    constructor() : this(null, null)
}