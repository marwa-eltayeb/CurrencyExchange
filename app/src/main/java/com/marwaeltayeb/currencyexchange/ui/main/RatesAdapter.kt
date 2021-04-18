package com.marwaeltayeb.currencyexchange.ui.main

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getCodeName
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag


class RatesAdapter : RecyclerView.Adapter<RatesAdapter.RatesViewHolder>(){

    private var rateList: List<Pair<String, Double>> = ArrayList<Pair<String, Double>>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RatesViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.list_item_rates, parent, false)
        return RatesViewHolder(view)
    }

    override fun onBindViewHolder(holder: RatesViewHolder, position: Int) {
        val currentRate: Pair<String, Double> = rateList[position]

        val currencyName = currentRate.first
        val currencyRate = currentRate.second

        if(getCodeName(currencyName) !=  holder.itemView.resources.getString(R.string.none)) {
            holder.countryFlag.setImageResource(getFlag(currencyName))
            holder.currencyCode.text = currencyName
            holder.currencyName.text = getCodeName(currencyName)
            if (currentRate.second == 1.0) {
                holder.countryRate.text = "1"
            } else {
                holder.countryRate.text = String.format("%.4f", currencyRate)
            }
        }else{
            holder.itemView.visibility = View.GONE
            holder.itemView.layoutParams = RecyclerView.LayoutParams(0, 0)
        }
    }

    override fun getItemCount(): Int {
        return rateList.size
    }

    fun setRates(rates: List<Pair<String, Double>>){
        this.rateList = rates
        notifyDataSetChanged()
    }

    class RatesViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var countryFlag: ImageView
        var currencyCode: TextView
        var currencyName: TextView
        var countryRate: TextView

        init {
            countryFlag = itemView.findViewById(R.id.img_country_flag)
            currencyCode = itemView.findViewById(R.id.txt_currency_code)
            currencyName = itemView.findViewById(R.id.txt_currency_name)
            countryRate = itemView.findViewById(R.id.txt_currency_rate)
        }
    }
}