package com.marwaeltayeb.currencyexchange.ui.conversion

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.utils.Const

class ConvertActivity : AppCompatActivity() {

    private lateinit var txtCurrencyCodeFrom: TextView
    private lateinit var txtCurrencyRateTo: TextView
    private lateinit var txtCurrencyCodeTo: TextView
    private lateinit var etFirstConversion: EditText
    private lateinit var etSecondConversion: EditText

    private lateinit var covertViewModel: ConvertViewModel
    private var rate = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)

        txtCurrencyCodeFrom = findViewById(R.id.txt_currency_code_from)
        txtCurrencyRateTo = findViewById(R.id.txt_currency_rate_to)
        txtCurrencyCodeTo = findViewById(R.id.txt_currency_code_to)
        etFirstConversion = findViewById(R.id.et_firstConversion)
        etSecondConversion = findViewById(R.id.et_secondConversion)

        covertViewModel = ViewModelProvider(this).get(ConvertViewModel::class.java)

        txtCurrencyCodeFrom.text = Const.FROM_DOLLAR
        txtCurrencyCodeTo.text = Const.TO_EURO

        textChanged()
    }

    private fun getResult() {
        if (Const.FROM_DOLLAR == Const.TO_EURO) {
            Toast.makeText(
                applicationContext,
                "Please pick a currency to convert",
                Toast.LENGTH_SHORT
            ).show()
        } else {
            covertViewModel.exchangeRate(Const.FROM_DOLLAR, Const.TO_EURO).observe(this, {
                rate = it.get(0).second
                txtCurrencyRateTo.text = String.format("%.4f", it.get(0).second)
            })

            val text = ((etFirstConversion.text.toString().toDouble()) * rate).toString()
            etSecondConversion.setText(text)
        }
    }

    private fun textChanged() {
        etFirstConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                try {
                    getResult()
                } catch (e: Exception) {
                    Toast.makeText(applicationContext, "Type a value", Toast.LENGTH_SHORT).show()
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d("Main", "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d("Main", "OnTextChanged")
            }
        })
    }
}