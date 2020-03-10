package com.pranjaldesai.coronavirustracker.ui

import android.graphics.Color
import com.pranjaldesai.coronavirustracker.data.models.CovidStats
import com.pranjaldesai.coronavirustracker.ui.shared.CoreViewModel

abstract class CoreCovidViewModel<ViewTypeT : ICovidView> : CoreViewModel<ViewTypeT>() {
    override lateinit var subscribedView: ViewTypeT

    var covidStats: CovidStats? = null
    var isDarkMode = false

    fun generateChartTextColor(): Int {
        return if (isDarkMode) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    }

}