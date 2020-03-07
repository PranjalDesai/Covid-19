package com.pranjaldesai.coronavirustracker.extension


fun String?.isNotNullOrEmpty() = this.isNullOrEmpty().not()
fun String?.isNotNullOrBlank() = this.isNullOrBlank().not()