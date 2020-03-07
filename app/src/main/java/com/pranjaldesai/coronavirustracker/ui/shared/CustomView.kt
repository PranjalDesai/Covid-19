package com.pranjaldesai.coronavirustracker.ui.shared

interface CustomView {
    val layoutResourceId: Int

    fun attachLayout()
    fun gatherControls()
    fun bindData()
}