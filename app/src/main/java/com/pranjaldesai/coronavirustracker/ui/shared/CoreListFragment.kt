package com.pranjaldesai.coronavirustracker.ui.shared

import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout

abstract class CoreListFragment<ViewDataBindingT : ViewDataBinding, AdapterT : RecyclerView.Adapter<*>> :
    CoreFragment<ViewDataBindingT>() {

    abstract val recyclerView: RecyclerView

    abstract val recyclerViewAdapter: AdapterT

    abstract val layoutManager: RecyclerView.LayoutManager

    open var loadDataWhenForeground: Boolean = false

    open val swipeToRefreshLayout: SwipeRefreshLayout? = null

    private val swipeRefreshListener: SwipeRefreshLayout.OnRefreshListener by lazy {
        SwipeRefreshLayout.OnRefreshListener { loadData() }
    }

    override fun bindData() {
        super.bindData()
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager
        initializeSwipeToRefresh()
        initializeLayout()
        showProgressIndicator()
        if (loadDataWhenForeground.not()) {
            loadData()
        }
    }

    override fun onResume() {
        super.onResume()
        activity?.window?.setFlags(
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS,
            WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS
        )
        if (loadDataWhenForeground) {
            loadData()
        }
    }

    abstract fun initializeLayout()

    abstract fun loadData()

    override fun hideProgressIndicator() {
        super.hideProgressIndicator()
        swipeToRefreshLayout?.isRefreshing = false
    }

    private fun initializeSwipeToRefresh() =
        swipeToRefreshLayout?.setOnRefreshListener(swipeRefreshListener)

    open fun onRecyclerViewItemSelected(selectedIndex: Int) {}
}