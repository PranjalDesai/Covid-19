package com.pranjaldesai.coronavirustracker.ui

import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.heatmaps.WeightedLatLng
import com.pranjaldesai.coronavirustracker.data.models.InfectedLocation
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING

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

    fun generateTag(infectedLocation: InfectedLocation): String? {
        return infectedLocation.infectedCountry ?: EMPTY_STRING
    }

    fun generateOverallCountry(countryName: String): OverallCountry? {
        covidStats?.let { stats ->
            val groupedConfirmed =
                stats.confirmed?.infectedLocations?.filter { it.infectedCountry == countryName }
            val groupedDeath =
                stats.death?.infectedLocations?.filter { it.infectedCountry == countryName }
            val groupedRecovered =
                stats.recovered?.infectedLocations?.filter { it.infectedCountry == countryName }
            var totalInfected = 0
            var totalRecovered = 0
            var totalDeath = 0
            groupedConfirmed?.forEach { location -> totalInfected += location.totalCount }
            groupedDeath?.forEach { location -> totalDeath += location.totalCount }
            groupedRecovered?.forEach { location -> totalRecovered += location.totalCount }
            return OverallCountry(
                countryName,
                totalInfected,
                totalDeath,
                totalRecovered,
                groupedConfirmed,
                groupedDeath,
                groupedRecovered
            )
        }
        return null
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