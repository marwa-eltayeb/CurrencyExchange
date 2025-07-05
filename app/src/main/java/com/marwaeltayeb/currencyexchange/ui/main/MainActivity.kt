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
import androidx.appcompat.widget.SearchView
import com.marwaeltayeb.currencyexchange.databinding.ActivityMainBinding
import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertActivity
import com.marwaeltayeb.currencyexchange.utils.AnimationUtils.animateViewRotation
import com.marwaeltayeb.currencyexchange.utils.Constant.Companion.FROM_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.Constant.Companion.TO_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.CurrencyUtils.updateCurrencyUi
import com.marwaeltayeb.currencyexchange.utils.DialogCallback
import com.marwaeltayeb.currencyexchange.utils.DialogManager.Companion.showCustomAlertDialog
import com.marwaeltayeb.currencyexchange.utils.NetworkUtils.isNetworkAvailable
import com.marwaeltayeb.currencyexchange.utils.showSnackBar
import com.marwaeltayeb.currencyexchange.utils.showToast
import timber.log.Timber
import java.util.*
import javax.inject.Inject

private const val TAG = "tagi"

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

        binding.imgConvert.setOnClickListener(exchangeRateListener)
        binding.imgCurrencyFlagFrom.setOnClickListener(onImgFlagFromListener)
        binding.imgCurrencyFlagTo.setOnClickListener(onImgFlagToListener)
        binding.swipeToRefresh.setOnRefreshListener(onSwipeToRefreshListener)
    }

    private fun setUpObservers() {
        ratesViewModel.latestRatesLiveData.observe(this) {latestRatesList ->
            Timber.tag(TAG).d("Rates: ${latestRatesList.size} IsEmpty ${latestRatesList.isEmpty()}")
            if (latestRatesList.isEmpty()) {
                binding.lottiePic.setAnimation(R.raw.no_data)
                binding.lottiePic.visibility = View.VISIBLE
                binding.lottiePic.resumeAnimation()
                binding.rcRatesList.visibility = View.GONE
            } else {
                binding.lottiePic.visibility = View.GONE
                binding.lottiePic.pauseAnimation()
                binding.rcRatesList.visibility = View.VISIBLE
                ratesAdapter.submitList(latestRatesList)
            }
        }

        ratesViewModel.exchangeRateLiveData.observe(this) {rate->
            Timber.tag(TAG).d("Rate: $baseCurrency to $convertedToCurrency = ${rate.get(0).second}")
            binding.txtCurrencyRateTo.text = String.format(Locale.US,"%.4f", rate.get(0).second)
        }

        ratesViewModel.isLoading.observe(this) { isLoading ->
            if (isLoading) binding.loadingIndicator.visibility = View.VISIBLE
            else binding.loadingIndicator.visibility = View.GONE
        }

        ratesViewModel.errorMessage.observe(this) { errorMessage ->
            if (!isNetworkAvailable(this)) return@observe
            showToast(errorMessage)
            binding.lottiePic.setAnimation(R.raw.error_message)
            binding.lottiePic.visibility = View.VISIBLE
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
        // Update base currency UI
        updateCurrencyUi(
            baseCurrency,
            binding.imgCurrencyFlagFrom,
            binding.txtCurrencyCodeFrom,
            binding.txtCurrencyNameFrom,
            this
        )
        binding.txtCurrencyRateFrom.text = "1"

        // Update converted currency UI
        updateCurrencyUi(
            convertedToCurrency,
            binding.imgCurrencyFlagTo,
            binding.txtCurrencyCodeTo,
            binding.txtCurrencyNameTo,
            this
        )
    }

    private fun loadRate(from:String, to:String) {
        if (from == to) binding.txtCurrencyRateTo.text = "???" else ratesViewModel.requestExchangeRate(from, to)
    }

    private fun loadAllRates(base: String) {
        ratesViewModel.requestLatestRates(base)
        if (!isNetworkAvailable(this)) {
            showSnackBar("Check Internet Connection")
            binding.loadingIndicator.visibility = View.GONE
            binding.lottiePic.setAnimation(R.raw.no_internet)
            binding.lottiePic.visibility = View.VISIBLE
            binding.rcRatesList.visibility = View.GONE
        } else {
            binding.loadingIndicator.visibility = View.VISIBLE
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main, menu)

        val searchViewItem = menu.findItem(R.id.search)
        val searchView = searchViewItem.actionView as SearchView
        searchView.queryHint = resources.getString(R.string.search_for_currency)

        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                newText?.let {
                    ratesViewModel.filterCurrencies(it)
                }
                return true
            }
        })

        searchView.setOnCloseListener {
            ratesViewModel.filterCurrencies("")
            false
        }

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

    private val exchangeRateListener = View.OnClickListener {
        animateViewRotation(binding.imgConvert)
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

    private val onImgFlagFromListener = View.OnClickListener {
        showCustomAlertDialog(this@MainActivity,object: DialogCallback{
            override fun onCallback(listView: ListView, item: Int) {
                baseCurrency = listView.getItemAtPosition(item) as String
                updateCurrencyUi(
                    baseCurrency,
                    binding.imgCurrencyFlagFrom,
                    binding.txtCurrencyCodeFrom,
                    binding.txtCurrencyNameFrom,
                    this@MainActivity
                )
                loadRate(baseCurrency, convertedToCurrency)
                loadAllRates(baseCurrency)
            }
        })
    }

    private val onImgFlagToListener = View.OnClickListener {
        showCustomAlertDialog(this@MainActivity,object: DialogCallback{
            override fun onCallback(listView: ListView, item: Int) {
                convertedToCurrency = listView.getItemAtPosition(item) as String
                updateCurrencyUi(
                    convertedToCurrency,
                    binding.imgCurrencyFlagTo,
                    binding.txtCurrencyCodeTo,
                    binding.txtCurrencyNameTo,
                    this@MainActivity
                )
                loadRate(baseCurrency, convertedToCurrency)
            }
        })
    }

    private val onSwipeToRefreshListener = SwipeRefreshLayout.OnRefreshListener {
        if (isNetworkAvailable(this)) {
            loadAllRates(baseCurrency)
        } else {
            showSnackBar("Check Internet Connection")
        }
        binding.swipeToRefresh.isRefreshing = false

        binding.swipeToRefresh.setColorSchemeColors(
            ContextCompat.getColor(this, R.color.blue_dark),
            ContextCompat.getColor(this, R.color.orange_dark),
            ContextCompat.getColor(this, R.color.green_dark),
            ContextCompat.getColor(this, R.color.red_dark),
        )
    }
}