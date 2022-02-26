package com.p4r4d0x.skintker.domain.parsers

import android.annotation.SuppressLint
import android.content.res.Resources
import com.google.firebase.Timestamp
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.Constants.FIFTH_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.Constants.MAX_QUESTION_NUMBER
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.getDateWithoutTime
import com.p4r4d0x.skintker.domain.log.Answer
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object DataParser {

    fun createLogFromSurvey(answers: List<Answer<*>>, resources: Resources): DailyLogBO {
        val dontHaveBeerQuestion = answers.size < MAX_QUESTION_NUMBER
        var questionCnt = 1
        var weatherHumidity = 0.0f
        var weatherTemperature = 0.0f
        var irritation = 0.0f
        var stress = 0.0f
        var alcohol = ""
        val beerType = mutableListOf<String>()
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
                    } else if (questionCnt == Constants.NINTH_QUESTION_NUMBER || questionCnt == Constants.EIGHTH_QUESTION_NUMBER) {
                        answer.answersStringRes.forEach {
                            foodList.add(
                                resources.getString(it)
                            )
                        }
                    } else if (questionCnt == FIFTH_QUESTION_NUMBER) {
                        answer.answersStringRes.forEach {
                            beerType.add(
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
                    if (questionCnt == Constants.SIXTH_QUESTION_NUMBER) {
                        weatherHumidity = answer.answerValueFirstSlider
                        weatherTemperature = answer.answerValueSecondSlider
                    }
                }
                is Answer.SingleTextInputSingleChoice -> {
                    if (questionCnt == Constants.SEVENTH_QUESTION_NUMBER) {
                        traveled = resources.getString(answer.answer)
                        city = answer.input
                    }
                }
                is Answer.SingleTextInput -> TODO()

            }
            questionCnt += if (dontHaveBeerQuestion && questionCnt == Constants.FOURTH_QUESTION_NUMBER) 2 else 1
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
                    traveled = traveled == resources.getString(R.string.question_7_answer_1),
                    city = city.lowercase(Locale.getDefault())
                ),
                alcoholLevel = AlcoholLevel.fromStringResource(alcohol, resources),
                beerTypes = beerType
            ),
            foodList = foodList
        )
    }

    @SuppressLint("SimpleDateFormat")
    fun getCurrentFormattedDate(): Date {
        return Calendar.getInstance().time.getDateWithoutTime()
    }

    @SuppressLint("SimpleDateFormat")
    fun stringToDate(aDate: String): Date {
        val pos = ParsePosition(0)
        val sdf = SimpleDateFormat("dd/mm/yyyy")
        return sdf.parse(aDate, pos) ?: Date(aDate)
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

    fun parseDocumentData(userId: String, data: MutableMap<String, Any>): DailyLogBO? {
        val userData = data[userId] as? MutableMap<*, *>

        return try {
            userData?.let {
                DailyLogBO(
                    date = (userData["date"] as? Timestamp)?.toDate() ?: Date(),
                    irritation = IrritationBO(
                        overallValue = (userData["irritation"] as Long).toInt(),
                        zoneValues = listOf(
                            * (userData["irritatedZones"] as String).split(",").toTypedArray()
                        )
                    ),
                    foodList = listOf(* (userData["foods"] as String).split(",").toTypedArray()),
                    additionalData = AdditionalDataBO(
                        stressLevel = (userData["stress"] as Long).toInt(),
                        weather = AdditionalDataBO.WeatherBO(
                            humidity = (userData["weatherTemperature"] as Long).toInt(),
                            temperature = (userData["humidityTemperature"] as Long).toInt()
                        ),
                        travel = AdditionalDataBO.TravelBO(
                            traveled = userData["traveled"] as Boolean,
                            city = userData["city"] as String
                        ),
                        alcoholLevel = AlcoholLevel.valueOf(userData["alcohol"] as String),
                        beerTypes = listOf(
                            * (userData["beers"] as String).split(",").toTypedArray()
                        ),
                    )
                )
            } ?: run {
                null
            }
        } catch (e: Exception) {
            null
        }
//        val firebaseLog = hashMapOf(
//            "date" to log.date,
//            "irritation" to log.irritation?.overallValue,
//            "irritatedZones" to log.irritation?.zoneValues?.joinToString(separator = ","),
//            "foods" to log.foodList.joinToString(separator = ","),
//            "beers" to log.additionalData?.beerTypes?.joinToString(separator = ","),
//            "alcohol" to log.additionalData?.alcoholLevel?.name,
//            "stress" to log.additionalData?.stressLevel,
//            "city" to log.additionalData?.travel?.city,
//            "traveled" to log.additionalData?.travel?.traveled,
//            "weatherTemperature" to log.additionalData?.weather?.temperature,
//            "humidityTemperature" to log.additionalData?.weather?.humidity
//        )


    }


}