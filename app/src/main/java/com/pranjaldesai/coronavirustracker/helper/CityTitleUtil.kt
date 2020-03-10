package com.pranjaldesai.coronavirustracker.helper

import com.pranjaldesai.coronavirustracker.data.models.InfectedLocation

fun generateCityTitle(infectedLocation: InfectedLocation): String {
    return if (infectedLocation.infectedProvince != null && infectedLocation.infectedProvince != EMPTY_TEXT) {
        infectedLocation.infectedProvince
    } else {
        infectedLocation.infectedCountry ?: EMPTY_STRING
    }
}