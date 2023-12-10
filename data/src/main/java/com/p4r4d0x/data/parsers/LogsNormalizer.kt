package com.p4r4d0x.data.parsers

import android.content.res.Resources
import com.example.data.R
import com.p4r4d0x.domain.bo.DailyLogBO

class LogsNormalizer(private val resources: Resources) {


    fun denormalizeLog(log: DailyLogBO): DailyLogBO {
        return log.copy(
            foodList = log.foodList.map(::denormalizeFood),
            irritation = log.irritation.copy(zoneValues = log.irritation.zoneValues.map(::denormalizeZones)),
            additionalData = log.additionalData.copy(beerTypes = log.additionalData.beerTypes.map(::denormalizeBeers))
        )
    }

    fun normalizeLog(log: DailyLogBO): DailyLogBO {
        return log.copy(
            foodList = log.foodList.map(::normalizeFood),
            irritation = log.irritation.copy(zoneValues = log.irritation.zoneValues.map(::normalizeZones)),
            additionalData = log.additionalData.copy(beerTypes = log.additionalData.beerTypes.map(::normalizeBeers))
        )
    }

    fun normalizeBeers(beerType: String) = when (beerType) {
        resources.getString(R.string.question_5_answer_1) -> resources.getString(R.string.normalized_question_5_answer_1)
        resources.getString(R.string.question_5_answer_2) -> resources.getString(R.string.normalized_question_5_answer_2)
        resources.getString(R.string.question_5_answer_3) -> resources.getString(R.string.normalized_question_5_answer_3)
        resources.getString(R.string.question_5_answer_4) -> resources.getString(R.string.normalized_question_5_answer_4)
        resources.getString(R.string.question_5_answer_5) -> resources.getString(R.string.normalized_question_5_answer_5)
        resources.getString(R.string.question_5_answer_6) -> resources.getString(R.string.normalized_question_5_answer_6)
        resources.getString(R.string.question_5_answer_7) -> resources.getString(R.string.normalized_question_5_answer_7)
        resources.getString(R.string.question_5_answer_8) -> resources.getString(R.string.normalized_question_5_answer_8)
        resources.getString(R.string.question_5_answer_9) -> resources.getString(R.string.normalized_question_5_answer_9)
        else -> throw IllegalArgumentException("Tipo de cerveza no reconocido: $beerType")
    }


    fun normalizeZones(zone: String) = when (zone) {
        resources.getString(R.string.question_2_answer_1) -> resources.getString(R.string.normalized_question_2_answer_1)
        resources.getString(R.string.question_2_answer_2) -> resources.getString(R.string.normalized_question_2_answer_2)
        resources.getString(R.string.question_2_answer_3) -> resources.getString(R.string.normalized_question_2_answer_3)
        resources.getString(R.string.question_2_answer_4) -> resources.getString(R.string.normalized_question_2_answer_4)
        resources.getString(R.string.question_2_answer_5) -> resources.getString(R.string.normalized_question_2_answer_5)
        resources.getString(R.string.question_2_answer_6) -> resources.getString(R.string.normalized_question_2_answer_6)
        resources.getString(R.string.question_2_answer_7) -> resources.getString(R.string.normalized_question_2_answer_7)
        resources.getString(R.string.question_2_answer_8) -> resources.getString(R.string.normalized_question_2_answer_8)
        resources.getString(R.string.question_2_answer_9) -> resources.getString(R.string.normalized_question_2_answer_9)
        resources.getString(R.string.question_2_answer_10) -> resources.getString(R.string.normalized_question_2_answer_10)
        resources.getString(R.string.question_2_answer_11) -> resources.getString(R.string.normalized_question_2_answer_11)
        resources.getString(R.string.question_2_answer_12) -> resources.getString(R.string.normalized_question_2_answer_12)
        else -> throw IllegalArgumentException("Zone not recognized: $zone")
    }


    fun normalizeFood(food: String) = when (food) {
        resources.getString(R.string.question_8_answer_1) -> resources.getString(R.string.normalized_question_8_answer_1)
        resources.getString(R.string.question_8_answer_2) -> resources.getString(R.string.normalized_question_8_answer_2)
        resources.getString(R.string.question_8_answer_3) -> resources.getString(R.string.normalized_question_8_answer_3)
        resources.getString(R.string.question_8_answer_4) -> resources.getString(R.string.normalized_question_8_answer_4)
        resources.getString(R.string.question_8_answer_5) -> resources.getString(R.string.normalized_question_8_answer_5)
        resources.getString(R.string.question_8_answer_6) -> resources.getString(R.string.normalized_question_8_answer_6)
        resources.getString(R.string.question_8_answer_7) -> resources.getString(R.string.normalized_question_8_answer_7)
        resources.getString(R.string.question_8_answer_8) -> resources.getString(R.string.normalized_question_8_answer_8)
        resources.getString(R.string.question_8_answer_9) -> resources.getString(R.string.normalized_question_8_answer_9)
        resources.getString(R.string.question_8_answer_10) -> resources.getString(R.string.normalized_question_8_answer_10)
        resources.getString(R.string.question_8_answer_11) -> resources.getString(R.string.normalized_question_8_answer_11)
        resources.getString(R.string.question_8_answer_12) -> resources.getString(R.string.normalized_question_8_answer_12)
        resources.getString(R.string.question_8_answer_13) -> resources.getString(R.string.normalized_question_8_answer_13)
        resources.getString(R.string.question_8_answer_14) -> resources.getString(R.string.normalized_question_8_answer_14)
        resources.getString(R.string.question_8_answer_15) -> resources.getString(R.string.normalized_question_8_answer_15)
        resources.getString(R.string.question_8_answer_16) -> resources.getString(R.string.normalized_question_8_answer_16)
        resources.getString(R.string.question_8_answer_17) -> resources.getString(R.string.normalized_question_8_answer_17)
        resources.getString(R.string.question_8_answer_18) -> resources.getString(R.string.normalized_question_8_answer_18)
        resources.getString(R.string.question_9_answer_1) -> resources.getString(R.string.normalized_question_9_answer_1)
        resources.getString(R.string.question_9_answer_2) -> resources.getString(R.string.normalized_question_9_answer_2)
        resources.getString(R.string.question_9_answer_3) -> resources.getString(R.string.normalized_question_9_answer_3)
        resources.getString(R.string.question_9_answer_4) -> resources.getString(R.string.normalized_question_9_answer_4)
        resources.getString(R.string.question_9_answer_5) -> resources.getString(R.string.normalized_question_9_answer_5)
        resources.getString(R.string.question_9_answer_6) -> resources.getString(R.string.normalized_question_9_answer_6)
        resources.getString(R.string.question_9_answer_7) -> resources.getString(R.string.normalized_question_9_answer_7)
        resources.getString(R.string.question_9_answer_8) -> resources.getString(R.string.normalized_question_9_answer_8)
        resources.getString(R.string.question_9_answer_9) -> resources.getString(R.string.normalized_question_9_answer_9)
        resources.getString(R.string.question_9_answer_10) -> resources.getString(R.string.normalized_question_9_answer_10)
        resources.getString(R.string.question_9_answer_11) -> resources.getString(R.string.normalized_question_9_answer_11)
        resources.getString(R.string.question_9_answer_12) -> resources.getString(R.string.normalized_question_9_answer_12)
        resources.getString(R.string.question_9_answer_13) -> resources.getString(R.string.normalized_question_9_answer_13)
        else -> throw IllegalArgumentException("food not recognized:  $food")
    }

    fun denormalizeBeers(normalizedBeerType: String) = when (normalizedBeerType) {
        resources.getString(R.string.normalized_question_5_answer_1) -> resources.getString(R.string.question_5_answer_1)
        resources.getString(R.string.normalized_question_5_answer_2) -> resources.getString(R.string.question_5_answer_2)
        resources.getString(R.string.normalized_question_5_answer_3) -> resources.getString(R.string.question_5_answer_3)
        resources.getString(R.string.normalized_question_5_answer_4) -> resources.getString(R.string.question_5_answer_4)
        resources.getString(R.string.normalized_question_5_answer_5) -> resources.getString(R.string.question_5_answer_5)
        resources.getString(R.string.normalized_question_5_answer_6) -> resources.getString(R.string.question_5_answer_6)
        resources.getString(R.string.normalized_question_5_answer_7) -> resources.getString(R.string.question_5_answer_7)
        resources.getString(R.string.normalized_question_5_answer_8) -> resources.getString(R.string.question_5_answer_8)
        resources.getString(R.string.normalized_question_5_answer_9) -> resources.getString(R.string.question_5_answer_9)
        else -> ""
    }

    fun denormalizeZones(normalizedZone: String) = when (normalizedZone) {
        resources.getString(R.string.normalized_question_2_answer_1) -> resources.getString(R.string.question_2_answer_1)
        resources.getString(R.string.normalized_question_2_answer_2) -> resources.getString(R.string.question_2_answer_2)
        resources.getString(R.string.normalized_question_2_answer_3) -> resources.getString(R.string.question_2_answer_3)
        resources.getString(R.string.normalized_question_2_answer_4) -> resources.getString(R.string.question_2_answer_4)
        resources.getString(R.string.normalized_question_2_answer_5) -> resources.getString(R.string.question_2_answer_5)
        resources.getString(R.string.normalized_question_2_answer_6) -> resources.getString(R.string.question_2_answer_6)
        resources.getString(R.string.normalized_question_2_answer_7) -> resources.getString(R.string.question_2_answer_7)
        resources.getString(R.string.normalized_question_2_answer_8) -> resources.getString(R.string.question_2_answer_8)
        resources.getString(R.string.normalized_question_2_answer_9) -> resources.getString(R.string.question_2_answer_9)
        resources.getString(R.string.normalized_question_2_answer_10) -> resources.getString(R.string.question_2_answer_10)
        resources.getString(R.string.normalized_question_2_answer_11) -> resources.getString(R.string.question_2_answer_11)
        resources.getString(R.string.normalized_question_2_answer_12) -> resources.getString(R.string.question_2_answer_12)
        else -> ""
    }

    fun denormalizeFood(normalizedFood: String) = when (normalizedFood) {
        resources.getString(R.string.normalized_question_8_answer_1) -> resources.getString(R.string.question_8_answer_1)
        resources.getString(R.string.normalized_question_8_answer_2) -> resources.getString(R.string.question_8_answer_2)
        resources.getString(R.string.normalized_question_8_answer_3) -> resources.getString(R.string.question_8_answer_3)
        resources.getString(R.string.normalized_question_8_answer_4) -> resources.getString(R.string.question_8_answer_4)
        resources.getString(R.string.normalized_question_8_answer_5) -> resources.getString(R.string.question_8_answer_5)
        resources.getString(R.string.normalized_question_8_answer_6) -> resources.getString(R.string.question_8_answer_6)
        resources.getString(R.string.normalized_question_8_answer_7) -> resources.getString(R.string.question_8_answer_7)
        resources.getString(R.string.normalized_question_8_answer_8) -> resources.getString(R.string.question_8_answer_8)
        resources.getString(R.string.normalized_question_8_answer_9) -> resources.getString(R.string.question_8_answer_9)
        resources.getString(R.string.normalized_question_8_answer_10) -> resources.getString(R.string.question_8_answer_10)
        resources.getString(R.string.normalized_question_8_answer_11) -> resources.getString(R.string.question_8_answer_11)
        resources.getString(R.string.normalized_question_8_answer_12) -> resources.getString(R.string.question_8_answer_12)
        resources.getString(R.string.normalized_question_8_answer_13) -> resources.getString(R.string.question_8_answer_13)
        resources.getString(R.string.normalized_question_8_answer_14) -> resources.getString(R.string.question_8_answer_14)
        resources.getString(R.string.normalized_question_8_answer_15) -> resources.getString(R.string.question_8_answer_15)
        resources.getString(R.string.normalized_question_8_answer_16) -> resources.getString(R.string.question_8_answer_16)
        resources.getString(R.string.normalized_question_8_answer_17) -> resources.getString(R.string.question_8_answer_17)
        resources.getString(R.string.normalized_question_8_answer_18) -> resources.getString(R.string.question_8_answer_18)
        resources.getString(R.string.normalized_question_9_answer_1) -> resources.getString(R.string.question_9_answer_1)
        resources.getString(R.string.normalized_question_9_answer_2) -> resources.getString(R.string.question_9_answer_2)
        resources.getString(R.string.normalized_question_9_answer_3) -> resources.getString(R.string.question_9_answer_3)
        resources.getString(R.string.normalized_question_9_answer_4) -> resources.getString(R.string.question_9_answer_4)
        resources.getString(R.string.normalized_question_9_answer_5) -> resources.getString(R.string.question_9_answer_5)
        resources.getString(R.string.normalized_question_9_answer_6) -> resources.getString(R.string.question_9_answer_6)
        resources.getString(R.string.normalized_question_9_answer_7) -> resources.getString(R.string.question_9_answer_7)
        resources.getString(R.string.normalized_question_9_answer_8) -> resources.getString(R.string.question_9_answer_8)
        resources.getString(R.string.normalized_question_9_answer_9) -> resources.getString(R.string.question_9_answer_9)
        resources.getString(R.string.normalized_question_9_answer_10) -> resources.getString(R.string.question_9_answer_10)
        resources.getString(R.string.normalized_question_9_answer_11) -> resources.getString(R.string.question_9_answer_11)
        resources.getString(R.string.normalized_question_9_answer_12) -> resources.getString(R.string.question_9_answer_12)
        resources.getString(R.string.normalized_question_9_answer_13) -> resources.getString(R.string.question_9_answer_13)
        else -> ""
    }

}