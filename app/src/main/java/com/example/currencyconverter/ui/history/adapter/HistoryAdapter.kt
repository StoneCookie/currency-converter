package com.example.currencyconverter.ui.history.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.currencyconverter.data.model.History
import com.example.currencyconverter.databinding.FragmentHistoryItemBinding

class HistoryAdapter: RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    var historyList: List<History> = listOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val inflater = LayoutInflater.from(parent.context)

        val binding = FragmentHistoryItemBinding.inflate(inflater, parent, false)

        return HistoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {

        val item = historyList[position]

        holder.binding.apply {
            historyVaultStart.text = item.first_vault
            historyVaultFinish.text = item.sec_vault

            historyStartPrice.text = item.first_price
            historyFinishPrice.text = item.sec_price

            historyDateExchange.text = item.date
        }

    }

    override fun getItemCount(): Int {
        return historyList.size
    }

    class HistoryViewHolder (var binding: FragmentHistoryItemBinding) :
        RecyclerView.ViewHolder(binding.root)
}