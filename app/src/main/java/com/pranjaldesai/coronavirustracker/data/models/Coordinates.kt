package com.pranjaldesai.coronavirustracker.data.models

import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Coordinates(
    val lat: String?,
    val long: String?
) {
    constructor() : this(null, null)
}