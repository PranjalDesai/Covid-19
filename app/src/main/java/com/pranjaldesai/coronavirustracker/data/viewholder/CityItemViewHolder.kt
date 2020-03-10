package com.pranjaldesai.coronavirustracker.data.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.databinding.ViewCityListItemBinding

class CityItemViewHolder(
    val binding: ViewCityListItemBinding,
    private val listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(city: OverallCity, position: Int, isDarkMode: Boolean) = with(itemView) {
        binding.data = CityViewData(city)
        setOnClickListener { listener(position) }
        binding.expandedView.visibility = if (city.isExpanded) {
            View.VISIBLE
        } else {
            View.GONE
        }

        generateStackedBarChart(city, isDarkMode)
        binding.executePendingBindings()
    }

    private fun generateStackedBarChart(city: OverallCity, isDarkMode: Boolean) {
        val textColor = generateChartTextColor(isDarkMode)
        val sortedKeys =
            city.infectedHistory?.keys?.toList()?.sortedByDescending { it } ?: ArrayList()
        val xAxis = ArrayList<String>(sortedKeys)
        val yAxisInfectedHistory = ArrayList<BarEntry>()
        val yAxisDeathHistory = ArrayList<BarEntry>()
        val yAxisRecoveredHistory = ArrayList<BarEntry>()
        sortedKeys.forEachIndexed { position, specificDate ->
            val infectedOnDate = city.infectedHistory?.get(specificDate)?.toFloat() ?: 0f
            val recoveredOnDate = city.recoveredHistory?.get(specificDate)?.toFloat() ?: 0f
            val deathOnDate = city.deathHistory?.get(specificDate)?.toFloat() ?: 0f
            yAxisInfectedHistory.add(BarEntry(position.toFloat(), infectedOnDate))
            yAxisDeathHistory.add(BarEntry(position.toFloat(), recoveredOnDate))
            yAxisRecoveredHistory.add(BarEntry(position.toFloat(), deathOnDate))
        }
        val barDataSetInfected = BarDataSet(yAxisInfectedHistory, "")
        barDataSetInfected.setColors(Color.parseColor("#FFC154"))
        barDataSetInfected.label = "Infected"
        barDataSetInfected.setDrawValues(false)
        barDataSetInfected.valueTextColor = textColor
        val barDataSetDeath = BarDataSet(yAxisDeathHistory, "")
        barDataSetDeath.setColors(Color.parseColor("#EC6B56"))
        barDataSetDeath.label = "Death"
        barDataSetDeath.setDrawValues(false)
        barDataSetDeath.valueTextColor = textColor
        val barDataSetRecovered = BarDataSet(yAxisRecoveredHistory, "")
        barDataSetRecovered.setColors(Color.parseColor("#47B39C"))
        barDataSetRecovered.label = "Recovered"
        barDataSetRecovered.setDrawValues(false)
        barDataSetRecovered.valueTextColor = textColor
        val barData = BarData(barDataSetInfected, barDataSetDeath, barDataSetRecovered)
        with(binding.stackedBarChart) {
            data = barData
            description.isEnabled = false
            this.xAxis.valueFormatter = IndexAxisValueFormatter(xAxis)
            this.xAxis.setCenterAxisLabels(true)
            this.xAxis.textColor = textColor
            this.axisLeft.textColor = textColor
            this.legend.textColor = textColor
            setBorderColor(textColor)
            setVisibleXRangeMaximum(3F)
            barData.barWidth = 0.30f
            groupBars(0f, 0.04f, 0.02f)
            data.isHighlightEnabled = false
            axisRight.isEnabled = false
            setPinchZoom(false)
            setFitBars(true)
            invalidate()
        }
    }

    private fun generateChartTextColor(isDarkMode: Boolean): Int {
        return if (isDarkMode) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    }

    class CityViewData(private val overallCity: OverallCity) {
        val title: String = generateTitle(overallCity.infectedProvince)
        val totalInfected: String = overallCity.totalInfectedCount.toString()
        val totalDeath: String = overallCity.totalDeathCount.toString()
        val totalRecovered: String = overallCity.totalRecoveredCount.toString()

        private fun generateTitle(cityName: String?): String {
            return if (cityName != null && cityName != "empty") {
                cityName
            } else {
                overallCity.countryName
            }
        }
    }
}