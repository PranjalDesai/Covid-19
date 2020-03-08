package com.pranjaldesai.coronavirustracker.data.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OverallCountry(
    val countryName: String,
    val totalInfected: Int,
    val totalDeath: Int,
    val totalRecovered: Int,
    val infectedLocations: List<InfectedLocation>?,
    val deathLocations: List<InfectedLocation>?,
    val recoveredLocations: List<InfectedLocation>?
) : Parcelable