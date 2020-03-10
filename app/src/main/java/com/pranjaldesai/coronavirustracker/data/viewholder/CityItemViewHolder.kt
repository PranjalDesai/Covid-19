package com.pranjaldesai.coronavirustracker.data.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.BarData
import com.github.mikephil.charting.data.BarDataSet
import com.github.mikephil.charting.data.BarEntry
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.databinding.ViewCityListItemBinding
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING
import com.pranjaldesai.coronavirustracker.helper.EMPTY_TEXT

class CityItemViewHolder(
    val binding: ViewCityListItemBinding,
    private val listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(city: OverallCity, position: Int, isDarkMode: Boolean) = with(itemView) {
        binding.data = CityViewData(city)
        setOnClickListener { listener(position) }
        binding.expandedView.visibility = if (city.isExpanded) {
            binding.expandArrow.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_action_arrow_up,
                    context.theme
                )
            )
            View.VISIBLE
        } else {
            binding.expandArrow.setImageDrawable(
                resources.getDrawable(
                    R.drawable.ic_action_arrow_down,
                    context.theme
                )
            )
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
            val infectedOnDate = city.infectedHistory?.get(specificDate)?.toFloat() ?: DEFAULT_FLOAT
            val recoveredOnDate =
                city.recoveredHistory?.get(specificDate)?.toFloat() ?: DEFAULT_FLOAT
            val deathOnDate = city.deathHistory?.get(specificDate)?.toFloat() ?: DEFAULT_FLOAT
            yAxisInfectedHistory.add(BarEntry(position.toFloat(), infectedOnDate))
            yAxisDeathHistory.add(BarEntry(position.toFloat(), deathOnDate))
            yAxisRecoveredHistory.add(BarEntry(position.toFloat(), recoveredOnDate))
        }
        with(binding.stackedBarChart) {
            data = generateBarData(
                yAxisInfectedHistory,
                textColor,
                yAxisDeathHistory,
                yAxisRecoveredHistory
            )
            description.isEnabled = false
            this.xAxis.valueFormatter = IndexAxisValueFormatter(xAxis)
            this.xAxis.setCenterAxisLabels(true)
            this.xAxis.textColor = textColor
            this.axisLeft.textColor = textColor
            this.legend.textColor = textColor
            setBorderColor(textColor)
            setVisibleXRangeMaximum(X_RANGE_MINIMUM)
            barData.barWidth = BAR_WIDTH
            groupBars(DEFAULT_FLOAT, GROUP_SPACE, BAR_SPACE)
            data.isHighlightEnabled = false
            axisRight.isEnabled = false
            setPinchZoom(false)
            setFitBars(true)
            invalidate()
        }
    }

    private fun generateBarData(
        yAxisInfectedHistory: ArrayList<BarEntry>,
        textColor: Int,
        yAxisDeathHistory: ArrayList<BarEntry>,
        yAxisRecoveredHistory: ArrayList<BarEntry>
    ): BarData {
        val barDataSetInfected =
            generateBarDataSet(yAxisInfectedHistory, textColor, colors[0], INFECTED_LABEL)
        val barDataSetRecovered =
            generateBarDataSet(yAxisRecoveredHistory, textColor, colors[1], RECOVERED_LABEL)
        val barDataSetDeath =
            generateBarDataSet(yAxisDeathHistory, textColor, colors[2], DEATH_LABEL)
        return BarData(barDataSetInfected, barDataSetRecovered, barDataSetDeath)
    }

    private fun generateBarDataSet(
        yAxisHistory: ArrayList<BarEntry>,
        textColor: Int,
        color: Int,
        label: String
    ): BarDataSet {
        val barDataSet = BarDataSet(yAxisHistory, EMPTY_STRING)
        barDataSet.setColors(color)
        barDataSet.label = label
        barDataSet.setDrawValues(false)
        barDataSet.valueTextColor = textColor
        return barDataSet
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
        val totalInfected: String = "$INFECTED_LABEL: ${overallCity.totalInfectedCount}"
        val totalDeath: String = "$DEATH_LABEL: ${overallCity.totalDeathCount}"
        val totalRecovered: String = "$RECOVERED_LABEL: ${overallCity.totalRecoveredCount}"

        private fun generateTitle(cityName: String?): String {
            return if (cityName != null && cityName != EMPTY_TEXT) {
                cityName
            } else {
                overallCity.countryName
            }
        }
    }

    companion object {
        const val INFECTED_LABEL = "Infected"
        const val DEATH_LABEL = "Death"
        const val RECOVERED_LABEL = "Recovered"
        const val X_RANGE_MINIMUM = 3f
        const val BAR_WIDTH = 0.30f
        const val DEFAULT_FLOAT = 0f
        const val GROUP_SPACE = 0.4f
        const val BAR_SPACE = 0.02f
        val colors = listOf(
            Color.parseColor("#FFC154"),
            Color.parseColor("#47B39C"),
            Color.parseColor("#EC6B56")
        )
    }
}