package com.p4r4d0x.skintker.domain.parsers

import android.annotation.SuppressLint
import android.content.res.Resources
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.getDateWithoutTime
import com.p4r4d0x.skintker.domain.log.Answer
import java.util.*

object DataParser {

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
}