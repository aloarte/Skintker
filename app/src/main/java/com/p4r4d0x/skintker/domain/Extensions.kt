package com.p4r4d0x.skintker.domain

fun Map<String, Int>.getMaxValue(): String? {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.first
}

fun Map<Int, Int>.getKeyOfMaxValue(): Int {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.second ?: 0
}

fun Map<Int, Int>.getMaxValue(): Int {
    return this.toSortedMap().toList().reversed().maxByOrNull { (_, value) -> value }?.first ?: 0
}

fun MutableMap<String, Int>.updateValue(key: String) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<Int, Int>.updateValue(key: Int) {
    this[key] = (this[key] ?: 0) + 1
}

fun MutableMap<Boolean, Int>.updateValue(key: Boolean) {
    this[key] = (this[key] ?: 0) + 1
}