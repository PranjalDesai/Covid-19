package com.pranjaldesai.coronavirustracker.ui

import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.pranjaldesai.coronavirustracker.helper.DATE_FORMAT
import com.pranjaldesai.coronavirustracker.ui.shared.CoreViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

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
            val infectedLocationCount = generateTotalCount(location.infectedHistory)
            val deathLocationCount = generateTotalCount(deathLocation?.infectedHistory)
            val recoveredLocationCount = generateTotalCount(recoveredLocation?.infectedHistory)
            if (infectedLocationCount != DEFAULT_COUNT || deathLocationCount != DEFAULT_COUNT || recoveredLocationCount != DEFAULT_COUNT) {
                cityList.add(
                    OverallCity(
                        generateInfectedHistory(location.infectedHistory),
                        generateInfectedHistory(deathLocation?.infectedHistory),
                        generateInfectedHistory(recoveredLocation?.infectedHistory),
                        location.infectedProvince,
                        infectedLocationCount,
                        deathLocationCount,
                        recoveredLocationCount,
                        countryName
                    )
                )
            }
        }

        if (cityList.size == DEFAULT_CITY_SIZE) {
            cityList[DEFAULT_INDEX].isExpanded = true
        }

        return cityList
    }

    private fun generateFilteredSortedKeys(history: Map<String, Int>?): List<String> {
        val filteredHistory = history?.filterValues {
            it != DEFAULT_COUNT
        }
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return try {
            filteredHistory?.keys?.toList()?.sortedByDescending {
                LocalDate.parse(it, dateTimeFormatter)
            } ?: ArrayList()
        } catch (exception: Exception) {
            loge(exception)
            ArrayList<String>()
        }
    }

    private fun generateInfectedHistory(history: Map<String, Int>?): Map<String, Int> {
        val keys = generateFilteredSortedKeys(history)
        return history?.filterKeys { keys.contains(it) } ?: mapOf()
    }

    private fun generateTotalCount(history: Map<String, Int>?): Int {
        val keys = generateFilteredSortedKeys(history)
        return if (keys.isNotEmpty()) {
            history?.get(keys[DEFAULT_INDEX]) ?: DEFAULT_COUNT
        } else {
            DEFAULT_COUNT
        }
    }

    companion object {
        const val DEFAULT_COUNT = 0
        const val DEFAULT_INDEX = 0
        const val DEFAULT_CITY_SIZE = 1
    }

}