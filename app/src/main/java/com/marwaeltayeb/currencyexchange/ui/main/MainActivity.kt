package com.marwaeltayeb.currencyexchange.ui.main

import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.currencyexchange.R

import com.marwaeltayeb.currencyexchange.ui.conversion.ConvertActivity
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.FROM_DOLLAR
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.FROM_EURO
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.TO_DOLLAR
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.TO_EURO
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getCodeName
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag

private const val TAG = "MainActivity"

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

    private var isConverted = false
    private var valueOne = ""
    private var valueTwo = ""


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()

        ratesViewModel = ViewModelProvider(this).get(RatesViewModel::class.java)

        loadAllRates()
        loadSpecificRate()

        img_convert.setOnClickListener {
            exchangeRate()
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
    }

    private fun loadAllRates() {
        ratesViewModel.getAllRates().observe(this, {
            ratesAdapter.setRates(it)
        })

        recyclerView.adapter = ratesAdapter
        ratesAdapter.notifyDataSetChanged()
    }

    private fun loadSpecificRate() {
        ratesViewModel.getSpecificExchangeRate(FROM_DOLLAR, TO_EURO).observe(this, {
            valueOne = it.get(0).second.toString()
            txt_currency_rate_to.text = valueOne
        })

        img_currency_flag_from.setBackgroundResource(getFlag(FROM_DOLLAR))
        img_currency_flag_to.setBackgroundResource(getFlag(TO_EURO))
        txt_currency_name_from.text = getCodeName(FROM_DOLLAR)
        txt_currency_name_to.text = getCodeName(TO_EURO)

        txt_currency_rate_from.text = "1"
        txt_currency_code_from.text = FROM_DOLLAR
        txt_currency_code_to.text = TO_EURO

    }

    private fun exchangeRate(){
        if(!isConverted) {

            ratesViewModel.getSpecificExchangeRate(FROM_EURO, TO_DOLLAR).observe(this, {
                valueTwo = it.get(0).second.toString()
                txt_currency_rate_to.text = valueTwo
            })

            img_currency_flag_from.setBackgroundResource(getFlag(TO_EURO))
            img_currency_flag_to.setBackgroundResource(getFlag(FROM_DOLLAR))
            txt_currency_name_from.text = getCodeName(TO_EURO)
            txt_currency_name_to.text = getCodeName(FROM_DOLLAR)
            txt_currency_rate_from.text = "1"
            txt_currency_code_from.text = TO_EURO
            txt_currency_code_to.text = FROM_DOLLAR

            isConverted = true
        }else {

            ratesViewModel.getSpecificExchangeRate(FROM_DOLLAR, TO_EURO).observe(this, {
                valueOne = it.get(0).second.toString()
                txt_currency_rate_to.text = valueOne
            })

            img_currency_flag_from.setBackgroundResource(getFlag(FROM_DOLLAR))
            img_currency_flag_to.setBackgroundResource(getFlag(TO_EURO))
            txt_currency_name_from.text = getCodeName(FROM_DOLLAR)
            txt_currency_name_to.text = getCodeName(TO_EURO)
            txt_currency_rate_from.text = "1"
            txt_currency_code_from.text = FROM_DOLLAR
            txt_currency_code_to.text = TO_EURO

            isConverted = false
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
}