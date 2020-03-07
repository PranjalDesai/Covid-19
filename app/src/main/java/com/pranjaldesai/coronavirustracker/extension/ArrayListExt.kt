package com.pranjaldesai.coronavirustracker.extension

fun <T> ArrayList<T>.initWithMultipleOf(value: T, size: Int): ArrayList<T> {
    for (index in 1..size) {
        add(value)
    }
    return this
}

fun <T> ArrayList<T>.removeFromStart(numberOfValuesToRemove: Int): ArrayList<T> {
    if (size >= numberOfValuesToRemove) {
        for (index in 0 until numberOfValuesToRemove) {
            removeAt(0)
        }
    }
    return this
}