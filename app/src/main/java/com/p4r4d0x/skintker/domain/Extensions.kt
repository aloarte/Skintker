package com.p4r4d0x.skintker.domain

import android.annotation.SuppressLint
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.Constants.REGEX_UNACCENT
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import java.text.Normalizer
import java.text.SimpleDateFormat
import java.util.*

fun Map<String, Int>.getMaxValue(): String? {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.first
}

fun Map<Int, Int>.getKeyOfMaxValue(): Int {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.second ?: 0
}

fun Map<Int, Int>.getMaxValue(): Int {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.first ?: 0
}

fun MutableMap<String, Int>.increaseValue(key: String) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<Int, Int>.increaseValue(key: Int) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<Boolean, Int>.increaseValue(key: Boolean) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<AlcoholLevel, Int>.increaseValue(key: AlcoholLevel) {
    this[key] = (this[key] ?: 0) + 1
}

fun String.cleanString(): String {
    val normalized = Normalizer.normalize(this, Normalizer.Form.NFD)
    return REGEX_UNACCENT.toRegex().replace(normalized, "").replace(
        Constants.CHARACTER_FILTER_REGEX.toRegex(),
        ""
    )
}

@SuppressLint("SimpleDateFormat")
fun Date.getDDMMYYYYDate(): String {
    val dfDate = SimpleDateFormat("dd/MM/yyyy")
    return dfDate.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.getDayDate(): String {
    val dfDay = SimpleDateFormat("EEEE")
    return dfDay.format(this)
}

@SuppressLint("SimpleDateFormat")
fun Date.getDateWithoutTime(): Date {
    val formatter = SimpleDateFormat(
        "dd/MM/yyyy"
    )
    return formatter.parse(formatter.format(this)) ?: Date()
}

