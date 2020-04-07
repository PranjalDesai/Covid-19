package com.pranjaldesai.coronavirustracker.ui

import android.graphics.Color
import android.os.Bundle
import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.adapter.CitiesAdapter
import com.pranjaldesai.coronavirustracker.data.models.OverallCountry
import com.pranjaldesai.coronavirustracker.databinding.FragmentCountryDetailBinding
import com.pranjaldesai.coronavirustracker.ui.shared.CoreListFragment
import com.pranjaldesai.coronavirustracker.ui.shared.subscribe
import org.koin.androidx.viewmodel.ext.android.viewModel

class CountryDetailFragment(country: OverallCountry? = null) :
    CoreListFragment<FragmentCountryDetailBinding, CitiesAdapter>(), ICovidView {
    @LayoutRes
    override val layoutResourceId: Int = R.layout.fragment_country_detail
    private lateinit var country: OverallCountry
    private val args: CountryDetailFragmentArgs by navArgs()
    override val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
    }
    private var isDarkMode = false
    override val recyclerView: RecyclerView by lazy { binding.expandableCityRecyclerview }
    override val toolbar: Toolbar? by lazy { binding.toolbar }
    override val toolbarTitle: String by lazy { this.country.countryName }
    private val viewModel: CountryDetailViewModel by viewModel()
    override val lifecycleOwner: LifecycleOwner by lazy { this }
    override val recyclerViewAdapter: CitiesAdapter = CitiesAdapter(ArrayList(), isDarkMode) {
        onCitySelected(it)
    }

    override fun loadData() = loadCities()

    init {
        country?.let {
            this.country = it
        }
    }

    override fun bindData() {
        viewModel.subscribe(this, lifecycleOwner)
        viewModel.overallCountry = country
        super.bindData()
        generateDailyInfectedChart()
    }

    private fun generateDailyInfectedChart() {
        val dailyInfectedMap = viewModel.generateDailyInfected()
        val xAxis = ArrayList<String>(dailyInfectedMap.keys)
        val yAxisInfectedHistory = viewModel.populateDailyHistoryYAxisData(dailyInfectedMap)
        val textColor = generateChartTextColor()
        val lineDataSet = viewModel.generateLineDataSet(yAxisInfectedHistory, textColor)
        val lineData = LineData(lineDataSet)
        with(binding.stackedLineChart) {
            data = lineData
            description.isEnabled = false
            legend.isEnabled = false
            this.xAxis.valueFormatter = IndexAxisValueFormatter(xAxis)
            this.xAxis.setAvoidFirstLastClipping(true)
            this.xAxis.setCenterAxisLabels(true)
            this.xAxis.textColor = textColor
            this.axisLeft.textColor = textColor
            this.legend.textColor = textColor
            setBorderColor(textColor)
            data.isHighlightEnabled = false
            axisRight.isEnabled = false
            setPinchZoom(false)
            animateXY(LINE_CHART_ANIMATION, LINE_CHART_ANIMATION)
        }
    }

    private fun generateChartTextColor(): Int {
        return if (isDarkMode) {
            Color.WHITE
        } else {
            Color.BLACK
        }
    }

    override fun initializeLayout() {
        configureUpNavigation()
        isDarkMode = resources.getBoolean(R.bool.isDarkMode)
    }

    override fun loadSavedInstanceState(savedInstanceState: Bundle?) {
        super.loadSavedInstanceState(savedInstanceState)
        if (::country.isInitialized.not()) {
            country = args.country
        }
    }

    private fun loadCities() {
        recyclerViewAdapter.updateTheme(isDarkMode)
        recyclerViewAdapter.updateData(viewModel.generateCities(country.countryName))
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

    companion object {
        const val DEFAULT_FLOAT = 0f
        const val LINE_WIDTH = 3f
        const val LINE_CHART_ANIMATION = 1000
        val color = Color.parseColor("#FFC154")
    }
}