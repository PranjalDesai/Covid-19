package com.pranjaldesai.coronavirustracker.data.models

import com.google.firebase.database.BuildConfig
import com.google.firebase.database.IgnoreExtraProperties

@IgnoreExtraProperties
data class Update(
    val updateAvailable: String = BuildConfig.VERSION_NAME
) {
    constructor() : this(BuildConfig.VERSION_NAME)
}