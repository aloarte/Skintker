package com.p4r4d0x.data.parsers

import android.content.res.Resources
import com.example.data.R
import com.p4r4d0x.domain.bo.DailyLogBO

class LogsNormalizer(private val resources: Resources) {

    fun denormalizeLog(log: DailyLogBO): DailyLogBO {
        return log.copy(
            foodList = log.foodList.map(::denormalizeFood),
            irritation = log.irritation.copy(zoneValues = log.irritation.zoneValues.map(::denormalizeZones)),
            additionalData = log.additionalData.copy(
                alcohol = log.additionalData.alcohol.copy(
                    beers = log.additionalData.alcohol.beers.map(::denormalizeBeers),
                    wines = log.additionalData.alcohol.wines.map(::denormalizeWines),
                    distilledDrinks = log.additionalData.alcohol.distilledDrinks.map(::denormalizeDistilledDrinks)
                )
            )
        )
    }

    fun normalizeLog(log: DailyLogBO): DailyLogBO {
        return log.copy(
            foodList = log.foodList.map(::normalizeFood),
            irritation = log.irritation.copy(zoneValues = log.irritation.zoneValues.map(::normalizeZones)),
            additionalData = log.additionalData.copy(
                alcohol = log.additionalData.alcohol.copy(
                    beers = log.additionalData.alcohol.beers.map(::normalizeBeers),
                    wines = log.additionalData.alcohol.wines.map(::normalizeWines),
                    distilledDrinks = log.additionalData.alcohol.distilledDrinks.map(::normalizeDistilledDrinks)
                )
            )
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
        resources.getString(R.string.question_5_answer_10) -> resources.getString(R.string.normalized_question_5_answer_10)
        else -> throw IllegalArgumentException("Beer type unknown: $beerType")
    }

    fun normalizeWines(wineType: String) = when (wineType) {
        resources.getString(R.string.question_6_answer_1) -> resources.getString(R.string.normalized_question_6_answer_1)
        resources.getString(R.string.question_6_answer_2) -> resources.getString(R.string.normalized_question_6_answer_2)
        resources.getString(R.string.question_6_answer_3) -> resources.getString(R.string.normalized_question_6_answer_3)
        resources.getString(R.string.question_6_answer_4) -> resources.getString(R.string.normalized_question_6_answer_4)
        resources.getString(R.string.question_6_answer_5) -> resources.getString(R.string.normalized_question_6_answer_5)
        resources.getString(R.string.question_6_answer_6) -> resources.getString(R.string.normalized_question_6_answer_6)
        else -> throw IllegalArgumentException("Wine type unknown: $wineType")
    }

    fun normalizeDistilledDrinks(distilledDrinkType: String) = when (distilledDrinkType) {
        resources.getString(R.string.question_7_answer_1) -> resources.getString(R.string.normalized_question_7_answer_1)
        resources.getString(R.string.question_7_answer_2) -> resources.getString(R.string.normalized_question_7_answer_2)
        resources.getString(R.string.question_7_answer_3) -> resources.getString(R.string.normalized_question_7_answer_3)
        resources.getString(R.string.question_7_answer_4) -> resources.getString(R.string.normalized_question_7_answer_4)
        resources.getString(R.string.question_7_answer_5) -> resources.getString(R.string.normalized_question_7_answer_5)
        resources.getString(R.string.question_7_answer_6) -> resources.getString(R.string.normalized_question_7_answer_6)
        resources.getString(R.string.question_7_answer_7) -> resources.getString(R.string.normalized_question_7_answer_7)

        else -> throw IllegalArgumentException("Distilled drink type unknown: $distilledDrinkType")
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
        resources.getString(R.string.question_10_answer_1) -> resources.getString(R.string.normalized_question_10_answer_1)
        resources.getString(R.string.question_10_answer_2) -> resources.getString(R.string.normalized_question_10_answer_2)
        resources.getString(R.string.question_10_answer_3) -> resources.getString(R.string.normalized_question_10_answer_3)
        resources.getString(R.string.question_10_answer_4) -> resources.getString(R.string.normalized_question_10_answer_4)
        resources.getString(R.string.question_10_answer_5) -> resources.getString(R.string.normalized_question_10_answer_5)
        resources.getString(R.string.question_10_answer_6) -> resources.getString(R.string.normalized_question_10_answer_6)
        resources.getString(R.string.question_10_answer_7) -> resources.getString(R.string.normalized_question_10_answer_7)
        resources.getString(R.string.question_10_answer_8) -> resources.getString(R.string.normalized_question_10_answer_8)
        resources.getString(R.string.question_10_answer_9) -> resources.getString(R.string.normalized_question_10_answer_9)
        resources.getString(R.string.question_10_answer_10) -> resources.getString(R.string.normalized_question_10_answer_10)
        resources.getString(R.string.question_10_answer_11) -> resources.getString(R.string.normalized_question_10_answer_11)
        resources.getString(R.string.question_10_answer_12) -> resources.getString(R.string.normalized_question_10_answer_12)
        resources.getString(R.string.question_10_answer_13) -> resources.getString(R.string.normalized_question_10_answer_13)
        resources.getString(R.string.question_10_answer_14) -> resources.getString(R.string.normalized_question_10_answer_14)
        resources.getString(R.string.question_10_answer_15) -> resources.getString(R.string.normalized_question_10_answer_15)
        resources.getString(R.string.question_10_answer_16) -> resources.getString(R.string.normalized_question_10_answer_16)
        resources.getString(R.string.question_10_answer_17) -> resources.getString(R.string.normalized_question_10_answer_17)
        resources.getString(R.string.question_10_answer_18) -> resources.getString(R.string.normalized_question_10_answer_18)
        resources.getString(R.string.question_11_answer_1) -> resources.getString(R.string.normalized_question_11_answer_1)
        resources.getString(R.string.question_11_answer_2) -> resources.getString(R.string.normalized_question_11_answer_2)
        resources.getString(R.string.question_11_answer_3) -> resources.getString(R.string.normalized_question_11_answer_3)
        resources.getString(R.string.question_11_answer_4) -> resources.getString(R.string.normalized_question_11_answer_4)
        resources.getString(R.string.question_11_answer_5) -> resources.getString(R.string.normalized_question_11_answer_5)
        resources.getString(R.string.question_11_answer_6) -> resources.getString(R.string.normalized_question_11_answer_6)
        resources.getString(R.string.question_11_answer_7) -> resources.getString(R.string.normalized_question_11_answer_7)
        resources.getString(R.string.question_11_answer_8) -> resources.getString(R.string.normalized_question_11_answer_8)
        resources.getString(R.string.question_11_answer_9) -> resources.getString(R.string.normalized_question_11_answer_9)
        resources.getString(R.string.question_11_answer_10) -> resources.getString(R.string.normalized_question_11_answer_10)
        resources.getString(R.string.question_11_answer_11) -> resources.getString(R.string.normalized_question_11_answer_11)
        resources.getString(R.string.question_11_answer_12) -> resources.getString(R.string.normalized_question_11_answer_12)
        resources.getString(R.string.question_11_answer_13) -> resources.getString(R.string.normalized_question_11_answer_13)
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
        resources.getString(R.string.normalized_question_5_answer_10) -> resources.getString(R.string.question_5_answer_10)
        else -> ""
    }

    fun denormalizeWines(normalizedWineType: String) = when (normalizedWineType) {
        resources.getString(R.string.normalized_question_6_answer_1) -> resources.getString(R.string.question_6_answer_1)
        resources.getString(R.string.normalized_question_6_answer_2) -> resources.getString(R.string.question_6_answer_2)
        resources.getString(R.string.normalized_question_6_answer_3) -> resources.getString(R.string.question_6_answer_3)
        resources.getString(R.string.normalized_question_6_answer_4) -> resources.getString(R.string.question_6_answer_4)
        resources.getString(R.string.normalized_question_6_answer_5) -> resources.getString(R.string.question_6_answer_5)
        resources.getString(R.string.normalized_question_6_answer_6) -> resources.getString(R.string.question_6_answer_6)
        else -> ""
    }

    fun denormalizeDistilledDrinks(normalizedDistilledDrinkType: String) = when (normalizedDistilledDrinkType) {
        resources.getString(R.string.normalized_question_7_answer_1) -> resources.getString(R.string.question_7_answer_1)
        resources.getString(R.string.normalized_question_7_answer_2) -> resources.getString(R.string.question_7_answer_2)
        resources.getString(R.string.normalized_question_7_answer_3) -> resources.getString(R.string.question_7_answer_3)
        resources.getString(R.string.normalized_question_7_answer_4) -> resources.getString(R.string.question_7_answer_4)
        resources.getString(R.string.normalized_question_7_answer_5) -> resources.getString(R.string.question_7_answer_5)
        resources.getString(R.string.normalized_question_7_answer_6) -> resources.getString(R.string.question_7_answer_6)
        resources.getString(R.string.normalized_question_7_answer_7) -> resources.getString(R.string.question_7_answer_7)
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
        resources.getString(R.string.normalized_question_10_answer_1) -> resources.getString(R.string.question_10_answer_1)
        resources.getString(R.string.normalized_question_10_answer_2) -> resources.getString(R.string.question_10_answer_2)
        resources.getString(R.string.normalized_question_10_answer_3) -> resources.getString(R.string.question_10_answer_3)
        resources.getString(R.string.normalized_question_10_answer_4) -> resources.getString(R.string.question_10_answer_4)
        resources.getString(R.string.normalized_question_10_answer_5) -> resources.getString(R.string.question_10_answer_5)
        resources.getString(R.string.normalized_question_10_answer_6) -> resources.getString(R.string.question_10_answer_6)
        resources.getString(R.string.normalized_question_10_answer_7) -> resources.getString(R.string.question_10_answer_7)
        resources.getString(R.string.normalized_question_10_answer_8) -> resources.getString(R.string.question_10_answer_8)
        resources.getString(R.string.normalized_question_10_answer_9) -> resources.getString(R.string.question_10_answer_9)
        resources.getString(R.string.normalized_question_10_answer_10) -> resources.getString(R.string.question_10_answer_10)
        resources.getString(R.string.normalized_question_10_answer_11) -> resources.getString(R.string.question_10_answer_11)
        resources.getString(R.string.normalized_question_10_answer_12) -> resources.getString(R.string.question_10_answer_12)
        resources.getString(R.string.normalized_question_10_answer_13) -> resources.getString(R.string.question_10_answer_13)
        resources.getString(R.string.normalized_question_10_answer_14) -> resources.getString(R.string.question_10_answer_14)
        resources.getString(R.string.normalized_question_10_answer_15) -> resources.getString(R.string.question_10_answer_15)
        resources.getString(R.string.normalized_question_10_answer_16) -> resources.getString(R.string.question_10_answer_16)
        resources.getString(R.string.normalized_question_10_answer_17) -> resources.getString(R.string.question_10_answer_17)
        resources.getString(R.string.normalized_question_10_answer_18) -> resources.getString(R.string.question_10_answer_18)
        resources.getString(R.string.normalized_question_11_answer_1) -> resources.getString(R.string.question_11_answer_1)
        resources.getString(R.string.normalized_question_11_answer_2) -> resources.getString(R.string.question_11_answer_2)
        resources.getString(R.string.normalized_question_11_answer_3) -> resources.getString(R.string.question_11_answer_3)
        resources.getString(R.string.normalized_question_11_answer_4) -> resources.getString(R.string.question_11_answer_4)
        resources.getString(R.string.normalized_question_11_answer_5) -> resources.getString(R.string.question_11_answer_5)
        resources.getString(R.string.normalized_question_11_answer_6) -> resources.getString(R.string.question_11_answer_6)
        resources.getString(R.string.normalized_question_11_answer_7) -> resources.getString(R.string.question_11_answer_7)
        resources.getString(R.string.normalized_question_11_answer_8) -> resources.getString(R.string.question_11_answer_8)
        resources.getString(R.string.normalized_question_11_answer_9) -> resources.getString(R.string.question_11_answer_9)
        resources.getString(R.string.normalized_question_11_answer_10) -> resources.getString(R.string.question_11_answer_10)
        resources.getString(R.string.normalized_question_11_answer_11) -> resources.getString(R.string.question_11_answer_11)
        resources.getString(R.string.normalized_question_11_answer_12) -> resources.getString(R.string.question_11_answer_12)
        resources.getString(R.string.normalized_question_11_answer_13) -> resources.getString(R.string.question_11_answer_13)
        else -> ""
    }

}