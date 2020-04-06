package com.pranjaldesai.coronavirustracker.data.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import com.pranjaldesai.coronavirustracker.data.viewholder.TipItemViewHolder
import com.pranjaldesai.coronavirustracker.databinding.ViewTipListItemBinding
import com.pranjaldesai.coronavirustracker.extension.launchOnMain
import kotlinx.coroutines.GlobalScope

class TipsAdapter(data: ArrayList<String>, private val clickListener: (Int) -> Unit) :
    CoreDataAdapter<String, TipItemViewHolder>(data) {

    private var workingData: ArrayList<String> = ArrayList(data)

    init {
        GlobalScope.launchOnMain {
            with(data) {
                clear()
                addAll(workingData)
            }
            notifyDataSetChanged()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TipItemViewHolder {
        val individualTipView =
            ViewTipListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return TipItemViewHolder(individualTipView, clickListener)
    }

    override fun onBindViewHolder(holder: TipItemViewHolder, position: Int) =
        holder.bind(data[position], position)

    override fun updateData(updatedData: List<String>) {
        workingData.clear()
        workingData.addAll(updatedData)
        super.updateData(updatedData)
        with(data) {
            clear()
            addAll(workingData)
        }
        notifyDataSetChanged()
    }

}