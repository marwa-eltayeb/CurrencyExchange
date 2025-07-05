package com.marwaeltayeb.currencyexchange.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.currencyexchange.data.file.CurrencyData
import com.marwaeltayeb.currencyexchange.databinding.ListItemRatesBinding
import com.marwaeltayeb.currencyexchange.data.model.Currency
import com.marwaeltayeb.currencyexchange.data.file.CurrencyLoader
import com.marwaeltayeb.currencyexchange.utils.CurrencyUtils.updateCurrencyUi
import java.util.*

class RatesAdapter : ListAdapter<Pair<String, Double>, RecyclerView.ViewHolder>(DiffCallback()){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int):  RecyclerView.ViewHolder {
        val binding = ListItemRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(binding)
    }

    override fun onBindViewHolder(holder:  RecyclerView.ViewHolder, position: Int) {
        val currentRate: Pair<String, Double> = getItem(position)
        (holder as RatesViewHolder).bind(currentRate)
    }

    class RatesViewHolder(var binding: ListItemRatesBinding) : RecyclerView.ViewHolder(binding.root){

        fun bind(currentRate: Pair<String, Double>) {
            val currencyCode = currentRate.first
            val currencyRate = currentRate.second

            val currencyInfo = CurrencyData.getCurrencyInfo()
            val currency = currencyInfo[currencyCode]

            if (currency != null) {
                updateCurrencyUi(
                    currencyCode,
                    binding.imgCountryFlag,
                    binding.txtCurrencyCode,
                    binding.txtCurrencyName,
                    binding.root.context
                )

                if (currentRate.second == 1.0) {
                    binding.txtCurrencyRate.text = "1"
                } else {
                    binding.txtCurrencyRate.text = String.format(Locale.US,"%.4f", currencyRate)
                }
            } else {
                binding.root.visibility = View.GONE
                binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            }
        }
    }


}

private class DiffCallback : DiffUtil.ItemCallback<Pair<String, Double>>() {
    override fun areItemsTheSame(oldItem: Pair<String, Double>, newItem: Pair<String, Double>): Boolean {
        return oldItem.first == newItem.first
    }

    override fun areContentsTheSame(oldItem: Pair<String, Double>, newItem: Pair<String, Double>): Boolean {
        return oldItem == newItem
    }
}

