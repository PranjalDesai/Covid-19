package com.pranjaldesai.coronavirustracker.di

import android.content.Context
import com.google.gson.GsonBuilder
import com.pranjaldesai.coronavirustracker.data.models.SortItem
import com.pranjaldesai.coronavirustracker.data.preferences.CoreSharedPreferences
import com.pranjaldesai.coronavirustracker.ui.CountryDetailViewModel
import com.pranjaldesai.coronavirustracker.ui.CovidDetailViewModel
import com.pranjaldesai.coronavirustracker.ui.CovidMapViewModel
import com.pranjaldesai.coronavirustracker.ui.dialog.CoreSortDialog
import com.pranjaldesai.coronavirustracker.ui.dialog.CountrySearchDialog
import org.koin.android.ext.koin.androidApplication
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val appModule = module(override = true) {
    single { GsonBuilder() }
    single { CoreSharedPreferences(context = androidApplication(), gson = get()) }
    factory { (context: Context, sortItems: ArrayList<SortItem>) ->
        CoreSortDialog(
            context = context,
            sortItems = sortItems
        )
    }
    factory { (context: Context) -> CountrySearchDialog(context = context) }
    viewModel { CovidMapViewModel() }
    viewModel { CovidDetailViewModel() }
    viewModel { CountryDetailViewModel() }
}