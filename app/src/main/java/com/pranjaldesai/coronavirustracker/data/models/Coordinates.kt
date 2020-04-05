package com.pranjaldesai.coronavirustracker.data.models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Coordinates(
    val lat: Double?,
    val long: Double?
) : Parcelable {
    constructor() : this(null, null)
}