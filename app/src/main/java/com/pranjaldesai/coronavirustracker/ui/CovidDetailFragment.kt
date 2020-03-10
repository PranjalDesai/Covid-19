package com.pranjaldesai.coronavirustracker.ui

import android.graphics.Color
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
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
import com.pranjaldesai.coronavirustracker.extension.LogExt.loge
import com.pranjaldesai.coronavirustracker.helper.generateCountrySortList
import com.pranjaldesai.coronavirustracker.ui.dialog.CoreSortDialog
import com.pranjaldesai.coronavirustracker.ui.dialog.CountrySearchDialog
import com.pranjaldesai.coronavirustracker.ui.shared.CoreFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment
import com.pranjaldesai.coronavirustracker.ui.shared.subscribe
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class CovidDetailFragment : CoreFragment<FragmentCovidDetailBinding>(), IPrimaryFragment,
    ICovidView {
    override val layoutResourceId: Int = R.layout.fragment_covid_detail
    override val lifecycleOwner: LifecycleOwner by lazy { this }
    override val toolbar: Toolbar? by lazy { binding.toolbar }
    override val toolbarTitle: String by lazy { getString(R.string.covid_detail_toolbar_title) }
    override val menuResourceId: Int? = R.menu.toolbar_menu

    private val viewModel: CovidDetailViewModel by viewModel()
    private val sharedPreferences: CoreSharedPreferences by inject()
    private val searchDialog: CountrySearchDialog by inject { parametersOf(context) }
    private val bottomNavOptionId: Int = R.id.covidDetail
    private val databaseRef = FirebaseDatabase.getInstance().reference
    private val overallCountryList = ArrayList<OverallCountry>()
    private val layoutManager: RecyclerView.LayoutManager =
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    private val recyclerViewAdapter: CountryAdapter =
        CountryAdapter(ArrayList(), sharedPreferences.countrySelectedSortStyle) {
            onCountryRecyclerViewClick(it)
        }
    private val sortDialog: CoreSortDialog by inject {
        parametersOf(
            context,
            generateCountrySortList()
        )
    }

    override fun bindData() {
        super.bindData()
        viewModel.subscribe(this, lifecycleOwner)
        binding.detailRecyclerview.adapter = recyclerViewAdapter
        binding.detailRecyclerview.layoutManager = layoutManager
        val postListener = object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val covidStats = dataSnapshot.getValue(CovidStats::class.java)
                viewModel.covidStats = covidStats
                updatePieChart()
                updateCountryAdapter()
            }

            override fun onCancelled(databaseError: DatabaseError) {
                loge(databaseError.toException())
            }
        }
        databaseRef.addValueEventListener(postListener)
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

    override fun onMenuItemClick(item: MenuItem?): Boolean {
        return when (item?.itemId) {
            R.id.sort_button -> {
                showSortSheet()
                true
            }
            R.id.search_button -> {
                searchDialog.show(overallCountryList, ::searchResultSelected)
                true
            }
            else -> super.onMenuItemClick(item)
        }
    }

    private fun updateCountryAdapter() {
        overallCountryList.clear()
        overallCountryList.addAll(viewModel.generateOverallCountryList())
        recyclerViewAdapter.updateData(overallCountryList)

    }

    private fun updatePieChart() {
        with(binding.pieChart) {
            data = viewModel.generatePieData()
            description.isEnabled = false
            legend.isEnabled = false
            extraLeftOffset = EXTRA_OFFSET
            extraBottomOffset = EXTRA_OFFSET
            extraRightOffset = EXTRA_OFFSET
            extraTopOffset = EXTRA_OFFSET
            holeRadius = HOLE_RADIUS
            setEntryLabelTextSize(ENTRY_LABEL_TEXT_SIZE)
            setUsePercentValues(false)
            setEntryLabelColor(Color.BLACK)
            animateXY(PIE_CHART_ANIMATION, PIE_CHART_ANIMATION)
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

    private fun searchResultSelected(country: OverallCountry) {
        searchDialog.dismiss()
        navigateToCountryDetail(country)
    }

    private fun navigateToCountryDetail(country: OverallCountry) {
        findNavController().navigate(
            CovidDetailFragmentDirections.actionCovidDetailToCountryDetailFragment(country)
        )
    }

    companion object {
        const val EXTRA_OFFSET = 10f
        const val HOLE_RADIUS = 35f
        const val PIE_CHART_ANIMATION = 2000
        const val ENTRY_LABEL_TEXT_SIZE = 14f
    }
}