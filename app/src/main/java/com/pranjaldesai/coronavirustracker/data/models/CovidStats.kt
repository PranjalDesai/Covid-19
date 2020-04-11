package com.pranjaldesai.coronavirustracker.data.models

//@IgnoreExtraProperties
data class CovidStats(
    val confirmed: Confirmed?,
    val death: Death?,
    val recovered: Recovered?,
    val update: Update?
) {
    constructor() : this(null, null, null, null)
}