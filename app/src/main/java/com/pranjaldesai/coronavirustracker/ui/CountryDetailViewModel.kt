package com.pranjaldesai.coronavirustracker.ui

import android.graphics.Color
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineDataSet
import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.pranjaldesai.coronavirustracker.helper.DATE_FORMAT
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING
import com.pranjaldesai.coronavirustracker.ui.shared.CoreViewModel
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CountryDetailViewModel : CoreViewModel<ICovidView>() {
    override lateinit var subscribedView: ICovidView

    var overallCountry: OverallCountry? = null
    var isDarkMode = false
    private val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
    private val mostInfectedComparator =
        compareBy<String> { LocalDate.parse(it, dateTimeFormatter) }

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

    fun generateDailyInfected(): Map<String, Int> {
        val infectedHistoryDates = generateAllInfectedDates()
        val sortedInfectedHistoryDates = generateSortedHistoryKeys(infectedHistoryDates)
        val valueMap = defaultEmptyHashMap(sortedInfectedHistoryDates)
        overallCountry?.infectedLocations?.forEach {
            sortedInfectedHistoryDates.forEachIndexed { index, currentDate ->
                val nextIndexDate = index + DEFAULT_NEXT_INDEX
                if (nextIndexDate < sortedInfectedHistoryDates.size) {
                    val todayInfection = it.infectedHistory?.get(currentDate) ?: DEFAULT_COUNT
                    val yesterdayInfection =
                        it.infectedHistory?.get(sortedInfectedHistoryDates[nextIndexDate])
                            ?: DEFAULT_COUNT
                    val newlyAdded = todayInfection - yesterdayInfection
                    if (newlyAdded > DEFAULT_COUNT) {
                        val currentValue = valueMap[currentDate] ?: DEFAULT_COUNT
                        valueMap[currentDate] = currentValue + newlyAdded
                    }

                }
            }
        }
        return valueMap.toSortedMap(mostInfectedComparator)
    }

    fun populateDailyHistoryYAxisData(dailyInfectedMap: Map<String, Int>): ArrayList<Entry> {
        val yAxisInfectedHistory = ArrayList<Entry>()
        dailyInfectedMap.keys.forEachIndexed { position, key ->
            yAxisInfectedHistory.add(
                Entry(
                    position.toFloat(),
                    dailyInfectedMap[key]?.toFloat() ?: CountryDetailFragment.DEFAULT_FLOAT
                )
            )
        }
        return yAxisInfectedHistory
    }

    fun generateLineDataSet(yAxisInfectedHistory: ArrayList<Entry>, textColor: Int): LineDataSet {
        val lineDataSet = LineDataSet(yAxisInfectedHistory, EMPTY_STRING)
        lineDataSet.setDrawValues(false)
        lineDataSet.setColors(CountryDetailFragment.color)
        lineDataSet.valueTextColor = textColor
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.setDrawCircles(false)
        lineDataSet.setDrawFilled(true)
        lineDataSet.lineWidth = CountryDetailFragment.LINE_WIDTH
        return lineDataSet
    }

    fun generateChartTextColor(): Int {
        return if (isDarkMode) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    }

    private fun generateFilteredSortedKeys(history: Map<String, Int>?): List<String> {
        val filteredHistory = history?.filterValues {
            it != DEFAULT_COUNT
        }
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

    private fun defaultEmptyHashMap(sortedInfectedHistoryDates: List<String>): HashMap<String, Int> {
        val valueMap = HashMap<String, Int>()
        sortedInfectedHistoryDates.forEach {
            valueMap[it] = DEFAULT_COUNT
        }
        return valueMap
    }

    private fun generateAllInfectedDates(): ArrayList<String> {
        val infectedHistoryDates = ArrayList<String>()
        overallCountry?.infectedLocations?.forEach {
            infectedHistoryDates.clear()
            infectedHistoryDates.addAll(
                infectedHistoryDates.union(
                    it.infectedHistory?.keys?.toList() ?: ArrayList()
                )
            )
        }
        return infectedHistoryDates
    }

    private fun generateSortedHistoryKeys(keys: ArrayList<String>): List<String> {
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return keys.sortedByDescending {
            LocalDate.parse(it, dateTimeFormatter)
        }
    }

    companion object {
        const val DEFAULT_COUNT = 0
        const val DEFAULT_INDEX = 0
        const val DEFAULT_CITY_SIZE = 1
        const val DEFAULT_NEXT_INDEX = 1
    }

}