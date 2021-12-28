package com.p4r4d0x.skintker.domain

import android.annotation.SuppressLint
import android.content.res.Resources
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.AlcoholLevel
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.FoodScheduleBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.log.Answer
import java.text.SimpleDateFormat
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
        val irritationZones = mutableListOf<IrritationBO.IrritatedZoneBO>()
        answers.forEach { answer ->
            when (answer) {
                is Answer.DoubleSlider -> {
                    if (questionCnt == Constants.FIFTH_QUESTION_NUMBER) {
                        weatherHumidity = answer.answerValueFirstSlider
                        weatherTemperature = answer.answerValueSecondSlider
                    }
                }
                is Answer.MultipleChoice -> {
                    if (questionCnt == Constants.SECOND_QUESTION_NUMBER) {
                        answer.answersStringRes.forEach {
                            irritationZones.add(
                                IrritationBO.IrritatedZoneBO(
                                    resources.getString(it),
                                    5
                                )
                            )
                        }
                    }
                }
                is Answer.SingleChoice -> {
                    if (questionCnt == Constants.FOURTH_QUESTION_NUMBER) {
                        alcohol = resources.getString(answer.answer)
                    }
                }
                is Answer.SingleTextInput -> TODO()
                is Answer.SingleTextInputSingleChoice -> {
                    if (questionCnt == Constants.SIXTH_QUESTION_NUMBER) {
                        traveled = resources.getString(answer.answer)
                        city = answer.input
                    }
                }
                is Answer.Slider -> {
                    if (questionCnt == Constants.FIRST_QUESTION_NUMBER) {
                        irritation = answer.answerValue
                    } else if (questionCnt == Constants.THIRD_QUESTION_NUMBER) {
                        stress = answer.answerValue
                    }
                }
            }
            questionCnt++
        }

        return DailyLogBO(
            date = getDateWithoutTimeUsingFormat(),
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
                    city = city
                ),
                alcoholLevel = AlcoholLevel.fromString(alcohol, resources)
            ),
            foodSchedule = FoodScheduleBO()
        )

    }

    @SuppressLint("SimpleDateFormat")
    fun getDateWithoutTimeUsingFormat(): Date {
        val formatter = SimpleDateFormat(
            "dd/MM/yyyy"
        )
        return formatter.parse(formatter.format(Calendar.getInstance().time)) ?: Date()
    }

}