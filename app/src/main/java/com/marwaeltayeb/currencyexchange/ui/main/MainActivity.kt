package com.marwaeltayeb.currencyexchange.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.marwaeltayeb.currencyexchange.BaseApplication
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.databinding.ActivityMainBinding

import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertActivity
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.FROM_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.TO_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.DialogCallback
import com.marwaeltayeb.currencyexchange.utils.DialogManager.Companion.showCustomAlertDialog
import com.marwaeltayeb.currencyexchange.utils.NetworkUtils.isNetworkAvailable
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getCodeName
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var ratesAdapter: RatesAdapter

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val ratesViewModel by viewModels<RatesViewModel> { viewModelFactory }

    private var isSwitched = true

    private var baseCurrency = FROM_CURRENCY
    private var convertedToCurrency = TO_CURRENCY

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent.ratesComponent().create().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initViews()

        setUpObservers()

        loadAllRates(FROM_CURRENCY)

        loadDefaultRate()

        binding.imgConvert.setOnClickListener {
            exchangeRate()
        }

        binding.imgCurrencyFlagFrom.setOnClickListener(onImgFlagFromListener)
        binding.imgCurrencyFlagTo.setOnClickListener(onImgFlagToListener)
    }

    private fun setUpObservers() {
        ratesViewModel.getLatestRates().observe(this, {
            ratesAdapter.submitList(it)
            ratesAdapter.notifyDataSetChanged()
        })

        ratesViewModel.getExchangeRate().observe(this, {
            Timber.tag(TAG).d("$baseCurrency to $convertedToCurrency = ${it.get(0).second}")
            binding.txtCurrencyRateTo.text = String.format("%.4f", it.get(0).second)
        })

        ratesViewModel.dataLoading.observe(this, { isDataLoaded ->
           if(isDataLoaded){
               binding.loadingIndicator.visibility = View.VISIBLE
           }else{
               binding.loadingIndicator.visibility = View.INVISIBLE
           }
        })
    }

    private fun initViews(){
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcRatesList.layoutManager = linearLayoutManager
        binding.rcRatesList.setHasFixedSize(true)

        ratesAdapter = RatesAdapter()
        binding.rcRatesList.adapter = ratesAdapter
    }

    private fun loadAllRates(base: String) {
        if (!isNetworkAvailable(this)) {
            Toast.makeText(applicationContext, "Check Internet Connection", Toast.LENGTH_LONG).show()
        }else {
            ratesViewModel.requestLatestRates(base)
        }
    }

    private fun updateExchangeRateUi(baseCurrency:String, convertedToCurrency: String){
        binding.imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))
        binding.txtCurrencyNameFrom.text = getCodeName(baseCurrency)
        binding.txtCurrencyCodeFrom.text = baseCurrency
        binding.txtCurrencyRateFrom.text = "1"

        binding.imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))
        binding.txtCurrencyNameTo.text = getCodeName(convertedToCurrency)
        binding.txtCurrencyCodeTo.text = convertedToCurrency
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

    private fun getRate(from:String, to:String) {
        if (from == to) {
            binding.txtCurrencyRateTo.text = "???"
        } else {
            ratesViewModel.requestExchangeRate(from, to)
        }
    }

    private val onImgFlagFromListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            showCustomAlertDialog(this@MainActivity,object: DialogCallback{
                override fun onCallback(listView: ListView, item: Int) {
                    baseCurrency = listView.getItemAtPosition(item) as String
                    binding.imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))
                    binding.txtCurrencyCodeFrom.text = baseCurrency
                    binding.txtCurrencyNameFrom.text = getCodeName(baseCurrency)
                    getRate(baseCurrency, convertedToCurrency)
                }
            })
        }
    }

    private val onImgFlagToListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            showCustomAlertDialog(this@MainActivity,object: DialogCallback{
                override fun onCallback(listView: ListView, item: Int) {
                    convertedToCurrency = listView.getItemAtPosition(item) as String
                    binding.imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))
                    binding.txtCurrencyCodeTo.text = convertedToCurrency
                    binding.txtCurrencyNameTo.text = getCodeName(convertedToCurrency)
                    getRate(baseCurrency, convertedToCurrency)
                }
            })
        }
    }
}