package com.marwaeltayeb.currencyexchange.ui.main

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.marwaeltayeb.currencyexchange.R

private const val TAG = "MainActivity"

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var ratesAdapter: RatesAdapter
    private lateinit var ratesViewModel:RatesViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        ratesViewModel = ViewModelProvider(this).get(RatesViewModel::class.java)

        initViews()
        loadData()
    }

    private fun initViews(){
        recyclerView = findViewById(R.id.rc_rates_list)
        val linearLayoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.layoutManager = linearLayoutManager
        recyclerView.setHasFixedSize(true)

        ratesAdapter = RatesAdapter()
    }

    private fun loadData() {
        ratesViewModel.getAllRates().observe(this, {
            ratesAdapter.setRates(it)
        })

        recyclerView.adapter = ratesAdapter
        ratesAdapter.notifyDataSetChanged()
    }
}