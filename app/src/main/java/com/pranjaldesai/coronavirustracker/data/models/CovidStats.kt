package com.pranjaldesai.coronavirustracker.data.models

//@IgnoreExtraProperties
data class CovidStats(
    val confirmed: Confirmed?,
    val death: Death?,
    val recovered: Recovered?,
    var isUpdateAvailable: String?
) {
    constructor() : this(null, null, null, null)
}