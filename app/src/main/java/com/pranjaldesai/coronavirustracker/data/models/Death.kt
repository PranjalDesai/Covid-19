package com.pranjaldesai.coronavirustracker.data.models

data class Death(
    val infectedLocations: List<InfectedLocation>?,
    val overallTotalCount: Int = 0
) {
    constructor() : this(null, 0)
}