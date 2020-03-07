package com.pranjaldesai.coronavirustracker.di

import android.content.Context
import com.pranjaldesai.coronavirustracker.ui.shared.DividerItemDecoration
import org.koin.dsl.module

val dividerItemDecorationModule = module(override = true) {
    factory { (context: Context) ->
        DividerItemDecoration(
            context
        )
    }
}