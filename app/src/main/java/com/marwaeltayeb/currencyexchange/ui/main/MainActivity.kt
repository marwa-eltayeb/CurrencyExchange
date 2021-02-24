package com.marwaeltayeb.currencyexchange.ui.main

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.currencyexchange.R

import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertActivity
import com.marwaeltayeb.currencyexchange.utils.Code
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.FROM_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.TO_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getCodeName
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag

class MainActivity : AppCompatActivity() {

    private lateinit var img_currency_flag_from: ImageView
    private lateinit var img_convert: ImageView
    private lateinit var img_currency_flag_to: ImageView
    private lateinit var txt_currency_name_from: TextView
    private lateinit var txt_currency_name_to: TextView

    private lateinit var txt_currency_rate_from: TextView
    private lateinit var txt_currency_code_from: TextView
    private lateinit var txt_currency_rate_to: TextView
    private lateinit var txt_currency_code_to: TextView

    private lateinit var recyclerView: RecyclerView
    private lateinit var ratesAdapter: RatesAdapter
    private lateinit var ratesViewModel:RatesViewModel

    private var isSwitched = true

    private var baseCurrency = FROM_CURRENCY
    private var convertedToCurrency = TO_CURRENCY

    private lateinit var listview: ListView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        ratesViewModel = ViewModelProvider(this).get(RatesViewModel::class.java)

        loadAllRates(FROM_CURRENCY)
        loadDefaultRate()

        img_convert.setOnClickListener {
            exchangeRate()
        }

        img_currency_flag_from.setOnClickListener {
            showCustomAlertDialog(true)
        }

        img_currency_flag_to.setOnClickListener {
            showCustomAlertDialog(false)
        }
    }

    private fun initViews(){
        img_currency_flag_from = findViewById(R.id.img_currency_flag_from)
        img_convert = findViewById(R.id.img_convert)
        img_currency_flag_to = findViewById(R.id.img_currency_flag_to)
        txt_currency_name_from = findViewById(R.id.txt_currency_name_from)
        txt_currency_name_to = findViewById(R.id.txt_currency_name_to)
        txt_currency_rate_from = findViewById(R.id.txt_currency_rate_from)
        txt_currency_code_from = findViewById(R.id.txt_currency_code_from)
        txt_currency_rate_to = findViewById(R.id.txt_currency_rate_to)
        txt_currency_code_to = findViewById(R.id.txt_currency_code_to)

        recyclerView = findViewById(R.id.rc_rates_list)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        ratesAdapter = RatesAdapter()
        recyclerView.adapter = ratesAdapter
    }

    private fun loadAllRates(base: String) {
        ratesViewModel.getAllRates(base).observe(this, {
            ratesAdapter.setRates(it)
            ratesAdapter.notifyDataSetChanged()
        })
    }

    private fun updateExchangeRateUi(baseCurrency:String, convertedToCurrency: String){
        img_currency_flag_from.setImageResource(getFlag(baseCurrency))
        txt_currency_name_from.text = getCodeName(baseCurrency)
        txt_currency_code_from.text = baseCurrency
        txt_currency_rate_from.text = "1"

        img_currency_flag_to.setImageResource(getFlag(convertedToCurrency))
        txt_currency_name_to.text = getCodeName(convertedToCurrency)
        txt_currency_code_to.text = convertedToCurrency
    }

    private fun loadDefaultRate() {
        getRate(baseCurrency, convertedToCurrency)
        
        updateExchangeRateUi(baseCurrency, convertedToCurrency)
    }

    private fun exchangeRate(){
        if(isSwitched) {

            getRate(convertedToCurrency, baseCurrency)

            loadAllRates(convertedToCurrency)

            updateExchangeRateUi(convertedToCurrency, baseCurrency)

            isSwitched = false
        }else {

            getRate(baseCurrency, convertedToCurrency)

            loadAllRates(baseCurrency)

            updateExchangeRateUi(baseCurrency, convertedToCurrency)

            isSwitched = true
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.convert_action -> {
                val intent = Intent(this, ConvertActivity::class.java)
                startActivity(intent)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun showCustomAlertDialog(isFromCurrency: Boolean) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)

        listview = dialog.findViewById(R.id.lst_codes)
        val codeAdapter: ListAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, Code.getCurrencyCodes(
                this
            )
        )
        listview.adapter = codeAdapter

        listview.setOnItemClickListener { _, _, myItemInt, _ ->
            if (isFromCurrency) {
                baseCurrency = listview.getItemAtPosition(myItemInt) as String
                img_currency_flag_from.setImageResource(getFlag(baseCurrency))
                txt_currency_code_from.text = baseCurrency
                txt_currency_name_from.text = getCodeName(baseCurrency)
            } else {
                convertedToCurrency = listview.getItemAtPosition(myItemInt) as String
                img_currency_flag_to.setImageResource(getFlag(convertedToCurrency))
                txt_currency_code_to.text = convertedToCurrency
                txt_currency_name_to.text = getCodeName(convertedToCurrency)
            }
            getRate(baseCurrency, convertedToCurrency)
            dialog.cancel()
        }
        dialog.show()
    }

    private fun getRate(from:String, to:String) {
        if (from == to) {
            txt_currency_rate_to.text = "???"
        } else {
            ratesViewModel.getSpecificExchangeRate(from, to).observe(this, {
                txt_currency_rate_to.text = String.format("%.4f", it.get(0).second)
            })
        }
    }
}