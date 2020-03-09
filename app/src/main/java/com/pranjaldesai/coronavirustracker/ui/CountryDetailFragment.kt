package com.pranjaldesai.coronavirustracker.ui

import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.adapter.CitiesAdapter
import com.pranjaldesai.coronavirustracker.data.models.OverallCity
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.databinding.FragmentCountryDetailBinding
import com.pranjaldesai.coronavirustracker.ui.shared.CoreListFragment

class CountryDetailFragment(country: OverallCountry? = null) :
    CoreListFragment<FragmentCountryDetailBinding, CitiesAdapter>() {
    @LayoutRes
    override val layoutResourceId: Int =
        com.pranjaldesai.coronavirustracker.R.layout.fragment_country_detail
    private lateinit var country: OverallCountry
    private val args: CountryDetailFragmentArgs by navArgs()
    override val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
    }
    override val recyclerView: RecyclerView by lazy { binding.expandableCityRecyclerview }
    override val toolbar: Toolbar? by lazy { binding.toolbar }
    override val toolbarTitle: String by lazy { this.country.countryName }
    override val recyclerViewAdapter: CitiesAdapter = CitiesAdapter(ArrayList()) {
        onCitySelected(it)
    }

    override fun loadData() = loadCities()

    init {
        country?.let {
            this.country = it
        }
    }

    override fun initializeLayout() {
        configureUpNavigation()

    }

    override fun loadSavedInstanceState(savedInstanceState: Bundle?) {
        super.loadSavedInstanceState(savedInstanceState)
        if (::country.isInitialized.not()) {
            country = args.country
        }
    }

    private fun loadCities() {
        val cityList = ArrayList<OverallCity>()

        country.infectedLocations?.forEach { location ->
            val deathLocation =
                country.deathLocations?.find { it.infectedProvince == location.infectedProvince }
            val recoveredLocation =
                country.recoveredLocations?.find { it.infectedProvince == location.infectedProvince }
            val deathLocationCount = deathLocation?.totalCount ?: 0
            val recoveredLocationCount = recoveredLocation?.totalCount ?: 0
            cityList.add(
                OverallCity(
                    location.infectedHistory,
                    deathLocation?.infectedHistory,
                    recoveredLocation?.infectedHistory,
                    location.infectedProvince,
                    location.totalCount,
                    deathLocationCount,
                    recoveredLocationCount,
                    country.countryName
                )
            )
        }
        recyclerViewAdapter.updateData(cityList)
        hideProgressIndicator()
    }

    private fun onCitySelected(position: Int) {
        val city = recyclerViewAdapter.getItemAtPosition(position)
        city.isExpanded = !city.isExpanded
        recyclerViewAdapter.updateItem(position, city)
    }

    private fun configureUpNavigation() =
        context?.let { context ->
            toolbar?.apply {
                navigationIcon = ContextCompat.getDrawable(context, R.drawable.ic_home_up_indicator)
                navigationContentDescription = getString(R.string.abc_action_bar_up_description)
                setNavigationOnClickListener { findNavController().navigateUp() }
            }
        }
}