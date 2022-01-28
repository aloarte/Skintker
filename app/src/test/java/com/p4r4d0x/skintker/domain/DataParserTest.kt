package com.p4r4d0x.skintker.domain

import android.content.res.Resources
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.log.Answer
import com.p4r4d0x.skintker.domain.parsers.DataParser
import com.p4r4d0x.skintker.domain.parsers.DataParser.createLogFromSurvey
import com.p4r4d0x.skintker.domain.parsers.DataParser.getAlcoholLevel
import com.p4r4d0x.skintker.domain.parsers.DataParser.getDataCSVRow
import com.p4r4d0x.skintker.domain.parsers.DataParser.getHeaderCSVRow
import com.p4r4d0x.skintker.domain.parsers.DataParser.getHumidityString
import com.p4r4d0x.skintker.domain.parsers.DataParser.getTemperatureString
import io.mockk.every
import io.mockk.impl.annotations.MockK
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import java.util.*
import kotlin.test.assertEquals

class DataParserTest {

    companion object {
        private const val FIRST_QUESTION_ANSWER_SLIDER = 5f
        private var SECOND_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private const val THIRD_QUESTION_ANSWER_SLIDER = 2f
        private const val FOURTH_QUESTION_ANSWER_SINGLE = R.string.card_no_alcohol
        private const val FIFTH_QUESTION_ANSWER_SLIDER_1 = 3f
        private const val FIFTH_QUESTION_ANSWER_SLIDER_2 = 7f
        private const val SIXTH_QUESTION_ANSWER_INPUT = "AnswerInput"
        private const val SIXTH_QUESTION_ANSWER_SINGLE = R.string.single_answer_2
        private const val SINGLE_ANSWER_1_VALUE = "No alcohol"
        private const val SINGLE_ANSWER_2_VALUE = "Single answer 2"
        private const val MULTIPLE_ANSWER_1_VALUE = "Multiple answer 1"
        private const val MULTIPLE_ANSWER_2_VALUE = "Multiple answer 2"
        private const val ALCOHOL_QUESTION_ANSWER_1 = "No alcohol ingested"
        private const val ALCOHOL_QUESTION_ANSWER_2 = "Took a few beers"
        private const val ALCOHOL_QUESTION_ANSWER_3 = "Had some drinks"
        private const val TRAVEL_QUESTION_ANSWER = "Yes, I traveled"

        val date = Date(1641031810L * 1000) // 2022/01/01 10:10:10
        private const val FOOD_1 = "Banana"
        private const val FOOD_2 = "Apple"
        private const val FOOD_3 = "Fish"
        private const val ZONE_1 = "Leg"
        private const val ZONE_2 = "Shoulder"
        val referenceZonesList = mapOf(ZONE_1 to 0, ZONE_2 to 1)
        val referenceFoodList = mapOf(FOOD_1 to 0, FOOD_2 to 1, FOOD_3 to 2)

    }

    @MockK
    private lateinit var resources: Resources

    @Before
    fun setup() {

    }

    @Test
    fun `get humidity`() {
        Assert.assertEquals(R.string.humidity_3, getHumidityString(-1))
        Assert.assertEquals(R.string.humidity_1, getHumidityString(0))
        Assert.assertEquals(R.string.humidity_1, getHumidityString(1))
        Assert.assertEquals(R.string.humidity_2, getHumidityString(2))
        Assert.assertEquals(R.string.humidity_2, getHumidityString(3))
        Assert.assertEquals(R.string.humidity_3, getHumidityString(4))
        Assert.assertEquals(R.string.humidity_3, getHumidityString(5))
        Assert.assertEquals(R.string.humidity_3, getHumidityString(6))
        Assert.assertEquals(R.string.humidity_4, getHumidityString(7))
        Assert.assertEquals(R.string.humidity_4, getHumidityString(8))
        Assert.assertEquals(R.string.humidity_5, getHumidityString(9))
        Assert.assertEquals(R.string.humidity_5, getHumidityString(10))
    }

    @Test
    fun `get temperature`() {
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(-1))
        Assert.assertEquals(R.string.temperature_1, getTemperatureString(0))
        Assert.assertEquals(R.string.temperature_1, getTemperatureString(1))
        Assert.assertEquals(R.string.temperature_2, getTemperatureString(2))
        Assert.assertEquals(R.string.temperature_2, getTemperatureString(3))
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(4))
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(5))
        Assert.assertEquals(R.string.temperature_3, getTemperatureString(6))
        Assert.assertEquals(R.string.temperature_4, getTemperatureString(7))
        Assert.assertEquals(R.string.temperature_4, getTemperatureString(8))
        Assert.assertEquals(R.string.temperature_5, getTemperatureString(9))
        Assert.assertEquals(R.string.temperature_5, getTemperatureString(10))
    }


    @Test
    fun `get alcohol level`() {
        Assert.assertEquals(R.string.card_no_alcohol, getAlcoholLevel(-1))
        Assert.assertEquals(R.string.card_no_alcohol, getAlcoholLevel(0))
        Assert.assertEquals(R.string.card_any_alcohol, getAlcoholLevel(1))
        Assert.assertEquals(R.string.card_quite_alcohol, getAlcoholLevel(2))
    }

    @Test
    fun `create log from survey `() {
        val answerList = mutableListOf<Answer<*>>()
        answerList.add(Answer.Slider(FIRST_QUESTION_ANSWER_SLIDER))
        answerList.add(Answer.MultipleChoice(SECOND_QUESTION_ANSWER_MULTIPLE))
        answerList.add(Answer.Slider(THIRD_QUESTION_ANSWER_SLIDER))
        answerList.add(Answer.SingleChoice(FOURTH_QUESTION_ANSWER_SINGLE))
        answerList.add(
            Answer.DoubleSlider(
                FIFTH_QUESTION_ANSWER_SLIDER_1, FIFTH_QUESTION_ANSWER_SLIDER_2
            )
        )
        answerList.add(
            Answer.SingleTextInputSingleChoice(
                SIXTH_QUESTION_ANSWER_INPUT,
                SIXTH_QUESTION_ANSWER_SINGLE
            )
        )
        every { resources.getString(R.string.card_no_alcohol) } returns SINGLE_ANSWER_1_VALUE
        every { resources.getString(R.string.single_answer_2) } returns SINGLE_ANSWER_2_VALUE
        every { resources.getString(R.string.multiple_answer_1) } returns MULTIPLE_ANSWER_1_VALUE
        every { resources.getString(R.string.multiple_answer_2) } returns MULTIPLE_ANSWER_2_VALUE
        every { resources.getString(R.string.question_4_answer_1) } returns ALCOHOL_QUESTION_ANSWER_1
        every { resources.getString(R.string.question_4_answer_2) } returns ALCOHOL_QUESTION_ANSWER_2
        every { resources.getString(R.string.question_4_answer_3) } returns ALCOHOL_QUESTION_ANSWER_3
        every { resources.getString(R.string.question_6_answer_1) } returns TRAVEL_QUESTION_ANSWER

        val surveyLog = createLogFromSurvey(answers = answerList, resources = resources)

        val expectedLog = DailyLogBO(
            date = DataParser.getCurrentFormattedDate(),
            irritation = IrritationBO(
                overallValue = FIRST_QUESTION_ANSWER_SLIDER.toInt(),
                zoneValues = listOf(
                    MULTIPLE_ANSWER_1_VALUE,
                    MULTIPLE_ANSWER_2_VALUE
                )
            ),
            additionalData = AdditionalDataBO(
                stressLevel = THIRD_QUESTION_ANSWER_SLIDER.toInt(),
                weather = AdditionalDataBO.WeatherBO(
                    humidity = FIFTH_QUESTION_ANSWER_SLIDER_1.toInt(),
                    temperature = FIFTH_QUESTION_ANSWER_SLIDER_2.toInt()
                ),
                travel = AdditionalDataBO.TravelBO(
                    traveled = false,
                    city = SIXTH_QUESTION_ANSWER_INPUT
                ),
                alcoholLevel = AlcoholLevel.None
            ),
            foodList = emptyList()
        )
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `get header CSV row`() {
        val headerList = getHeaderCSVRow(referenceZonesList, referenceFoodList)

        val expectedList = listOf(
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
            "Traveled",
            ZONE_1, ZONE_2,
            FOOD_1, FOOD_2, FOOD_3
        )

        assertEquals(expectedList, headerList)
    }

    @Test
    fun `get data CSV row`() {
        val dailyLog = DailyLogBO(
            date = date,

            foodList = listOf(FOOD_1, FOOD_3),
            additionalData = AdditionalDataBO(
                alcoholLevel = AlcoholLevel.Few,
                weather = AdditionalDataBO.WeatherBO(temperature = 3, humidity = 4),
                travel = AdditionalDataBO.TravelBO(city = "Madrid", traveled = true),
                stressLevel = 10
            ),
            irritation = IrritationBO(overallValue = 10, zoneValues = listOf(ZONE_1))
        )

        val dataRow = getDataCSVRow(
            index = 2,
            log = dailyLog,
            referenceZonesList = referenceZonesList,
            referenceFoodList = referenceFoodList,
        )

        val expectedList = listOf(
            "2",
            "01/01/2022",
            "$FOOD_1,$FOOD_3",
            "10",
            ZONE_1,
            "Few",
            "10",
            "4",
            "3",
            "Madrid",
            "true",
            ZONE_1, "",
            FOOD_1, "", FOOD_3
        )
        assertEquals(expectedList, dataRow)
    }


}