package com.pranjaldesai.coronavirustracker.data.models

data class OverallCity(
    val infectedHistory: Map<String, String>?,
    val deathHistory: Map<String, String>?,
    val recoveredHistory: Map<String, String>?,
    val infectedProvince: String?,
    val totalInfectedCount: Int = 0,
    val totalDeathCount: Int = 0,
    val totalRecoveredCount: Int = 0,
    val countryName: String,
    var isExpanded: Boolean = false
)