package com.pranjaldesai.coronavirustracker.data.viewholder

import android.graphics.Color
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.databinding.ViewCityListItemBinding
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.pranjaldesai.coronavirustracker.helper.DATE_FORMAT
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING
import com.pranjaldesai.coronavirustracker.helper.EMPTY_TEXT
import java.text.NumberFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter

class CityItemViewHolder(
    val binding: ViewCityListItemBinding,
    private val listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(city: OverallCity, position: Int, isDarkMode: Boolean) = with(itemView) {
        binding.data = CityViewData(city)
        setOnClickListener { listener(position) }
        val sortedKeys = generateSortedKeys(city)
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
        generateStackedLineChart(city, sortedKeys, isDarkMode)
        binding.executePendingBindings()
    }

    private fun generateSortedKeys(city: OverallCity): List<String> {
        val filteredHistory = city.infectedHistory?.filterValues { it != 0 }
        val dateTimeFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern(DATE_FORMAT)
        return try {
            filteredHistory?.keys?.toList()?.sortedBy {
                LocalDate.parse(it, dateTimeFormatter)
            } ?: ArrayList()
        } catch (exception: Exception) {
            loge(exception)
            ArrayList<String>()
        }
    }

    private fun generateStackedLineChart(
        city: OverallCity,
        sortedKeys: List<String>,
        isDarkMode: Boolean
    ) {
        val textColor = generateChartTextColor(isDarkMode)

        val xAxis = ArrayList<String>(sortedKeys)
        val yAxisInfectedHistory = ArrayList<Entry>()
        val yAxisDeathHistory = ArrayList<Entry>()
        val yAxisRecoveredHistory = ArrayList<Entry>()
        sortedKeys.forEachIndexed { position, specificDate ->
            val infectedOnDate = city.infectedHistory?.get(specificDate)?.toFloat() ?: DEFAULT_FLOAT
            val recoveredOnDate =
                city.recoveredHistory?.get(specificDate)?.toFloat() ?: DEFAULT_FLOAT
            val deathOnDate = city.deathHistory?.get(specificDate)?.toFloat() ?: DEFAULT_FLOAT
            yAxisInfectedHistory.add(Entry(position.toFloat(), infectedOnDate))
            yAxisDeathHistory.add(Entry(position.toFloat(), deathOnDate))
            yAxisRecoveredHistory.add(Entry(position.toFloat(), recoveredOnDate))
        }
        with(binding.stackedLineChart) {
            data = generateLineData(
                yAxisInfectedHistory,
                yAxisDeathHistory,
                yAxisRecoveredHistory,
                textColor
            )
            description.isEnabled = false
            this.xAxis.valueFormatter = IndexAxisValueFormatter(xAxis)

            this.xAxis.setCenterAxisLabels(true)
            this.xAxis.textColor = textColor
            this.axisLeft.textColor = textColor
            this.legend.textColor = textColor
            setBorderColor(textColor)
            data.isHighlightEnabled = false
            axisRight.isEnabled = false
            setPinchZoom(false)
            invalidate()
        }
    }

    private fun generateLineData(
        yAxisInfectedHistory: ArrayList<Entry>,
        yAxisDeathHistory: ArrayList<Entry>,
        yAxisRecoveredHistory: ArrayList<Entry>,
        textColor: Int
    ): LineData {
        val lineDataSetInfected =
            generateLineDataSet(yAxisInfectedHistory, textColor, colors[0], INFECTED_LABEL)
        val lineDataSetRecovered =
            generateLineDataSet(yAxisRecoveredHistory, textColor, colors[1], RECOVERED_LABEL)
        val lineDataSetDeath =
            generateLineDataSet(yAxisDeathHistory, textColor, colors[2], DEATH_LABEL)
        return LineData(lineDataSetInfected, lineDataSetRecovered, lineDataSetDeath)
    }

    private fun generateLineDataSet(
        yAxisHistory: ArrayList<Entry>,
        textColor: Int,
        color: Int,
        label: String
    ): LineDataSet {
        val lineDataSet = LineDataSet(yAxisHistory, EMPTY_STRING)
        lineDataSet.setDrawValues(false)
        lineDataSet.setColors(color)
        lineDataSet.label = label
        lineDataSet.valueTextColor = textColor
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.setDrawCircles(false)
        lineDataSet.lineWidth = LINE_WIDTH
        return lineDataSet
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
        val totalInfected: String =
            "$INFECTED_LABEL: ${numberFormat.format(overallCity.totalInfectedCount)}"
        val totalDeath: String = "$DEATH_LABEL: ${numberFormat.format(overallCity.totalDeathCount)}"
        val totalRecovered: String =
            "$RECOVERED_LABEL: ${numberFormat.format(overallCity.totalRecoveredCount)}"

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
        const val DEFAULT_FLOAT = 0f
        const val LINE_WIDTH = 3f
        val colors = listOf(
            Color.parseColor("#FFC154"),
            Color.parseColor("#47B39C"),
            Color.parseColor("#EC6B56")
        )
        val numberFormat: NumberFormat = NumberFormat.getInstance()
    }
}