package com.pranjaldesai.coronavirustracker.ui.dialog

import android.content.Context
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.ListSortStyle
import com.pranjaldesai.coronavirustracker.data.adapter.CoreSortListAdapter
import com.pranjaldesai.coronavirustracker.data.models.SortItem

class CoreSortDialog(context: Context, private val sortItems: ArrayList<SortItem>) :
    BottomSheetDialog(context) {

    private val sortLayoutView: View by lazy { layoutInflater.inflate(R.layout.dialog_sort, null) }
    private val sortRecyclerView: RecyclerView by lazy { sortLayoutView.findViewById<RecyclerView>(R.id.sort_recycler_view) }
    private val layoutManager: LinearLayoutManager =
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    private lateinit var sortSelectionListener: (selectedSort: ListSortStyle) -> Unit
    private val sortAdapter: CoreSortListAdapter = CoreSortListAdapter(sortItems) { position ->
        onSortSelected(position)
    }

    init {
        sortRecyclerView.layoutManager = layoutManager
        sortRecyclerView.adapter = sortAdapter
        setContentView(sortLayoutView)
    }

    fun show(
        currentSortStyle: ListSortStyle,
        onSortSelected: (ListSortStyle) -> Unit = { _ -> dismiss() }
    ) {
        updateCurrentSortStyle(currentSortStyle)
        sortSelectionListener = onSortSelected
        super.show()
    }

    override fun dismiss() {
        if (isShowing) {
            super.dismiss()
        }
    }

    private fun onSortSelected(position: Int) {
        sortSelectionListener(sortAdapter.getItemAtPosition(position).sortStyle)
        dismiss()
    }

    private fun updateCurrentSortStyle(currentSortStyle: ListSortStyle) {
        sortItems.forEach { sortItem ->
            sortItem.isSortSelected = sortItem.sortStyle == currentSortStyle
        }
        sortAdapter.updateData(sortItems)
    }
}