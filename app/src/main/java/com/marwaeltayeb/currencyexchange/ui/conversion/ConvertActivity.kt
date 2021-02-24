package com.marwaeltayeb.currencyexchange.ui.conversion

import android.app.Dialog
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.Window
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.utils.Code.Companion.getCurrencyCodes
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.FROM_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.TO_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag
import com.marwaeltayeb.currencyexchange.utils.DateUtils.Companion.getEndDate
import com.marwaeltayeb.currencyexchange.utils.DateUtils.Companion.getStartDate

private const val TAG = "ConvertActivity"

class ConvertActivity : AppCompatActivity() {

    private lateinit var imgCurrencyFlagFrom: ImageView
    private lateinit var imgCurrencyFlagTo: ImageView
    private lateinit var txtCurrencyCodeFrom: TextView
    private lateinit var txtCurrencyRateTo: TextView
    private lateinit var txtCurrencyCodeTo: TextView
    private lateinit var edtFirstConversion: EditText
    private lateinit var edtSecondConversion: EditText

    private lateinit var covertViewModel: ConvertViewModel
    private var rate = 0.0

    private lateinit var listview: ListView

    private var baseCurrency = FROM_CURRENCY
    private var convertedToCurrency = TO_CURRENCY

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_convert)

        imgCurrencyFlagFrom = findViewById(R.id.img_currency_flag_from)
        imgCurrencyFlagTo = findViewById(R.id.img_currency_flag_to)
        txtCurrencyCodeFrom = findViewById(R.id.txt_currency_code_from)
        txtCurrencyRateTo = findViewById(R.id.txt_currency_rate_to)
        txtCurrencyCodeTo = findViewById(R.id.txt_currency_code_to)
        edtFirstConversion = findViewById(R.id.edt_firstConversion)
        edtSecondConversion = findViewById(R.id.edt_secondConversion)

        covertViewModel = ViewModelProvider(this).get(ConvertViewModel::class.java)

        txtCurrencyCodeFrom.text = baseCurrency
        imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))

        txtCurrencyCodeTo.text = convertedToCurrency
        imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))

        getRate()

        setupTextWatcher()

        imgCurrencyFlagFrom.setOnClickListener {
            showCustomAlertDialog(true)
        }

        imgCurrencyFlagTo.setOnClickListener {
            showCustomAlertDialog(false)
        }

        getHistoricalRates()
    }

    private fun getResult() {
        if (baseCurrency == convertedToCurrency) {
            Toast.makeText(this, "Please pick a currency to convert", Toast.LENGTH_SHORT).show()
            txtCurrencyRateTo.text = "???"
        } else {
            getRate()

            val text = ((edtFirstConversion.text.toString().toDouble()) * rate).toString()
            edtSecondConversion.setText(text)
        }
    }

    private fun setupTextWatcher() {
        edtFirstConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (edtFirstConversion.text.isBlank()) {
                    edtSecondConversion.setText("")
                } else {
                    try {
                        getResult()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Type a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Log.d(TAG, "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Log.d(TAG, "OnTextChanged")
            }
        })
    }

    private fun showCustomAlertDialog(isFromCurrency: Boolean) {
        val dialog = Dialog(this)
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE)
        dialog.setContentView(R.layout.custom_dialog)

        listview = dialog.findViewById(R.id.lst_codes)
        val codeAdapter: ListAdapter = ArrayAdapter<String>(
            this, android.R.layout.simple_list_item_1, getCurrencyCodes(
                this
            )
        )
        listview.adapter = codeAdapter

        listview.setOnItemClickListener { _, _, myItemInt, _ ->
            if (isFromCurrency) {
                baseCurrency = listview.getItemAtPosition(myItemInt) as String
                imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))
                txtCurrencyCodeFrom.text = baseCurrency
                getRate()
                getHistoricalRates()
            } else {
                convertedToCurrency = listview.getItemAtPosition(myItemInt) as String
                imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))
                txtCurrencyCodeTo.text = convertedToCurrency
                Log.d("Before", convertedToCurrency)
                getRate()
                getHistoricalRates()
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


    private fun getHistoricalRates() {
        covertViewModel.getHistoricalRates(getStartDate(), getEndDate(), baseCurrency, convertedToCurrency).observe(this, { data ->

            val response = data.rates.toSortedMap()
            val listOfRates = arrayListOf<Entry>()

            repeat(5) { i -> listOfRates.add(Entry(i.toFloat(), response.values.elementAt(i)[convertedToCurrency]!!.toFloat())) }

            val dates = arrayListOf<String>()
            repeat(5) { i -> dates.add(response.keys.elementAt(i)) }

            setLineChart(dates, listOfRates)
        })
    }

    private fun setLineChart(dates: List<String>, listOfRates: ArrayList<Entry>) {
        val chart: LineChart = findViewById(R.id.chart)
        chart.setDragEnabled(true)
        chart.setScaleEnabled(false)

        val lineDataSet = LineDataSet(listOfRates, getString(R.string.currency_rates))
        lineDataSet.fillAlpha = 110
        lineDataSet.color = Color.RED
        lineDataSet.valueTextSize = 8f
        lineDataSet.valueTextColor = Color.GREEN
        lineDataSet.mode = LineDataSet.Mode.CUBIC_BEZIER
        lineDataSet.setDrawFilled(true)
        lineDataSet.fillColor = ContextCompat.getColor(this, R.color.colorPrimaryVariant)

        val dataSets = arrayListOf<ILineDataSet>()
        dataSets.add(lineDataSet)

        val xAxis = chart.xAxis
        xAxis.valueFormatter = XAxisValueFormatter(dates.toList())
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 5
        xAxis.labelRotationAngle = -45f

        val lineData = LineData(dataSets)
        lineData.setDrawValues(true)
        chart.data = lineData
        chart.invalidate()

        // Hide Left Axis
        chart.axisLeft.textColor = ContextCompat.getColor(this, R.color.white)
        // Change Label Text Color
        chart.legend.textColor = Color.GREEN
        // Make Right Axis 5 labels
        chart.axisRight.labelCount = 5
    }

    class XAxisValueFormatter(private val values: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            Log.d(TAG, "getFormattedValue: Index ${value}  -> ${values.elementAt(value.toInt())}")
            return values[value.toInt()]
        }
    }
}

