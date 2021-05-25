package com.marwaeltayeb.currencyexchange.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.databinding.ListItemRatesBinding
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getCodeName
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag

class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>(){

    private var rateList: List<Pair<String, Double>> = ArrayList<Pair<String, Double>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val binding = ListItemRatesBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return RatesViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val currentRate: Pair<String, Double> = rateList[position]
        holder.bind(currentRate)
    }

    override fun getItemCount(): Int {
        return rateList.size
    }

    fun setRates(rates: List<Pair<String, Double>>){
        this.rateList = rates
        notifyDataSetChanged()
    }

    class RatesViewHolder(var binding: ListItemRatesBinding) : RecyclerView.ViewHolder(binding.root){
        fun bind(currentRate: Pair<String, Double>) {
            val currencyName = currentRate.first
            val currencyRate = currentRate.second

            if(getCodeName(currencyName) !=  binding.root.resources.getString(R.string.none)) {
                binding.imgCountryFlag.setImageResource(getFlag(currencyName))
                binding.txtCurrencyCode.text = currencyName
                binding.txtCurrencyName.text = getCodeName(currencyName)
                if (currentRate.second == 1.0) {
                    binding.txtCurrencyRate.text = "1"
                } else {
                    binding.txtCurrencyRate.text = String.format("%.4f", currencyRate)
                }
            }else{
                binding.root.visibility = View.GONE
                binding.root.layoutParams = RecyclerView.LayoutParams(0, 0)
            }
        }
    }
}