package com.pranjaldesai.coronavirustracker.data.models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class InfectedLocation(
    val coordinates: Coordinates?,
    val infectedCountry: String?,
    val infectedHistory: Map<String, String>?,
    val infectedProvince: String?,
    val totalCount: Int = 0
) : Parcelable {
    constructor() : this(null, null, null, null, 0)

}