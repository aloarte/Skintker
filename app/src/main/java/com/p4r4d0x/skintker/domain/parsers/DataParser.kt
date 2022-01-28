package com.p4r4d0x.skintker.domain.parsers

import android.annotation.SuppressLint
import android.content.res.Resources
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.cleanString
import com.p4r4d0x.skintker.domain.getDDMMYYYYDate
import com.p4r4d0x.skintker.domain.getDateWithoutTime
import com.p4r4d0x.skintker.domain.log.Answer
import java.util.*

object DataParser {

    private val zonesReferenceMap = mapOf(
        R.string.question_2_answer_1 to 0,
        R.string.question_2_answer_2 to 1,
        R.string.question_2_answer_3 to 2,
        R.string.question_2_answer_4 to 3,
        R.string.question_2_answer_5 to 4,
        R.string.question_2_answer_6 to 5,
        R.string.question_2_answer_7 to 6,
        R.string.question_2_answer_8 to 7,
        R.string.question_2_answer_9 to 8,
        R.string.question_2_answer_10 to 9,
        R.string.question_2_answer_11 to 10,
        R.string.question_2_answer_12 to 11
    )

    private val foodReferenceMap = mapOf(
        R.string.question_7_answer_1 to 0,
        R.string.question_7_answer_2 to 1,
        R.string.question_7_answer_3 to 2,
        R.string.question_7_answer_4 to 3,
        R.string.question_7_answer_5 to 4,
        R.string.question_7_answer_6 to 5,
        R.string.question_7_answer_7 to 6,
        R.string.question_7_answer_8 to 7,
        R.string.question_7_answer_9 to 8,
        R.string.question_7_answer_10 to 9,
        R.string.question_7_answer_11 to 10,
        R.string.question_7_answer_12 to 11,
        R.string.question_7_answer_13 to 12,
        R.string.question_7_answer_14 to 13,
        R.string.question_7_answer_15 to 14,
        R.string.question_7_answer_16 to 15,
        R.string.question_7_answer_17 to 16,
        R.string.question_7_answer_18 to 17,
        R.string.question_8_answer_1 to 18,
        R.string.question_8_answer_2 to 19,
        R.string.question_8_answer_3 to 20,
        R.string.question_8_answer_4 to 21,
        R.string.question_8_answer_5 to 22,
        R.string.question_8_answer_6 to 23,
        R.string.question_8_answer_7 to 24,
        R.string.question_8_answer_8 to 25,
        R.string.question_8_answer_9 to 26,
        R.string.question_8_answer_10 to 27,
        R.string.question_8_answer_11 to 28,
        R.string.question_8_answer_12 to 29,
        R.string.question_8_answer_13 to 30
    )

    fun createLogFromSurvey(answers: List<Answer<*>>, resources: Resources): DailyLogBO {
        var questionCnt = 1
        var weatherHumidity = 0.0f
        var weatherTemperature = 0.0f
        var irritation = 0.0f
        var stress = 0.0f
        var alcohol = ""
        var city = ""
        var traveled = ""
        val irritationZones = mutableListOf<String>()
        val foodList = mutableListOf<String>()
        answers.forEach { answer ->
            when (answer) {
                is Answer.Slider -> {
                    if (questionCnt == Constants.FIRST_QUESTION_NUMBER) {
                        irritation = answer.answerValue
                    } else if (questionCnt == Constants.THIRD_QUESTION_NUMBER) {
                        stress = answer.answerValue
                    }
                }
                is Answer.MultipleChoice -> {
                    if (questionCnt == Constants.SECOND_QUESTION_NUMBER) {
                        answer.answersStringRes.forEach {
                            irritationZones.add(resources.getString(it))
                        }
                    } else if (questionCnt == Constants.SEVENTH_QUESTION_NUMBER || questionCnt == Constants.EIGHT_QUESTION_NUMBER) {
                        answer.answersStringRes.forEach {
                            foodList.add(
                                resources.getString(it)
                            )
                        }

                    }
                }

                is Answer.SingleChoice -> {
                    if (questionCnt == Constants.FOURTH_QUESTION_NUMBER) {
                        alcohol = resources.getString(answer.answer)
                    }
                }
                is Answer.DoubleSlider -> {
                    if (questionCnt == Constants.FIFTH_QUESTION_NUMBER) {
                        weatherHumidity = answer.answerValueFirstSlider
                        weatherTemperature = answer.answerValueSecondSlider
                    }
                }
                is Answer.SingleTextInputSingleChoice -> {
                    if (questionCnt == Constants.SIXTH_QUESTION_NUMBER) {
                        traveled = resources.getString(answer.answer)
                        city = answer.input
                    }
                }
                is Answer.SingleTextInput -> TODO()

            }
            questionCnt++
        }

        return DailyLogBO(
            date = getCurrentFormattedDate(),
            irritation = IrritationBO(
                overallValue = irritation.toInt(),
                zoneValues = irritationZones
            ),
            additionalData = AdditionalDataBO(
                stressLevel = stress.toInt(),
                weather = AdditionalDataBO.WeatherBO(
                    humidity = weatherHumidity.toInt(),
                    temperature = weatherTemperature.toInt()
                ),
                travel = AdditionalDataBO.TravelBO(
                    traveled = traveled == resources.getString(R.string.question_6_answer_1),
                    city = city.lowercase(Locale.getDefault())
                ),
                alcoholLevel = AlcoholLevel.fromString(alcohol, resources)
            ),
            foodList = foodList
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentFormattedDate(): Date {
        return Calendar.getInstance().time.getDateWithoutTime()
    }

    fun getHumidityString(value: Int) =
        when (value) {
            0, 1 -> R.string.humidity_1
            2, 3 -> R.string.humidity_2
            4, 5, 6 -> R.string.humidity_3
            7, 8 -> R.string.humidity_4
            9, 10 -> R.string.humidity_5
            else -> R.string.humidity_3
        }

    fun getTemperatureString(value: Int) =
        when (value) {
            0, 1 -> R.string.temperature_1
            2, 3 -> R.string.temperature_2
            4, 5, 6 -> R.string.temperature_3
            7, 8 -> R.string.temperature_4
            9, 10 -> R.string.temperature_5
            else -> R.string.temperature_3
        }

    fun getAlcoholLevel(alcoholLevel: Int): Int =
        when (alcoholLevel) {
            0 -> R.string.card_no_alcohol
            1 -> R.string.card_any_alcohol
            2 -> R.string.card_quite_alcohol
            else -> R.string.card_no_alcohol
        }


    private fun getReferenceMap(
        resources: Resources,
        referenceResourceMap: Map<Int, Int>
    ): Map<String, Int> {
        val transformedMap = mutableMapOf<String, Int>()
        referenceResourceMap.forEach {
            transformedMap[resources.getString(it.key).cleanString()] = it.value
        }
        return transformedMap
    }

    fun getZonesReferenceMap(resources: Resources) =
        getReferenceMap(resources, zonesReferenceMap)

    fun getFoodReferenceMap(resources: Resources) =
        getReferenceMap(resources, foodReferenceMap)

    fun getHeaderCSVRow(
        referenceZonesList: Map<String, Int>,
        referenceFoodList: Map<String, Int>
    ): List<String> {
        val zonesHeaderList = referenceZonesList.toList().map { it.first }
        val foodHeaderList = referenceFoodList.toList().map { it.first }

        val headerList = mutableListOf(
            "Id",
            "Date",
            "FoodList",
            "Irritation",
            "IrritationZones",
            "Alcohol",
            "Stress",
            "Humidity",
            "Temperature",
            "City",
            "Traveled"
        )
        headerList.addAll(zonesHeaderList)
        headerList.addAll(foodHeaderList)
        return headerList
    }

    fun getDataCSVRow(
        index: Int,
        log: DailyLogBO,
        referenceZonesList: Map<String, Int>,
        referenceFoodList: Map<String, Int>
    ): List<Any?> {

        val dataList = mutableListOf(
            index.toString(),
            log.date.getDDMMYYYYDate(),
            log.foodList.joinToString(separator = ",") { food ->
                food.cleanString()
            },
            log.irritation?.overallValue.toString(),
            log.irritation?.zoneValues?.joinToString(separator = ",") { zone ->
                zone.cleanString()
            },
            log.additionalData?.alcoholLevel?.name,
            log.additionalData?.stressLevel.toString(),
            log.additionalData?.weather?.humidity.toString(),
            log.additionalData?.weather?.temperature.toString(),
            log.additionalData?.travel?.city,
            log.additionalData?.travel?.traveled.toString()

        )

        dataList.addAll(getDataList(log.irritation?.zoneValues, referenceZonesList))
        dataList.addAll(getDataList(log.foodList, referenceFoodList))
        return dataList
    }

    private fun getDataList(
        dataValues: List<String>?,
        referenceMap: Map<String, Int>
    ): List<String> {
        val rowMap = mutableMapOf<Int, String>()
        var indexCnt = 0
        referenceMap.forEach { _ ->
            rowMap[indexCnt] = ""
            indexCnt++
        }

        dataValues?.forEach { dataValue ->
            val cleanedData = dataValue.cleanString()
            val position = referenceMap[cleanedData]
            position?.let {
                rowMap[it] = cleanedData
            }
        }
        return rowMap.toList().map { it.second }
    }

}