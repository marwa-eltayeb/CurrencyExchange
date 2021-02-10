package com.marwaeltayeb.currencyexchange.utils

import android.content.Context
import com.marwaeltayeb.currencyexchange.R

class Code {

    companion object {

        fun getCurrencyCodes(context: Context): ArrayList<String> {
            val codes: ArrayList<String> = ArrayList<String>()
            codes.add(context.getString(R.string.cad))
            codes.add(context.getString(R.string.hkd))
            codes.add(context.getString(R.string.isk))
            codes.add(context.getString(R.string.php))
            codes.add(context.getString(R.string.dkk))
            codes.add(context.getString(R.string.huf))
            codes.add(context.getString(R.string.czk))
            codes.add(context.getString(R.string.aud))
            codes.add(context.getString(R.string.ron))
            codes.add(context.getString(R.string.sek))
            codes.add(context.getString(R.string.idr))
            codes.add(context.getString(R.string.inr))
            codes.add(context.getString(R.string.brl))
            codes.add(context.getString(R.string.rub))
            codes.add(context.getString(R.string.hrk))
            codes.add(context.getString(R.string.jpy))
            codes.add(context.getString(R.string.thb))
            codes.add(context.getString(R.string.chf))
            codes.add(context.getString(R.string.sgd))
            codes.add(context.getString(R.string.pln))
            codes.add(context.getString(R.string.bgn))
            codes.add(context.getString(R.string.trry))
            codes.add(context.getString(R.string.cny))
            codes.add(context.getString(R.string.nok))
            codes.add(context.getString(R.string.nzd))
            codes.add(context.getString(R.string.zar))
            codes.add(context.getString(R.string.usd))
            codes.add(context.getString(R.string.mxn))
            codes.add(context.getString(R.string.ils))
            codes.add(context.getString(R.string.gbp))
            codes.add(context.getString(R.string.krw))
            codes.add(context.getString(R.string.eur))
            return codes;
        }
    }
}