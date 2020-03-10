package com.pranjaldesai.coronavirustracker.ui.shared

import android.view.WindowManager
import androidx.databinding.ViewDataBinding
import androidx.recyclerview.widget.RecyclerView

abstract class CoreListFragment<ViewDataBindingT : ViewDataBinding, AdapterT : RecyclerView.Adapter<*>> :
    CoreFragment<ViewDataBindingT>() {

    abstract val recyclerView: RecyclerView

    abstract val recyclerViewAdapter: AdapterT

    abstract val layoutManager: RecyclerView.LayoutManager

    open var loadDataWhenForeground: Boolean = false

    override fun bindData() {
        super.bindData()
        recyclerView.adapter = recyclerViewAdapter
        recyclerView.layoutManager = layoutManager
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

    open fun onRecyclerViewItemSelected(selectedIndex: Int) {}
}