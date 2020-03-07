package com.pranjaldesai.coronavirustracker.di

import android.view.View
import com.pranjaldesai.coronavirustracker.ui.shared.SnackbarComponent
import org.koin.dsl.module

val snackbarModule = module {
    factory { (view: View) ->
        SnackbarComponent(view)
    }
}