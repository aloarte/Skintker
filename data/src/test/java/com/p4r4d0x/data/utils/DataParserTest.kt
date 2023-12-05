package com.p4r4d0x.data.utils

import android.content.res.Resources
import com.example.data.R
import com.google.firebase.Timestamp
import com.p4r4d0x.data.datasources.StatsDataSourceTest
import com.p4r4d0x.data.dto.SkintkvaultResponseLogs
import com.p4r4d0x.data.dto.SkintkvaultResponseStats
import com.p4r4d0x.data.dto.logs.LogListResponse
import com.p4r4d0x.data.dto.stats.StatsResponse
import com.p4r4d0x.data.parsers.DataParser
import com.p4r4d0x.data.parsers.DataParser.createLogFromSurvey
import com.p4r4d0x.data.parsers.DataParser.toDailyLogContents
import com.p4r4d0x.data.parsers.DataParser.toPossibleCauses
import com.p4r4d0x.data.testutils.TestData.completeStatsBo
import com.p4r4d0x.data.testutils.TestData.completeStatsDto
import com.p4r4d0x.data.testutils.TestData.incompleteStatsBo
import com.p4r4d0x.data.testutils.TestData.incompleteStatsDto
import com.p4r4d0x.data.testutils.TestData.log
import com.p4r4d0x.data.testutils.TestData.logDto
import com.p4r4d0x.data.testutils.Utils.buildResponse
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.domain.utils.Constants
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
        private var FOURTH_QUESTION_ANSWER_SINGLE = R.string.card_no_alcohol
        private var FIFTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private const val SIXTH_QUESTION_ANSWER_SLIDER_1 = 3f
        private const val SIXTH_QUESTION_ANSWER_SLIDER_2 = 7f
        private const val SEVENTH_QUESTION_ANSWER_INPUT = "AnswerInput"
        private const val SEVENTH_QUESTION_ANSWER_INPUT_LOWER_CASE = "answerinput"
        private var SEVENTH_QUESTION_ANSWER_SINGLE = R.string.single_answer_2
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

    private fun getDailyLog(expandBeerQuestion: Boolean, alcoholLevel: AlcoholLevel) =
        DailyLogBO(
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
                alcoholLevel = alcoholLevel,
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
    fun `test create log from survey with beer question`() {
        val answerList = getAnswerList(expandBeerQuestion = true)

        val surveyLog = createLogFromSurvey(
            date = DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(expandBeerQuestion = true, AlcoholLevel.None)
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `test create log from survey without beer question`() {
        val answerList = getAnswerList(expandBeerQuestion = false)

        val surveyLog = createLogFromSurvey(
            DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(expandBeerQuestion = false, alcoholLevel = AlcoholLevel.None)
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `test to possible causes incomplete item`() {
        val responseStats = SkintkvaultResponseStats(
            statusCode = 200,
            content = StatsResponse("", incompleteStatsDto)
        )

        val surveyLog = responseStats.toPossibleCauses()

        Assert.assertEquals(incompleteStatsBo, surveyLog)
    }

    @Test
    fun `test to possible causes complete item`() {
        val responseStats = SkintkvaultResponseStats(
            statusCode = 200,
            content = StatsResponse("", completeStatsDto)
        )

        val surveyLog = responseStats.toPossibleCauses()

        Assert.assertEquals(completeStatsBo, surveyLog)
    }

    @Test
    fun `test to daily log contents`() {
        val responseStats = SkintkvaultResponseLogs(
            statusCode = 200,
            content = LogListResponse("", listOf(logDto), 1)
        )

        val surveyLog = responseStats.toDailyLogContents()

        Assert.assertEquals(DailyLogContentsBO(1, listOf(log)), surveyLog)
    }

    @Test
    fun `test to daily log contents no response content`() {
        val responseStats = SkintkvaultResponseLogs(
            statusCode = 200,
            content = null
        )

        val surveyLog = responseStats.toDailyLogContents()

        Assert.assertEquals(DailyLogContentsBO(), surveyLog)
    }
}