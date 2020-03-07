package com.pranjaldesai.coronavirustracker.extension

import android.view.View

fun Boolean.translateBooleanIntoViewVisibility(): Int =
    if (this) {
        View.VISIBLE
    } else {
        View.GONE
    }