package com.marwaeltayeb.currencyexchange.ui.conversion

import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.*
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.ValueFormatter
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.marwaeltayeb.currencyexchange.BaseApplication
import com.marwaeltayeb.currencyexchange.R
import com.marwaeltayeb.currencyexchange.databinding.ActivityConvertBinding
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.FROM_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.Const.Companion.TO_CURRENCY
import com.marwaeltayeb.currencyexchange.utils.RateUtils.Companion.getFlag
import com.marwaeltayeb.currencyexchange.utils.DateUtils.Companion.getEndDate
import com.marwaeltayeb.currencyexchange.utils.DateUtils.Companion.getStartDate
import com.marwaeltayeb.currencyexchange.utils.DialogCallback
import com.marwaeltayeb.currencyexchange.utils.DialogManager.Companion.showCustomAlertDialog
import timber.log.Timber
import javax.inject.Inject

private const val TAG = "ConvertActivity"

class ConvertActivity : AppCompatActivity() {

    private lateinit var binding: ActivityConvertBinding

    @Inject
    lateinit var viewModelFactory: ViewModelProvider.Factory
    private val covertViewModel by viewModels<ConvertViewModel> { viewModelFactory }

    private var rate = 0.0

    private var baseCurrency = FROM_CURRENCY
    private var convertedToCurrency = TO_CURRENCY

    override fun onCreate(savedInstanceState: Bundle?) {
        (application as BaseApplication).appComponent.convertComponent().create().inject(this)
        super.onCreate(savedInstanceState)
        binding = ActivityConvertBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.txtCurrencyCodeFrom.text = baseCurrency
        binding.imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))

        binding.txtCurrencyCodeTo.text = convertedToCurrency
        binding.imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))

        setUpObservers()

        getRate()

        setupTextWatcher()

        getHistoricalRates()

        binding.imgCurrencyFlagFrom.setOnClickListener(imgCurrencyFlagFromListener)
        binding.imgCurrencyFlagTo.setOnClickListener(imgCurrencyFlagToListener)
    }

    private fun setUpObservers() {
        covertViewModel.getExchangeRate().observe(this, {
            Timber.tag(TAG).d( "$baseCurrency to $convertedToCurrency = ${it.get(0).second}")
            rate = it.get(0).second
            binding.txtCurrencyRateTo.text = String.format("%.4f", it.get(0).second)
        })

        covertViewModel.getHistoricalRates().observe(this, { data ->
            val response = data.rates.toSortedMap()
            Timber.tag(TAG).d( "getHistoricalRates: {${convertedToCurrency}} + ${response.values} ? ")

            val listOfRates = arrayListOf<Entry>()

            repeat(5){ i ->
                listOfRates.add(Entry(i.toFloat(), response.values.elementAt(i)[convertedToCurrency]!!.toFloat()))
            }

            val dates = arrayListOf<String>()
            repeat(5) { i ->
                dates.add(response.keys.elementAt(i))
            }

            setLineChart(dates, listOfRates)
        })
    }

    private fun setupTextWatcher() {
        binding.edtFirstConversion.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
                if (binding.edtFirstConversion.text.isBlank()) {
                    binding.edtSecondConversion.setText("")
                } else {
                    try {
                        getResult()
                    } catch (e: Exception) {
                        Toast.makeText(applicationContext, "Type a value", Toast.LENGTH_SHORT).show()
                    }
                }
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                Timber.tag(TAG).d( "Before Text Changed")
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                Timber.tag(TAG).d( "OnTextChanged")
            }
        })
    }

    private fun getResult() {
        if (baseCurrency == convertedToCurrency) {
            Toast.makeText(this, "Please pick a currency to convert", Toast.LENGTH_SHORT).show()
            binding.txtCurrencyRateTo.text = "???"
        } else {
            getRate()

            val text = String.format("%.4f",((binding.edtFirstConversion.text.toString().toDouble()) * rate))
            binding.edtSecondConversion.setText(text)
        }
    }

    private fun getRate() {
        if (baseCurrency == convertedToCurrency) {
            binding.txtCurrencyRateTo.text = "???"
        } else {
            covertViewModel.requestExchangeRate(baseCurrency, convertedToCurrency)
        }
    }

    private fun getHistoricalRates() {
        covertViewModel.requestHistoricalRates(getStartDate(),getEndDate(),baseCurrency, convertedToCurrency)
    }

    private fun setLineChart(dates: List<String>, listOfRates: ArrayList<Entry>) {
        binding.lineChart.setDragEnabled(true)
        binding.lineChart.setScaleEnabled(false)

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

        val xAxis = binding.lineChart.xAxis
        xAxis.valueFormatter = XAxisValueFormatter(dates.toList())
        xAxis.granularity = 1f
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.labelCount = 5
        xAxis.labelRotationAngle = -45f

        val lineData = LineData(dataSets)
        lineData.setDrawValues(true)
        binding.lineChart.data = lineData
        binding.lineChart.invalidate()

        // Hide Left Axis
        binding.lineChart.axisLeft.textColor = ContextCompat.getColor(this, R.color.white)
        // Change Label Text Color
        binding.lineChart.legend.textColor = Color.GREEN
        // Make Right Axis 5 labels
        binding.lineChart.axisRight.labelCount = 5
    }

    class XAxisValueFormatter(private val values: List<String>) : ValueFormatter() {
        override fun getFormattedValue(value: Float): String {
            Timber.tag(TAG).d( "getFormattedValue: Index ${value}  -> ${values.elementAt(value.toInt())}")
            return values[value.toInt()]
        }
    }

    private val imgCurrencyFlagFromListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            showCustomAlertDialog(this@ConvertActivity, object: DialogCallback{
                override fun onCallback(listView: ListView, item: Int) {
                    baseCurrency = listView.getItemAtPosition(item) as String
                    binding.imgCurrencyFlagFrom.setImageResource(getFlag(baseCurrency))
                    binding.txtCurrencyCodeFrom.text = baseCurrency
                    getRate()
                    getHistoricalRates()
                }
            })
        }
    }

    private val imgCurrencyFlagToListener = object : View.OnClickListener {
        override fun onClick(v: View?) {
            showCustomAlertDialog(this@ConvertActivity, object: DialogCallback{
                override fun onCallback(listView: ListView, item: Int) {
                    convertedToCurrency = listView.getItemAtPosition(item) as String
                    binding.imgCurrencyFlagTo.setImageResource(getFlag(convertedToCurrency))
                    binding.txtCurrencyCodeTo.text = convertedToCurrency
                    getRate()
                    getHistoricalRates()
                }
            })
        }
    }
}

