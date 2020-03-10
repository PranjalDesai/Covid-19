package com.pranjaldesai.coronavirustracker.ui

import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.ui.shared.CoreViewModel

class CountryDetailViewModel : CoreViewModel<ICovidView>() {
    override lateinit var subscribedView: ICovidView

    var overallCountry: OverallCountry? = null

    fun generateCities(countryName: String): ArrayList<OverallCity> {
        val cityList = ArrayList<OverallCity>()
        overallCountry?.infectedLocations?.forEach { location ->
            val deathLocation =
                overallCountry?.deathLocations?.find { it.infectedProvince == location.infectedProvince }
            val recoveredLocation =
                overallCountry?.recoveredLocations?.find { it.infectedProvince == location.infectedProvince }
            val deathLocationCount = deathLocation?.totalCount ?: DEFAULT_COUNT
            val recoveredLocationCount = recoveredLocation?.totalCount ?: DEFAULT_COUNT
            cityList.add(
                OverallCity(
                    location.infectedHistory,
                    deathLocation?.infectedHistory,
                    recoveredLocation?.infectedHistory,
                    location.infectedProvince,
                    location.totalCount,
                    deathLocationCount,
                    recoveredLocationCount,
                    countryName
                )
            )
        }

        return cityList
    }

    companion object {
        const val DEFAULT_COUNT = 0
    }

}