package com.pranjaldesai.coronavirustracker.ui

import androidx.annotation.LayoutRes
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.adapter.TipsAdapter
import com.pranjaldesai.coronavirustracker.databinding.FragmentCovidTipBinding
import com.pranjaldesai.coronavirustracker.ui.shared.CoreListFragment
import com.pranjaldesai.coronavirustracker.ui.shared.IPrimaryFragment
import com.pranjaldesai.coronavirustracker.ui.shared.ImageFullScreenDialog
import org.koin.android.ext.android.inject
import org.koin.core.parameter.parametersOf

class CovidTipFragment :
    CoreListFragment<FragmentCovidTipBinding, TipsAdapter>(), IPrimaryFragment {
    @LayoutRes
    override val layoutResourceId: Int = R.layout.fragment_covid_tip

    override val layoutManager: LinearLayoutManager by lazy {
        LinearLayoutManager(
            context,
            RecyclerView.VERTICAL,
            false
        )
    }
    override val recyclerView: RecyclerView by lazy { binding.tipRecyclerview }
    override val toolbar: Toolbar? by lazy { binding.toolbar }
    override val toolbarTitle: String by lazy { getString(R.string.covid_tip_toolbar_title) }
    override val recyclerViewAdapter: TipsAdapter = TipsAdapter(ArrayList()) {
        onTipSelected(it)
    }
    private val tipArray: Array<String> by lazy { resources.getStringArray(R.array.tip_urls) }
    private val bottomNavOptionId: Int = R.id.covidTip
    private val imageFullScreenDialog: ImageFullScreenDialog by inject { parametersOf(context) }

    override fun loadData() = loadTipUrls()

    override fun initializeLayout() {}

    private fun loadTipUrls() {
        hideProgressIndicator()
        recyclerViewAdapter.updateData(tipArray.toList())
    }

    private fun onTipSelected(position: Int) {
        val specificTip = recyclerViewAdapter.getItemAtPosition(position)
        imageFullScreenDialog.imageUrl(specificTip).show()
    }

    override fun onResume() {
        super.onResume()
        subscribeToNavigationHost()
        updateBottomNavigationSelection(bottomNavOptionId)
    }

    override fun onPause() {
        super.onPause()
        unsubscribeFromNavigationHost()
    }
}