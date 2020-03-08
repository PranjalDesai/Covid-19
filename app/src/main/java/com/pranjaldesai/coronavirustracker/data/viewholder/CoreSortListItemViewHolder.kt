package com.pranjaldesai.coronavirustracker.data.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.data.models.SortItem
import com.pranjaldesai.coronavirustracker.databinding.DialogSortItemBinding

class CoreSortListItemViewHolder(
    val binding: DialogSortItemBinding,
    private val listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {

    fun bind(sortItem: SortItem, position: Int) = with(itemView) {
        binding.data = SortData(sortItem)
        itemView.setOnClickListener { listener(position) }
        binding.executePendingBindings()
    }

    class SortData(sortItem: SortItem) {
        val title = sortItem.text
        val invisibleIfFalse = sortItem.isSortSelected
    }
}