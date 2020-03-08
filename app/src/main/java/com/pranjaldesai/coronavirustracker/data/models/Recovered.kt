package com.pranjaldesai.coronavirustracker.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Recovered(
    val infectedLocations: List<InfectedLocation>?,
    val overallTotalCount: Int = 0
) {
    constructor() : this(null, 0)
}