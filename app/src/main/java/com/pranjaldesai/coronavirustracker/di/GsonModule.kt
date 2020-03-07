package com.pranjaldesai.coronavirustracker.di

import com.google.gson.Gson
import com.google.gson.GsonBuilder
import org.koin.dsl.module

val gsonModule = module(override = true) {
    single { buildGsonInstance(get()) }
}

private fun buildGsonInstance(builder: GsonBuilder): Gson = builder.create()