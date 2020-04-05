package com.pranjaldesai.coronavirustracker.ui

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.heatmaps.WeightedLatLng
import com.pranjaldesai.coronavirustracker.data.models.InfectedLocation

class CovidMapViewModel : CoreCovidViewModel<ICovidView>() {
    override lateinit var subscribedView: ICovidView

    fun generateCoordinatesList(): ArrayList<WeightedLatLng> {
        val coordinatesList = ArrayList<WeightedLatLng>()
        covidStats?.confirmed?.infectedLocations?.forEach {
            val lat = it.coordinates?.lat
            val long = it.coordinates?.long
            val intensity = it.totalCount.toDouble()
            if (lat != null && long != null) {
                coordinatesList.add(WeightedLatLng(LatLng(lat, long), intensity))
            }
        }
        return coordinatesList
    }

    fun generateCoordinates(infectedLocation: InfectedLocation): LatLng? {
        val lat = infectedLocation.coordinates?.lat
        val long = infectedLocation.coordinates?.long
        return if (lat != null && long != null) {
            LatLng(lat, long)
        } else {
            null
        }
    }

    fun generateSnippet(infectedLocation: InfectedLocation): String? {
        return if (infectedLocation.totalCount != 0) {
            "${CovidMapFragment.DEFAULT_MARKER_SNIPPET} ${infectedLocation.totalCount}"
        } else {
            null
        }
    }

    fun shouldShowMarkers(currentZoomLevel: Float, zoomLevel: Float?): Boolean? {
        return if (zoomLevel != null && zoomLevel > 4.2 && currentZoomLevel != zoomLevel) {
            true
        } else if (zoomLevel != null && zoomLevel <= 4.2 && currentZoomLevel != zoomLevel) {
            false
        } else {
            null
        }
    }
}