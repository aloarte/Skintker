package com.p4r4d0x.data.parsers

import android.annotation.SuppressLint
import android.content.res.Resources
import com.example.data.R
import com.p4r4d0x.data.dto.*
import com.p4r4d0x.data.dto.logs.*
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.domain.utils.Constants
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
        var questionCnt = 1
        var weatherHumidity = 0.0f
        var weatherTemperature = 0.0f
        var irritation = 0.0f
        var stress = 0.0f
        var alcohol = ""
        val beers = mutableListOf<String>()
        val wines = mutableListOf<String>()
        val distilledDrinks = mutableListOf<String>()
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
                        Constants.TENTH_QUESTION_NUMBER, Constants.ELEVENTH_QUESTION_NUMBER -> {
                            answer.answersStringRes.forEach {
                                foodList.add(
                                    resources.getString(it)
                                )
                            }
                        }

                        Constants.FIFTH_QUESTION_NUMBER -> {
                            answer.answersStringRes.forEach {
                                beers.add(resources.getString(it))
                            }
                        }

                        Constants.SIXTH_QUESTION_NUMBER -> {
                            answer.answersStringRes.forEach {
                                wines.add(resources.getString(it))
                            }
                        }

                        Constants.SEVENTH_QUESTION_NUMBER -> {
                            answer.answersStringRes.forEach {
                                distilledDrinks.add(resources.getString(it))
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
                    if (questionCnt == Constants.EIGHTH_QUESTION_NUMBER) {
                        weatherHumidity = answer.answerValueFirstSlider
                        weatherTemperature = answer.answerValueSecondSlider
                    }
                }

                is Answer.SingleTextInputSingleChoice -> {
                    if (questionCnt == Constants.NINTH_QUESTION_NUMBER) {
                        traveled = resources.getString(answer.answer)
                        city = answer.input
                    }
                }

                is Answer.SingleTextInput -> TODO()

            }

            questionCnt += when (questionCnt) {
                Constants.FOURTH_QUESTION_NUMBER -> {  //Alcohol type question
                    when(fromStringResource(alcohol, resources)){
                        AlcoholLevel.None -> 4
                        AlcoholLevel.Beer -> 1
                        AlcoholLevel.Wine -> 2
                        AlcoholLevel.Distilled -> 3
                        AlcoholLevel.Mixed -> 4
                    }
                }
                Constants.FIFTH_QUESTION_NUMBER -> 3   // Beer question
                Constants.SIXTH_QUESTION_NUMBER -> 2   // Wine question
                Constants.SEVENTH_QUESTION_NUMBER -> 1 //Distilled drinks question
                else -> 1
            }
        }
        val alcoholType = fromStringResource(alcohol, resources)

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
                alcohol = AdditionalDataBO.AlcoholBO(
                    level = alcoholType,
                    beers = if(alcoholType==AlcoholLevel.Beer) beers else emptyList(),
                    wines = if(alcoholType==AlcoholLevel.Wine) wines else emptyList(),
                    distilledDrinks = if(alcoholType==AlcoholLevel.Distilled) distilledDrinks else emptyList()
                )
            ),
            foodList = foodList
        )
    }

    private fun fromStringResource(alcoholStr: String, resources: Resources): AlcoholLevel {
        return when (alcoholStr) {
            resources.getString(R.string.question_4_answer_1) -> AlcoholLevel.None
            resources.getString(R.string.question_4_answer_2) -> AlcoholLevel.Beer
            resources.getString(R.string.question_4_answer_3) -> AlcoholLevel.Wine
            resources.getString(R.string.question_4_answer_4) -> AlcoholLevel.Distilled
            resources.getString(R.string.question_4_answer_5) -> AlcoholLevel.Mixed
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
        return this.content?.stats?.let {
                it.toPossibleCauses()
        }
    }

}
