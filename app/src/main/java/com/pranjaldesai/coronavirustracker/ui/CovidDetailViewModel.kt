package com.pranjaldesai.coronavirustracker.ui

import android.content.Intent
import android.graphics.Color
import android.net.Uri
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.pranjaldesai.coronavirustracker.BuildConfig
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

    fun isUpdateAvailable(updateVersion: String): Boolean {
        return BuildConfig.VERSION_NAME != updateVersion
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
        pieDataSet.valueTextColor = generateChartTextColor()
        pieDataSet.valueTextSize = PIE_DATA_TEXT_SIZE
        pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE

        return PieData(pieDataSet)
    }

    fun generateUpdateIntent(): Intent {
        val intent = Intent(Intent.ACTION_VIEW)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        intent.data = Uri.parse(UPDATE_URL)
        return intent
    }

    companion object {
        val colors = listOf(
            Color.parseColor("#FFC154"),
            Color.parseColor("#EC6B56"),
            Color.parseColor("#47B39C")
        )
        const val DEFAULT_COUNT = 0
        const val INFECTED_LABEL = "Infected"
        const val DEATH_LABEL = "Death"
        const val RECOVERED_LABEL = "Recovered"
        const val PIE_DATA_TEXT_SIZE = 16f
        const val UPDATE_URL =
            "https://firebasestorage.googleapis.com/v0/b/covid-19-abb5f.appspot.com/o/coronavirus-tracker.apk?alt=media"
    }
}