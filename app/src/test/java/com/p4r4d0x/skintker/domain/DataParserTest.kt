package com.p4r4d0x.skintker.domain


import android.content.res.Resources
import com.google.firebase.Timestamp
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.enums.AlcoholLevel
import com.p4r4d0x.skintker.domain.bo.AdditionalDataBO
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.log.Answer
import com.p4r4d0x.skintker.domain.parsers.DataParser
import com.p4r4d0x.skintker.domain.parsers.DataParser.createLogFromSurvey
import com.p4r4d0x.skintker.domain.parsers.DataParser.getAlcoholLevel
import com.p4r4d0x.skintker.domain.parsers.DataParser.getHumidityString
import com.p4r4d0x.skintker.domain.parsers.DataParser.getTemperatureString
import com.p4r4d0x.skintker.domain.parsers.DataParser.parseDocumentData
import io.mockk.every
import io.mockk.mockk
import org.junit.Assert
import org.junit.Before
import org.junit.Test

class DataParserTest {
    companion object {
        private const val FIRST_QUESTION_ANSWER_SLIDER = 5f
        private var SECOND_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private const val THIRD_QUESTION_ANSWER_SLIDER = 2f
        private const val FOURTH_QUESTION_ANSWER_SINGLE = R.string.card_no_alcohol
        private var FIFTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private const val SIXTH_QUESTION_ANSWER_SLIDER_1 = 3f
        private const val SIXTH_QUESTION_ANSWER_SLIDER_2 = 7f
        private const val SEVENTH_QUESTION_ANSWER_INPUT = "AnswerInput"
        private const val SEVENTH_QUESTION_ANSWER_INPUT_LOWER_CASE = "answerinput"
        private const val SEVENTH_QUESTION_ANSWER_SINGLE = R.string.single_answer_2
        private var EIGHT_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private var NINTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private const val SINGLE_ANSWER_1_VALUE = "No alcohol"
        private const val SINGLE_ANSWER_2_VALUE = "Single answer 2"
        private const val MULTIPLE_ANSWER_1_VALUE = "Multiple answer 1"
        private const val MULTIPLE_ANSWER_2_VALUE = "Multiple answer 2"
        private const val ALCOHOL_QUESTION_ANSWER_1 = "No alcohol ingested"
        private const val ALCOHOL_QUESTION_ANSWER_2 = "Took a few beers"
        private const val ALCOHOL_QUESTION_ANSWER_3 = "Took a few wine cups"
        private const val ALCOHOL_QUESTION_ANSWER_4 = "Had some drinks"
        private const val TRAVEL_QUESTION_ANSWER = "Yes, I traveled"
        private const val USER_ID = "UserId"
        private const val BAD_USER_ID = "BadUserId"
    }

    lateinit var resources: Resources

    @Before
    fun setup() {
        mockResources()
    }

    private fun mockResources() {
        resources = mockk()
        every { resources.getString(R.string.card_no_alcohol) } returns SINGLE_ANSWER_1_VALUE
        every { resources.getString(R.string.single_answer_2) } returns SINGLE_ANSWER_2_VALUE
        every { resources.getString(R.string.multiple_answer_1) } returns MULTIPLE_ANSWER_1_VALUE
        every { resources.getString(R.string.multiple_answer_2) } returns MULTIPLE_ANSWER_2_VALUE
        every { resources.getString(R.string.question_4_answer_1) } returns ALCOHOL_QUESTION_ANSWER_1
        every { resources.getString(R.string.question_4_answer_2) } returns ALCOHOL_QUESTION_ANSWER_2
        every { resources.getString(R.string.question_4_answer_3) } returns ALCOHOL_QUESTION_ANSWER_3
        every { resources.getString(R.string.question_4_answer_4) } returns ALCOHOL_QUESTION_ANSWER_4
        every { resources.getString(R.string.question_7_answer_1) } returns TRAVEL_QUESTION_ANSWER
    }

    private fun getAnswerList(expandBeerQuestion: Boolean): List<Answer<*>> {
        val answerList = mutableListOf<Answer<*>>()
        answerList.add(Answer.Slider(FIRST_QUESTION_ANSWER_SLIDER))
        answerList.add(Answer.MultipleChoice(SECOND_QUESTION_ANSWER_MULTIPLE))
        answerList.add(Answer.Slider(THIRD_QUESTION_ANSWER_SLIDER))
        answerList.add(Answer.SingleChoice(FOURTH_QUESTION_ANSWER_SINGLE))
        if (expandBeerQuestion) {
            answerList.add(Answer.MultipleChoice(FIFTH_QUESTION_ANSWER_MULTIPLE))
        }
        answerList.add(
            Answer.DoubleSlider(
                SIXTH_QUESTION_ANSWER_SLIDER_1, SIXTH_QUESTION_ANSWER_SLIDER_2
            )
        )
        answerList.add(
            Answer.SingleTextInputSingleChoice(
                SEVENTH_QUESTION_ANSWER_INPUT,
                SEVENTH_QUESTION_ANSWER_SINGLE
            )
        )
        answerList.add(Answer.MultipleChoice(EIGHT_QUESTION_ANSWER_MULTIPLE))
        answerList.add(Answer.MultipleChoice(NINTH_QUESTION_ANSWER_MULTIPLE))
        return answerList
    }

    private fun getDailyLog(expandBeerQuestion: Boolean) = DailyLogBO(
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
                humidity = SIXTH_QUESTION_ANSWER_SLIDER_1.toInt(),
                temperature = SIXTH_QUESTION_ANSWER_SLIDER_2.toInt()
            ),
            travel = AdditionalDataBO.TravelBO(
                traveled = false,
                city = SEVENTH_QUESTION_ANSWER_INPUT_LOWER_CASE
            ),
            alcoholLevel = AlcoholLevel.None,
            beerTypes = if (expandBeerQuestion) listOf(
                MULTIPLE_ANSWER_1_VALUE,
                MULTIPLE_ANSWER_2_VALUE
            ) else emptyList()
        ),
        foodList = listOf(
            MULTIPLE_ANSWER_1_VALUE,
            MULTIPLE_ANSWER_2_VALUE,
            MULTIPLE_ANSWER_1_VALUE,
            MULTIPLE_ANSWER_2_VALUE
        )
    )

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
        Assert.assertEquals(R.string.card_any_alcohol_beer, getAlcoholLevel(1))
        Assert.assertEquals(R.string.card_any_alcohol_wine, getAlcoholLevel(2))
        Assert.assertEquals(R.string.card_quite_alcohol, getAlcoholLevel(3))
    }

    @Test
    fun `create log from survey with beer question`() {
        val answerList = getAnswerList(expandBeerQuestion = true)

        val surveyLog = createLogFromSurvey(
            date = DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(expandBeerQuestion = true)
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `create log from survey without beer question`() {
        val answerList = getAnswerList(expandBeerQuestion = false)

        val surveyLog = createLogFromSurvey(
            DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(expandBeerQuestion = false)
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `parse document data from fb success data`() {
        val log = getDailyLog(true)
        val data = mutableMapOf<String, Any>()
        val userData = hashMapOf(
            Constants.LABEL_DATE to Timestamp(log.date),
            Constants.LABEL_IRRITATION to log.irritation?.overallValue?.toLong(),
            Constants.LABEL_IRRITATED_ZONES to log.irritation?.zoneValues?.joinToString(separator = ","),
            Constants.LABEL_FOODS to log.foodList.joinToString(separator = ","),
            Constants.LABEL_BEERS to log.additionalData?.beerTypes?.joinToString(separator = ","),
            Constants.LABEL_ALCOHOL to log.additionalData?.alcoholLevel?.name,
            Constants.LABEL_STRESS to log.additionalData?.stressLevel?.toLong(),
            Constants.LABEL_CITY to log.additionalData?.travel?.city,
            Constants.LABEL_TRAVELED to log.additionalData?.travel?.traveled,
            Constants.LABEL_WEATHER_TEMPERATURE to log.additionalData?.weather?.temperature?.toLong(),
            Constants.LABEL_WEATHER_HUMIDITY to log.additionalData?.weather?.humidity?.toLong()
        )
        data[USER_ID] = userData

        val parsedLog = parseDocumentData(userId = USER_ID, data = data)

        Assert.assertEquals(log, parsedLog)
    }

    @Test
    fun `parse document data from fb wrong data bad user`() {
        val log = getDailyLog(true)
        val data = mutableMapOf<String, Any>()
        val userData = hashMapOf(
            Constants.LABEL_DATE to Timestamp(log.date),
            Constants.LABEL_IRRITATION to log.irritation?.overallValue?.toLong(),
            Constants.LABEL_IRRITATED_ZONES to log.irritation?.zoneValues?.joinToString(separator = ","),
            Constants.LABEL_FOODS to log.foodList.joinToString(separator = ","),
            Constants.LABEL_BEERS to log.additionalData?.beerTypes?.joinToString(separator = ","),
            Constants.LABEL_ALCOHOL to log.additionalData?.alcoholLevel?.name,
            Constants.LABEL_STRESS to log.additionalData?.stressLevel?.toLong(),
            Constants.LABEL_CITY to log.additionalData?.travel?.city,
            Constants.LABEL_TRAVELED to log.additionalData?.travel?.traveled,
            Constants.LABEL_WEATHER_TEMPERATURE to log.additionalData?.weather?.temperature?.toLong(),
            Constants.LABEL_WEATHER_HUMIDITY to log.additionalData?.weather?.humidity?.toLong()
        )
        data[USER_ID] = userData

        val parsedLog = parseDocumentData(userId = BAD_USER_ID, data = data)

        Assert.assertNull(parsedLog)
    }

    @Test
    fun `parse document data from fb wrong data parse exception`() {
        val log = getDailyLog(true)
        val data = mutableMapOf<String, Any>()
        val userData = hashMapOf(
            Constants.LABEL_DATE to Timestamp(log.date)
        )
        data[USER_ID] = userData

        val parsedLog = parseDocumentData(userId = USER_ID, data = data)

        Assert.assertNull(parsedLog)
    }
}