package com.pranjaldesai.coronavirustracker.ui

import com.google.android.gms.maps.model.LatLng
import com.pranjaldesai.coronavirustracker.data.models.InfectedLocation

class CovidMapViewModel : CoreCovidViewModel<ICovidView>() {
    override lateinit var subscribedView: ICovidView

    fun generateCoordinatesList(): ArrayList<LatLng> {
        val coordinatesList = ArrayList<LatLng>()
        covidStats?.confirmed?.infectedLocations?.forEach {
            val lat = it.coordinates?.lat?.toDouble()
            val long = it.coordinates?.long?.toDouble()
            if (lat != null && long != null) {
                coordinatesList.add(LatLng(lat, long))
            }
        }
        return coordinatesList
    }

    fun generateCoordinates(infectedLocation: InfectedLocation): LatLng? {
        val lat = infectedLocation.coordinates?.lat?.toDouble()
        val long = infectedLocation.coordinates?.long?.toDouble()
        return if (lat != null && long != null) {
            LatLng(lat, long)
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