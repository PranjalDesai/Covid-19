package com.pranjaldesai.coronavirustracker.data.models

data class OverallCity(
    val infectedHistory: Map<String, Int>?,
    val deathHistory: Map<String, Int>?,
    val recoveredHistory: Map<String, Int>?,
    val infectedProvince: String?,
    val totalInfectedCount: Int = 0,
    val totalDeathCount: Int = 0,
    val totalRecoveredCount: Int = 0,
    val countryName: String,
    var isExpanded: Boolean = false
)