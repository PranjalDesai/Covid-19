package com.pranjaldesai.coronavirustracker.di

import com.crashlytics.android.Crashlytics
import com.crashlytics.android.core.CrashlyticsCore
import org.koin.dsl.module

val crashlyticsModule = module(override = true) {
    single {
        Crashlytics.Builder().core(
            CrashlyticsCore
                .Builder()
                .build()
        ).build()
    }
}