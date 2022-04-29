package com.p4r4d0x.skintker.utils

import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.getAlcoholLevel
import com.p4r4d0x.skintker.getHumidityString
import com.p4r4d0x.skintker.getTemperatureString
import org.junit.Assert
import org.junit.Test

class UtilsTest {

    @Test
    fun `get humidity`() {
        Assert.assertEquals(R.string.humidity_3, getHumidityString(-1))
        Assert.assertEquals(R.string.humidity_1, getHumidityString(0))
        Assert.assertEquals(R.string.humidity_1, getHumidityString(1))
        Assert.assertEquals(R.string.humidity_2, getHumidityString(2))
        Assert.assertEquals(R.string.humidity_2, getHumidityString(3))
        Assert.assertEquals(R.string.humidity_3, getHumidityString(4))
        Assert.assertEquals(R.string.humidity_3, getHumidityString(5))
        Assert.assertEquals(R.string.humidity_3, getHumidityString(6))
        Assert.assertEquals(R.string.humidity_4, getHumidityString(7))
        Assert.assertEquals(R.string.humidity_4, getHumidityString(8))
        Assert.assertEquals(R.string.humidity_5, getHumidityString(9))
        Assert.assertEquals(R.string.humidity_5, getHumidityString(10))
    }

    @Test
    fun `get temperature`() {
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(-1))
        Assert.assertEquals(R.string.temperature_1, getTemperatureString(0))
        Assert.assertEquals(R.string.temperature_1, getTemperatureString(1))
        Assert.assertEquals(R.string.temperature_2, getTemperatureString(2))
        Assert.assertEquals(R.string.temperature_2, getTemperatureString(3))
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(4))
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(5))
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(6))
        Assert.assertEquals(R.string.temperature_4, getTemperatureString(7))
        Assert.assertEquals(R.string.temperature_4, getTemperatureString(8))
        Assert.assertEquals(R.string.temperature_5, getTemperatureString(9))
        Assert.assertEquals(R.string.temperature_5, getTemperatureString(10))
    }

    @Test
    fun `get alcohol level`() {
        Assert.assertEquals(R.string.card_no_alcohol, getAlcoholLevel(-1))
        Assert.assertEquals(R.string.card_no_alcohol, getAlcoholLevel(0))
        Assert.assertEquals(R.string.card_any_alcohol_beer, getAlcoholLevel(1))
        Assert.assertEquals(R.string.card_any_alcohol_wine, getAlcoholLevel(2))
        Assert.assertEquals(R.string.card_quite_alcohol, getAlcoholLevel(3))
    }
}