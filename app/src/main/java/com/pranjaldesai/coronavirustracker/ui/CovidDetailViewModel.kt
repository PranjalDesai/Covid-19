package com.pranjaldesai.coronavirustracker.ui

import android.graphics.Color
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING

class CovidDetailViewModel : CoreCovidViewModel<ICovidView>() {
    override lateinit var subscribedView: ICovidView

    fun generateOverallCountryList(): ArrayList<OverallCountry> {
        val overallCountryList = ArrayList<OverallCountry>()
        covidStats?.let {
            val groupedConfirmed =
                it.confirmed?.infectedLocations?.groupBy { location -> location.infectedCountry }
            val groupedDeath =
                it.death?.infectedLocations?.groupBy { location -> location.infectedCountry }
            val groupedRecovered =
                it.recovered?.infectedLocations?.groupBy { location -> location.infectedCountry }
            groupedConfirmed?.keys?.forEach { key ->
                var totalInfected = 0
                var totalRecovered = 0
                var totalDeath = 0
                groupedConfirmed[key]?.forEach { location -> totalInfected += location.totalCount }
                groupedDeath?.get(key)?.forEach { location -> totalDeath += location.totalCount }
                groupedRecovered?.get(key)
                    ?.forEach { location -> totalRecovered += location.totalCount }

                key?.let { it1 ->
                    overallCountryList.add(
                        OverallCountry(
                            it1,
                            totalInfected,
                            totalDeath,
                            totalRecovered,
                            groupedConfirmed[key],
                            groupedDeath?.get(key),
                            groupedRecovered?.get(key)
                        )
                    )
                }
            }
        }
        return overallCountryList
    }

    fun generatePieData(): PieData {
        val chartData = ArrayList<PieEntry>()
        val confirmedCases = covidStats?.confirmed?.overallTotalCount ?: DEFAULT_COUNT
        val deathCases = covidStats?.death?.overallTotalCount ?: DEFAULT_COUNT
        val recoveredCases = covidStats?.recovered?.overallTotalCount ?: DEFAULT_COUNT
        chartData.add(PieEntry(confirmedCases.toFloat(), INFECTED_LABEL))
        chartData.add(PieEntry(deathCases.toFloat(), DEATH_LABEL))
        chartData.add(PieEntry(recoveredCases.toFloat(), RECOVERED_LABEL))

        val pieDataSet = PieDataSet(chartData, EMPTY_STRING)
        pieDataSet.colors = colors
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = PIE_DATA_TEXT_SIZE
        pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        return PieData(pieDataSet)
    }

    companion object {
        val colors = listOf(Color.YELLOW, Color.RED, Color.GREEN)
        const val DEFAULT_COUNT = 0
        const val INFECTED_LABEL = "Infected"
        const val DEATH_LABEL = "Death"
        const val RECOVERED_LABEL = "Recovered"
        const val PIE_DATA_TEXT_SIZE = 16f
    }
}