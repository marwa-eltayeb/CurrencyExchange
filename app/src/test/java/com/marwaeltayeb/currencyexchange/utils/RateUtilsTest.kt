package com.marwaeltayeb.currencyexchange.utils

import com.google.common.truth.Truth.assertThat
import com.marwaeltayeb.currencyexchange.R
import org.junit.Test

class RateUtilsTest{

    @Test
    fun currencyCodeName_ReturnsValidName() {
        val currencyCode = "USD"
        val currencyName = "US Dollar"
        val result = RateUtils.getCodeName(currencyCode)
        assertThat(result).isEqualTo(currencyName)
    }

    @Test
    fun currencyCodeName_ReturnsNone() {
        val currencyCode = "DZD"
        val currencyName = "None"
        val result = RateUtils.getCodeName(currencyCode)
        assertThat(result).isEqualTo(currencyName)
    }

    @Test
    fun currencyFlag_ReturnsValidFlag() {
        val currencyCode = "USD"
        val currencyFlag = R.drawable.flag_usd
        val result = RateUtils.getFlag(currencyCode)
        assertThat(result).isEqualTo(currencyFlag)
    }

    @Test
    fun currencyFlag_ReturnsNone() {
        val currencyCode = "DZD"
        val currencyFlag = R.drawable.ic_money
        val result = RateUtils.getFlag(currencyCode)
        assertThat(result).isEqualTo(currencyFlag)
    }
}