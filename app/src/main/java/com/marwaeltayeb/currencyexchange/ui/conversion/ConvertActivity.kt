package com.marwaeltayeb.currencyexchange.ui.conversion

import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.utils.Code.Companion.getCurrencyCodes
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.FROM
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.TO
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag

class ConvertActivity : AppCompatActivity() {

    private lateinit var imgCurrencyFlagFrom: ImageView
    private lateinit var imgCurrencyFlagTo: ImageView
    private lateinit var txtCurrencyCodeFrom: TextView
    private lateinit var txtCurrencyRateTo: TextView
    private lateinit var txtCurrencyCodeTo: TextView
    private lateinit var etFirstConversion: EditText
    private lateinit var etSecondConversion: EditText

    private lateinit var covertViewModel: ConvertViewModel
    private var rate = 0.0

    private lateinit var listview: ListView

    var baseCurrency = FROM
    var convertedToCurrency = TO

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)

        imgCurrencyFlagFrom = findViewById(R.id.img_currency_flag_from)
        imgCurrencyFlagTo = findViewById(R.id.img_currency_flag_to)
        txtCurrencyCodeFrom = findViewById(R.id.txt_currency_code_from)
        txtCurrencyRateTo = findViewById(R.id.txt_currency_rate_to)
        txtCurrencyCodeTo = findViewById(R.id.txt_currency_code_to)
        etFirstConversion = findViewById(R.id.et_firstConversion)
        etSecondConversion = findViewById(R.id.et_secondConversion)

        covertViewModel = ViewModelProvider(this).get(ConvertViewModel::class.java)

        txtCurrencyCodeFrom.text = baseCurrency
        imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))

        txtCurrencyCodeTo.text = convertedToCurrency
        imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))

        getRate()

        textChanged()

        imgCurrencyFlagFrom.setOnClickListener {
            showCustomAlertDialog(true)
        }

        imgCurrencyFlagTo.setOnClickListener {
            showCustomAlertDialog(false)
        }
    }

    private fun getResult() {
        if (baseCurrency == convertedToCurrency) {
            Toast.makeText(
                applicationContext,
                "Please pick a currency to convert",
                Toast.LENGTH_SHORT
            ).show()
            txtCurrencyRateTo.text = "???"
        } else {
            getRate()

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

    private fun showCustomAlertDialog(isFromCurrency: Boolean) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)

        listview = dialog.findViewById(R.id.lst_codes)
        val adapter: ListAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, getCurrencyCodes(
                this
            )
        )
        listview.setAdapter(adapter)

        listview.setOnItemClickListener { myAdapter, myView, myItemInt, mylng ->
            if (isFromCurrency) {
                baseCurrency = listview.getItemAtPosition(myItemInt) as String
                imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))
                txtCurrencyCodeFrom.text = baseCurrency
                getRate()
            } else {
                convertedToCurrency = listview.getItemAtPosition(myItemInt) as String
                imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))
                txtCurrencyCodeTo.text = convertedToCurrency
                getRate()
            }
            dialog.cancel()
        }
        dialog.show()
    }


    private fun getRate() {
        if (baseCurrency == convertedToCurrency) {
            txtCurrencyRateTo.text = "???"
        } else {
            covertViewModel.exchangeRate(baseCurrency, convertedToCurrency).observe(this, {
                rate = it.get(0).second
                txtCurrencyRateTo.text = String.format("%.4f", it.get(0).second)
            })
        }
    }
}