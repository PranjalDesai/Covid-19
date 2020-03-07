package com.pranjaldesai.coronavirustracker.data.models

data class Death(
    val infectedLocations: List<InfectedLocation>?,
    val overallTotalCount: Int?
) {
    constructor() : this(null, null)
}