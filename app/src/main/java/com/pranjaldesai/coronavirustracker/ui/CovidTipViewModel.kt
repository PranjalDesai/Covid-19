package com.pranjaldesai.coronavirustracker.ui

import com.pranjaldesai.coronavirustracker.ui.shared.CoreViewModel

class CovidTipViewModel : CoreViewModel<ICovidView>() {
    override lateinit var subscribedView: ICovidView
    var preventionTipArray: List<String>? = null
    var misconceptionTipArray: List<String>? = null
    var travelTipArray: List<String>? = null

    fun generateCategoryTitle(tabNumber: Int): String {
        return when (tabNumber) {
            PREVENTION_TAB -> "$TITLE_BUILDER_START $PREVENTION $TITLE_BUILDER_END"
            MISCONCEPTION_TAB -> "$TITLE_BUILDER_START $MISCONCEPTION $TITLE_BUILDER_END"
            TRAVEL_TAB -> "$TITLE_BUILDER_START $TRAVEL $TITLE_BUILDER_END"
            else -> "$TITLE_BUILDER_START $TITLE_BUILDER_END"
        }
    }

    fun updateTips(tabNumber: Int): List<String>? {
        return when (tabNumber) {
            PREVENTION_TAB -> preventionTipArray
            MISCONCEPTION_TAB -> misconceptionTipArray
            TRAVEL_TAB -> travelTipArray
            else -> null
        }
    }

    companion object {
        const val PREVENTION_TAB = 0
        const val MISCONCEPTION_TAB = 1
        const val TRAVEL_TAB = 2
        const val PREVENTION = "Prevention"
        const val MISCONCEPTION = "Misconception"
        const val TRAVEL = "Travel"
        const val TITLE_BUILDER_START = "Coronavirus"
        const val TITLE_BUILDER_END = "Tip"
    }

}