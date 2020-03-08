package com.pranjaldesai.coronavirustracker.ui.dialog

import android.app.Dialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.R
import com.pranjaldesai.coronavirustracker.data.adapter.CoreDataAdapter
import com.pranjaldesai.coronavirustracker.di.DispatcherProvider
import com.pranjaldesai.coronavirustracker.extension.launchOnMain
import com.pranjaldesai.coronavirustracker.helper.EMPTY_STRING
import kotlinx.android.synthetic.main.dialog_search.view.*
import kotlinx.coroutines.CoroutineScope
import org.koin.core.KoinComponent
import kotlin.coroutines.CoroutineContext

abstract class CoreSearchDialog<T>(val context: Context) : CoroutineScope, KoinComponent {

    override val coroutineContext: CoroutineContext = DispatcherProvider.provideDefaultContext()
    private val dialog: Dialog by lazy { setUpSearchDialog() }
    private val layoutInflater: LayoutInflater = LayoutInflater.from(context)
    private val layoutManager: LinearLayoutManager =
        LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    private val searchLayoutView: View by lazy {
        layoutInflater.inflate(
            R.layout.dialog_search,
            null
        )
    }
    private val searchView: SearchView by lazy { searchLayoutView.findViewById<SearchView>(R.id.searchView) }
    protected val searchRecyclerView: RecyclerView by lazy {
        searchLayoutView.findViewById<RecyclerView>(
            R.id.search_list_recycler_view
        )
    }
    private var lastQuery: String? = null

    val clickListener: (Int) -> Unit = { onSearchListItemSelected(it) }
    private lateinit var itemSelectionListener: (selectedItem: T) -> Unit

    abstract fun searchPredicateValidator(item: T, userInput: String): Boolean
    abstract fun loadItems(items: ArrayList<T>)

    abstract val searchAdapter: CoreDataAdapter<T, out RecyclerView.ViewHolder>

    private fun setUpAdapter() {
        searchRecyclerView.adapter = searchAdapter
    }

    private fun updateDataAdapter(queriedList: ArrayList<T>) {
        launchOnMain {
            searchAdapter.updateData(queriedList)
        }
    }

    private fun itemAtIndex(selectedIndex: Int): T = searchAdapter.getItemAtPosition(selectedIndex)

    var items: List<T> = ArrayList()
        set(value) {
            field = value
            lastQuery?.let { query ->
                if (value.isNotEmpty()) {
                    queryItems(ArrayList(), query)
                }
            }
        }

    fun show(items: ArrayList<T>, onItemSelected: (T) -> Unit = { _ -> dismiss() }) {
        setUpRecyclerViewAndTextChangeListener()
        itemSelectionListener = onItemSelected
        loadItems(items)
        searchView.setQuery(EMPTY_STRING, false)
        dialog.show()

        prepareKeyboardOnTouch()
    }

    private fun prepareKeyboardOnTouch() {
        searchLayoutView.searchView.clearFocus()
        searchLayoutView.searchView.requestFocusFromTouch()
    }

    fun dismiss() {
        if (dialog.isShowing) {
            dialog.dismiss()
        }
    }

    private fun setUpSearchDialog(): Dialog =
        Dialog(context, android.R.style.Theme_Material_NoActionBar_TranslucentDecor).apply {
            setContentView(searchLayoutView)
            searchLayoutView.toolbarDialog.setNavigationOnClickListener { hide() }
        }

    private fun setUpRecyclerViewAndTextChangeListener() {
        val queriedList = ArrayList<T>()

        updateDataAdapter(queriedList)

        searchRecyclerView.layoutManager = layoutManager
        setUpAdapter()

        searchLayoutView.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return lastQuery != query
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return queryItems(queriedList, newText)
            }
        })
    }

    private fun queryItems(queriedList: ArrayList<T>, queryText: String?): Boolean =
        if (this.lastQuery != queryText) {
            this.lastQuery = queryText
            queriedList.clear()

            queryText?.let {
                if (it.isNotEmpty()) {
                    updateListFromQuery(it, queriedList)
                }
            }
            updateDataAdapter(queriedList)
            true
        } else {
            false
        }

    private fun updateListFromQuery(queryText: String, queriedList: ArrayList<T>) {
        items.forEach { items ->
            if (searchPredicateValidator(items, queryText)) {
                queriedList.add(items)
            }
        }
    }

    private fun onSearchListItemSelected(selectedIndex: Int) {
        val selectedReport = itemAtIndex(selectedIndex)
        itemSelectionListener(selectedReport)
    }

}