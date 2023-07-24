package com.marwaeltayeb.currencyexchange.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
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
import com.marwaeltayeb.currencyexchange.utils.showSnackBar
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

        loadRate(baseCurrency, convertedToCurrency)
        updateExchangeRateUi(baseCurrency, convertedToCurrency)
        loadAllRates(FROM_CURRENCY)

        binding.imgConvert.setOnClickListener(exChangeRateListener)
        binding.imgCurrencyFlagFrom.setOnClickListener(onImgFlagFromListener)
        binding.imgCurrencyFlagTo.setOnClickListener(onImgFlagToListener)
        binding.swipeToRefresh.setOnRefreshListener(onSwipeToRefreshListener)
    }

    private fun setUpObservers() {
        ratesViewModel.latestRatesLiveData.observe(this) {latestRatesList ->
            Timber.tag(TAG).d(latestRatesList.size.toString())
            ratesAdapter.submitList(latestRatesList)
        }

        ratesViewModel.exchangeRateLiveData.observe(this) {rate->
            Timber.tag(TAG).d("$baseCurrency to $convertedToCurrency = ${rate.get(0).second}")
            binding.txtCurrencyRateTo.text = String.format("%.4f", rate.get(0).second)
        }

        ratesViewModel.dataLoading.observe(this) { isDataLoaded ->
            if (isDataLoaded) binding.loadingIndicator.visibility = View.GONE
            else binding.loadingIndicator.visibility = View.VISIBLE
        }
    }

    private fun initViews(){
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rcRatesList.layoutManager = linearLayoutManager
        binding.rcRatesList.setHasFixedSize(true)

        ratesAdapter = RatesAdapter()
        binding.rcRatesList.adapter = ratesAdapter
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

    private fun loadRate(from:String, to:String) {
        if (from == to) binding.txtCurrencyRateTo.text = "???" else ratesViewModel.requestExchangeRate(from, to)
    }

    private fun loadAllRates(base: String) {
        ratesViewModel.requestLatestRates(base)
        binding.swipeToRefresh.isRefreshing = false
        if (!isNetworkAvailable(this)) {
            showSnackBar("Check Internet Connection")
            binding.loadingIndicator.visibility = View.GONE
        }
    }

    private fun toggleExchangeRate(){
        if(isSwitched) {
            loadRate(convertedToCurrency, baseCurrency)
            updateExchangeRateUi(convertedToCurrency, baseCurrency)
            loadAllRates(convertedToCurrency)
            isSwitched = false
        }else {
            loadRate(baseCurrency, convertedToCurrency)
            updateExchangeRateUi(baseCurrency, convertedToCurrency)
            loadAllRates(baseCurrency)
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

    private val exChangeRateListener = View.OnClickListener {
        toggleExchangeRate()
    }

    private val onImgFlagFromListener = View.OnClickListener {
        showCustomAlertDialog(this@MainActivity,object: DialogCallback{
            override fun onCallback(listView: ListView, item: Int) {
                loadRate(baseCurrency, convertedToCurrency)
                baseCurrency = listView.getItemAtPosition(item) as String
                binding.imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))
                binding.txtCurrencyNameFrom.text = getCodeName(baseCurrency)
                binding.txtCurrencyCodeFrom.text = baseCurrency
                loadAllRates(baseCurrency)
            }
        })
    }

    private val onImgFlagToListener = View.OnClickListener {
        showCustomAlertDialog(this@MainActivity,object: DialogCallback{
            override fun onCallback(listView: ListView, item: Int) {
                loadRate(baseCurrency, convertedToCurrency)
                convertedToCurrency = listView.getItemAtPosition(item) as String
                binding.imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))
                binding.txtCurrencyNameTo.text = getCodeName(convertedToCurrency)
                binding.txtCurrencyCodeTo.text = convertedToCurrency
            }
        })
    }

    private val onSwipeToRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        if (isNetworkAvailable(this)) {
            loadAllRates(baseCurrency)
        } else {
            showSnackBar("Check Internet Connection")
            binding.swipeToRefresh.isRefreshing = false
        }

        binding.swipeToRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.blue_dark),
            ContextCompat.getColor(this, R.color.orange_dark),
            ContextCompat.getColor(this, R.color.green_dark),
            ContextCompat.getColor(this, R.color.red_dark),
        )
    }
}