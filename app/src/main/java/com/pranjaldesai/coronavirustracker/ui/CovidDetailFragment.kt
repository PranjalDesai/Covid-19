package com.pranjaldesai.coronavirustracker.ui

import android.graphics.Color
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.ListSortStyle
import com.pranjaldesai.coronavirustracker.data.adapter.CountryAdapter
import com.pranjaldesai.coronavirustracker.data.models.CovidStats
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.data.preferences.CoreSharedPreferences
import com.pranjaldesai.coronavirustracker.databinding.FragmentCovidDetailBinding
import com.pranjaldesai.coronavirustracker.extension.LogExt
import com.pranjaldesai.coronavirustracker.helper.generateCountrySortList
import com.pranjaldesai.coronavirustracker.ui.dialog.CoreSortDialog
import com.pranjaldesai.coronavirustracker.ui.dialog.CountrySearchDialog
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CovidDetailFragment : CoreFragment<FragmentCovidDetailBinding>(), IPrimaryFragment {
    override val layoutResourceId: Int = R.layout.fragment_covid_detail

    private val sortDialog: CoreSortDialog by inject {
        parametersOf(
            context,
            generateCountrySortList()
        )
    }
    private val sharedPreferences: CoreSharedPreferences by inject()
    private val bottomNavOptionId: Int = R.id.covidDetail
    private val databaseRef = FirebaseDatabase.getInstance().reference
    override val toolbar: Toolbar? by lazy { binding.toolbar }
    private val searchDialog: CountrySearchDialog by inject { parametersOf(context) }
    override val toolbarTitle: String by lazy { "Coronavirus Stats" }
    override val menuResourceId: Int? = R.menu.toolbar_menu
    private val colors = listOf(Color.YELLOW, Color.RED, Color.GREEN)
    private val layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    private val recyclerViewAdapter: CountryAdapter =
        CountryAdapter(ArrayList(), sharedPreferences.countrySelectedSortStyle) {
            onCountryRecyclerViewClick(it)
        }

    private var covidStats: CovidStats? = null
    private var overallCountryList: ArrayList<OverallCountry>? = null


    override fun bindData() {
        super.bindData()
        binding.detailRecyclerview.adapter = recyclerViewAdapter
        binding.detailRecyclerview.layoutManager = layoutManager
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                covidStats = dataSnapshot.getValue(CovidStats::class.java)
                covidStats?.let {
                    updatePieChart(it)
                    updateCountryAdapter(it)
                }

            }

            override fun onCancelled(databaseError: DatabaseError) {
                LogExt.loge(databaseError.toException())
            }
        }
        databaseRef.addValueEventListener(postListener)
    }

    private fun updateCountryAdapter(covidStats: CovidStats) {
        val overallCountry = ArrayList<OverallCountry>()
        val groupedConfirmed =
            covidStats.confirmed?.infectedLocations?.groupBy { location -> location.infectedCountry }
        val groupedDeath =
            covidStats.death?.infectedLocations?.groupBy { location -> location.infectedCountry }
        val groupedRecovered =
            covidStats.recovered?.infectedLocations?.groupBy { location -> location.infectedCountry }
        groupedConfirmed?.keys?.forEach { key ->
            var totalInfected = 0
            var totalRecovered = 0
            var totalDeath = 0
            groupedConfirmed[key]?.forEach { location -> totalInfected += location.totalCount }
            groupedDeath?.get(key)?.forEach { location -> totalDeath += location.totalCount }
            groupedRecovered?.get(key)
                ?.forEach { location -> totalRecovered += location.totalCount }

            key?.let { it1 ->
                overallCountry.add(
                    OverallCountry(
                        it1,
                        totalInfected,
                        totalDeath,
                        totalRecovered,
                        groupedConfirmed[key],
                        groupedDeath?.get(key),
                        groupedRecovered?.get(key)
                    )
                )
            }
        }
        overallCountryList = overallCountry
        recyclerViewAdapter.updateData(overallCountry)

    }

    private fun updatePieChart(covidStats: CovidStats) {
        val chartData = ArrayList<PieEntry>()
        val confirmedCases = covidStats.confirmed?.overallTotalCount ?: 0
        val deathCases = covidStats.death?.overallTotalCount ?: 0
        val recoveredCases = covidStats.recovered?.overallTotalCount ?: 0
        val description = binding.pieChart.description
        description.isEnabled = false
        chartData.add(PieEntry(confirmedCases.toFloat(), "Infected Cases"))
        chartData.add(PieEntry(deathCases.toFloat(), "Death Cases"))
        chartData.add(PieEntry(recoveredCases.toFloat(), "Recovered Cases"))

        val pieDataSet = PieDataSet(chartData, "")
        pieDataSet.colors = colors
        pieDataSet.valueTextColor = Color.BLACK
        pieDataSet.valueTextSize = 16.toFloat()
        pieDataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        pieDataSet.xValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE
        with(binding.pieChart) {
            data = PieData((pieDataSet))
            holeRadius = 25f
            transparentCircleRadius = 15f
            legend.isWordWrapEnabled = true
            legend.textColor = Color.BLACK
            legend.textSize = 10f
            legend.direction = Legend.LegendDirection.LEFT_TO_RIGHT
            setEntryLabelColor(Color.BLACK)
            setEntryLabelTextSize(14f)
            setUsePercentValues(false)
            extraLeftOffset = 10f
            extraBottomOffset = 10f
            extraRightOffset = 10f
            extraTopOffset = 10f
            offsetLeftAndRight(10)
            this.description = description
            animateY(1000)
        }
    }

    private fun onCountryRecyclerViewClick(position: Int) {
        navigateToCountryDetail(recyclerViewAdapter.getItemAtPosition(position))
    }

    private fun showSortSheet() {
        sortDialog.show(sharedPreferences.countrySelectedSortStyle) { updatedSortStyle ->
            sharedPreferences.countrySelectedSortStyle = updatedSortStyle
            updateAdapterBasedOnSort(updatedSortStyle)
        }
    }

    private fun updateAdapterBasedOnSort(updatedSortStyle: ListSortStyle) {
        recyclerViewAdapter.sort(updatedSortStyle)
        binding.detailRecyclerview.invalidate()
    }

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.sort_button -> {
                showSortSheet()
                true
            }
            R.id.search_button -> {
                overallCountryList?.let { searchDialog.show(it, ::searchResultSelected) }
                true
            }
            else -> super.onMenuItemClick(item)
        }
    }

    private fun searchResultSelected(country: OverallCountry) {
        searchDialog.dismiss()
        navigateToCountryDetail(country)
    }

    private fun navigateToCountryDetail(country: OverallCountry) {
        findNavController().navigate(
            CovidDetailFragmentDirections.actionCovidDetailToCountryDetailFragment(
                country
            )
        )
    }

    override fun onResume() {
        super.onResume()
        subscribeToNavigationHost()
        updateBottomNavigationSelection(bottomNavOptionId)
    }

    override fun onPause() {
        super.onPause()
        sortDialog.dismiss()
        searchDialog.dismiss()
        unsubscribeFromNavigationHost()
    }
}