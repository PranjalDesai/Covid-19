package com.pranjaldesai.coronavirustracker.data.models

import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.android.parcel.Parcelize

@IgnoreExtraProperties
@Parcelize
data class Coordinates(
    val lat: String?,
    val long: String?
) : Parcelable {
    constructor() : this(null, null)
}