package com.pranjaldesai.coronavirustracker.ui

import com.pranjaldesai.coronavirustracker.data.models.CovidStats
import com.pranjaldesai.coronavirustracker.ui.shared.CoreViewModel

abstract class CoreCovidViewModel<ViewTypeT : ICovidView> : CoreViewModel<ViewTypeT>() {
    override lateinit var subscribedView: ViewTypeT

    var covidStats: CovidStats? = null

}