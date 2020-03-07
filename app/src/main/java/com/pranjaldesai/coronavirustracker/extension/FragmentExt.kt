package com.pranjaldesai.coronavirustracker.extension

import androidx.annotation.IdRes
import androidx.fragment.app.Fragment
import androidx.navigation.NavDirections
import androidx.navigation.fragment.findNavController

fun Fragment.navigate(@IdRes destinationId: Int) {
    findNavController().navigate(destinationId)
}

fun Fragment.navigate(navDirections: NavDirections) {
    findNavController().navigate(navDirections)
}