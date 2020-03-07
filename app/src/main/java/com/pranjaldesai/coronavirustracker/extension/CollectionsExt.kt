package com.pranjaldesai.coronavirustracker.extension

fun <T> Collection<T>?.isNotNullOrEmpty() = isNullOrEmpty().not()