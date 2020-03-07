package com.pranjaldesai.coronavirustracker.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class CovidStats(
    val confirmed: Confirmed?,
    val death: Death?,
    val recovered: Recovered?
) {
    constructor() : this(null, null, null)
}