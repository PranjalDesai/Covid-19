package com.pranjaldesai.coronavirustracker.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.data.models.SortItem
import com.pranjaldesai.coronavirustracker.data.viewholder.CoreSortListItemViewHolder
import com.pranjaldesai.coronavirustracker.databinding.DialogSortItemBinding
import org.koin.core.KoinComponent

class CoreSortListAdapter(data: ArrayList<SortItem>, private val clickListener: (Int) -> Unit) :
    RecyclerView.Adapter<CoreSortListItemViewHolder>(), KoinComponent {

    private val data: ArrayList<SortItem> = ArrayList(data)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CoreSortListItemViewHolder {
        val sortItemView =
            DialogSortItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CoreSortListItemViewHolder(sortItemView, clickListener)
    }

    override fun getItemCount(): Int = data.size

    override fun onBindViewHolder(holder: CoreSortListItemViewHolder, position: Int) =
        holder.bind(data[position], position)

    fun getItemAtPosition(position: Int): SortItem = data[position]

    fun updateData(framesData: List<SortItem>) {
        data.clear()
        data.addAll(framesData)
        notifyDataSetChanged()
    }
}