package com.pranjaldesai.coronavirustracker.data.viewholder

import androidx.recyclerview.widget.RecyclerView
import com.pranjaldesai.coronavirustracker.databinding.ViewTipListItemBinding

class TipItemViewHolder(
    val binding: ViewTipListItemBinding,
    private val listener: (Int) -> Unit
) : RecyclerView.ViewHolder(binding.root) {
    fun bind(tipUrl: String, position: Int) = with(itemView) {
        binding.data = TipViewData(tipUrl)
        setOnClickListener { listener(position) }
        binding.executePendingBindings()
    }

    class TipViewData(private val tipUrl: String) {
        val imageUrl: String = tipUrl
    }
}