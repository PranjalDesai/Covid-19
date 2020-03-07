package com.pranjaldesai.coronavirustracker.extension

import android.view.View
import android.view.View.*
import androidx.databinding.BindingAdapter
import com.google.android.material.textfield.TextInputLayout

fun TextInputLayout.toggleErrorState(errorEnabled: Boolean, errorMessage: String) {
    isErrorEnabled = errorEnabled
    error = errorMessage
}

@BindingAdapter("hideIfTrue")
fun View.hideIfTrue(hide: Boolean) = if (hide) {
    this.visibility = GONE
} else {
    this.visibility = VISIBLE
}

@BindingAdapter("hideIfFalse")
fun View.hideIfFalse(show: Boolean) = if (show) {
    this.visibility = VISIBLE
} else {
    this.visibility = GONE
}

@BindingAdapter("invisibleIfFalse")
fun View.invisibleIfFalse(show: Boolean) = if (show) {
    this.visibility = VISIBLE
} else {
    this.visibility = INVISIBLE
}


@BindingAdapter("invisibleIfTrue")
fun View.invisibleIfTrue(hide: Boolean) = if (hide) {
    this.visibility = VISIBLE
} else {
    this.visibility = INVISIBLE
}