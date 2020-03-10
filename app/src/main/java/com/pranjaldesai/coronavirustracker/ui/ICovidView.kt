package com.pranjaldesai.coronavirustracker.ui

import androidx.lifecycle.LifecycleOwner
import com.pranjaldesai.coronavirustracker.ui.shared.ICoreView

interface ICovidView : ICoreView {
    val lifecycleOwner: LifecycleOwner
}