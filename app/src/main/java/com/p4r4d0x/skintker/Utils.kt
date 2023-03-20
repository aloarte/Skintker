package com.p4r4d0x.skintker

fun getHumidityString(value: Int) =
    when (value) {
        1 -> R.string.humidity_1
        2 -> R.string.humidity_2
        3 -> R.string.humidity_3
        4 -> R.string.humidity_4
        5 -> R.string.humidity_5
        else -> R.string.humidity_3
    }

fun getTemperatureString(value: Int) =
    when (value) {
        1 -> R.string.temperature_1
        2 -> R.string.temperature_2
        3 -> R.string.temperature_3
        4 -> R.string.temperature_4
        5 -> R.string.temperature_5
        else -> R.string.temperature_3
    }

fun getAlcoholLevel(alcoholLevel: Int): Int =
    when (alcoholLevel) {
        0 -> R.string.card_no_alcohol
        1 -> R.string.card_any_alcohol_beer
        2 -> R.string.card_any_alcohol_wine
        3 -> R.string.card_quite_alcohol
        else -> R.string.card_no_alcohol
    }