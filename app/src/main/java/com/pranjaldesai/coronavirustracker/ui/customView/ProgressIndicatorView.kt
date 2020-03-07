package com.pranjaldesai.coronavirustracker.ui.customView

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.ProgressBar
import androidx.annotation.CallSuper
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.ui.shared.CustomView

open class ProgressIndicatorView : LinearLayout,
    CustomView {

    open val progressBarId: Int = R.id.pb_progress

    override val layoutResourceId: Int = R.layout.view_progress_indicator

    open val attachToRoot: Boolean = true

    lateinit var progressBar: ProgressBar
        private set

    constructor(context: Context?) : this(context, null)

    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        attachLayout()
    }

    final override fun attachLayout() {
        LayoutInflater.from(context).inflate(layoutResourceId, this, attachToRoot)
        gatherControls()
        bindData()
    }

    @CallSuper
    override fun gatherControls() {
        progressBar = findViewById(progressBarId)
    }

    override fun bindData() {}
}