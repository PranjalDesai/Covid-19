package com.pranjaldesai.coronavirustracker.data.adapter

import androidx.annotation.CallSuper
import androidx.recyclerview.widget.RecyclerView

abstract class CoreDataAdapter<ModelT, ViewHolderT : RecyclerView.ViewHolder>(adapterData: List<ModelT> = ArrayList()) :
    RecyclerView.Adapter<ViewHolderT>() {

    val data: ArrayList<ModelT> = ArrayList(adapterData)

    @CallSuper
    open fun updateData(updatedData: List<ModelT>) {
        data.apply {
            clear()
            addAll(updatedData)
            notifyDataSetChanged()
        }
    }

    @CallSuper
    open fun updateDataAtPosition(position: Int, updatedItem: ModelT) {
        data[position] = updatedItem
        notifyItemChanged(position)
    }

    final override fun getItemCount(): Int = data.size

    fun getItemAtPosition(position: Int): ModelT = data[position]
}