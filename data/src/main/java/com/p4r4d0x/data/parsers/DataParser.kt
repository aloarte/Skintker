package com.p4r4d0x.data.parsers

import android.annotation.SuppressLint
import android.content.res.Resources
import com.example.data.R
import com.google.firebase.Timestamp
import com.p4r4d0x.data.dto.*
import com.p4r4d0x.data.dto.logs.*
import com.p4r4d0x.data.parsers.DataParser.backendStringToDate
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.domain.utils.Constants.FIFTH_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.Constants.LABEL_ALCOHOL
import com.p4r4d0x.domain.utils.Constants.LABEL_BEERS
import com.p4r4d0x.domain.utils.Constants.LABEL_CITY
import com.p4r4d0x.domain.utils.Constants.LABEL_DATE
import com.p4r4d0x.domain.utils.Constants.LABEL_FOODS
import com.p4r4d0x.domain.utils.Constants.LABEL_IRRITATED_ZONES
import com.p4r4d0x.domain.utils.Constants.LABEL_IRRITATION
import com.p4r4d0x.domain.utils.Constants.LABEL_STRESS
import com.p4r4d0x.domain.utils.Constants.LABEL_TRAVELED
import com.p4r4d0x.domain.utils.Constants.LABEL_WEATHER_HUMIDITY
import com.p4r4d0x.domain.utils.Constants.LABEL_WEATHER_TEMPERATURE
import com.p4r4d0x.domain.utils.Constants.MAX_QUESTION_NUMBER
import com.p4r4d0x.domain.utils.getDateWithoutTime
import java.text.ParsePosition
import java.text.SimpleDateFormat
import java.util.*

object DataParser {

    @SuppressLint("SimpleDateFormat")
    private val backendSDF = SimpleDateFormat("dd-MM-yyyy")

    fun createLogFromSurvey(
        date: Date,
        answers: List<Answer<*>>,
        resources: Resources
    ): DailyLogBO {
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
                    when (questionCnt) {
                        Constants.SECOND_QUESTION_NUMBER -> {
                            answer.answersStringRes.forEach {
                                irritationZones.add(resources.getString(it))
                            }
                        }
                        Constants.NINTH_QUESTION_NUMBER, Constants.EIGHTH_QUESTION_NUMBER -> {
                            answer.answersStringRes.forEach {
                                foodList.add(
                                    resources.getString(it)
                                )
                            }
                        }
                        FIFTH_QUESTION_NUMBER -> {
                            answer.answersStringRes.forEach {
                                beerType.add(
                                    resources.getString(it)
                                )
                            }
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
            date = date,
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
                alcoholLevel = fromStringResource(alcohol, resources),
                beerTypes = beerType
            ),
            foodList = foodList
        )
    }

    private fun fromStringResource(alcoholStr: String, resources: Resources): AlcoholLevel {
        return when (alcoholStr) {
            resources.getString(R.string.question_4_answer_1) -> AlcoholLevel.None
            resources.getString(R.string.question_4_answer_2) -> AlcoholLevel.Few
            resources.getString(R.string.question_4_answer_3) -> AlcoholLevel.FewWine
            resources.getString(R.string.question_4_answer_4) -> AlcoholLevel.Some
            else -> AlcoholLevel.None
        }
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

    @SuppressLint("SimpleDateFormat")
    fun backendDateToString(aDate: Date): String {
        return backendSDF.format(aDate)
    }

    @SuppressLint("SimpleDateFormat")
    fun backendStringToDate(aDate: String): Date {
        return backendSDF.parse(aDate) ?: Date()
    }

    @SuppressLint("SimpleDateFormat")
    fun stringToDateFromPicker(aDate: String): Date {
        val pos = ParsePosition(0)
        val sdf = SimpleDateFormat("yyyy-mm-dd")
        return sdf.parse(aDate, pos) ?: Date(aDate)
    }

    fun parseDocumentData(userId: String, data: MutableMap<String, Any>): DailyLogBO? {
        val userData = data[userId] as? MutableMap<*, *>
        return try {
            userData?.let {
                DailyLogBO(
                    date = (userData[LABEL_DATE] as? Timestamp)?.toDate() ?: Date(),
                    irritation = IrritationBO(
                        overallValue = (userData[LABEL_IRRITATION] as Long).toInt(),
                        zoneValues = listOf(
                            * (userData[LABEL_IRRITATED_ZONES] as String).split(",").toTypedArray()
                        )
                    ),
                    foodList = listOf(
                        * (userData[LABEL_FOODS] as String).split(",").toTypedArray()
                    ),
                    additionalData = AdditionalDataBO(
                        stressLevel = (userData[LABEL_STRESS] as Long).toInt(),
                        weather = AdditionalDataBO.WeatherBO(
                            humidity = (userData[LABEL_WEATHER_HUMIDITY] as Long).toInt(),
                            temperature = (userData[LABEL_WEATHER_TEMPERATURE] as Long).toInt()
                        ),
                        travel = AdditionalDataBO.TravelBO(
                            traveled = userData[LABEL_TRAVELED] as Boolean,
                            city = userData[LABEL_CITY] as String
                        ),
                        alcoholLevel = try {
                            AlcoholLevel.valueOf(userData[LABEL_ALCOHOL] as String)
                        } catch (e: IllegalArgumentException) {
                            AlcoholLevel.None
                        },
                        beerTypes = listOf(
                            * (userData[LABEL_BEERS] as String).split(",").toTypedArray()
                        )
                    )
                )
            } ?: run {
                null
            }
        } catch (e: Exception) {
            null
        }
    }

}


@SuppressLint("SimpleDateFormat")
fun ReportDto.toBo(): DailyLogBO {
    return DailyLogBO(
        date = backendStringToDate(this.date),
        foodList = this.foodList,
        irritation = this.irritation.toBo(),
        additionalData = this.additionalData.toBo()
    )
}


fun SkintkvaultResponseLogs.toDailyLogContents(): DailyLogContentsBO {
    return this.content?.let { logListResponse ->
        DailyLogContentsBO(logListResponse.count, logListResponse.logList.map { it.toBo() })
    } ?: DailyLogContentsBO()

}

fun SkintkvaultResponseStats.toPossibleCauses(): PossibleCausesBO? {
    return this.content?.stats?.toPossibleCauses()
}

