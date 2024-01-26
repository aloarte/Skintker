package com.p4r4d0x.data.utils

import android.content.res.Resources
import com.example.data.R
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
import com.p4r4d0x.domain.bo.*
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
        private var FOURTH_QUESTION_ANSWER_SINGLE_A = R.string.question_4_answer_1
        private var FOURTH_QUESTION_ANSWER_SINGLE_B = R.string.question_4_answer_2
        private var FOURTH_QUESTION_ANSWER_SINGLE_C = R.string.question_4_answer_3
        private var FOURTH_QUESTION_ANSWER_SINGLE_D = R.string.question_4_answer_4
        private var FIFTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private var SIXTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private var SEVENTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private const val EIGHT_QUESTION_ANSWER_SLIDER_1 = 0f
        private const val EIGHT_QUESTION_ANSWER_SLIDER_2 = 0f
        private const val NINTH_QUESTION_ANSWER_INPUT = "AnswerInput"
        private const val NINTH_QUESTION_ANSWER_INPUT_LOWER_CASE = "answerinput"
        private var NINTH_QUESTION_ANSWER_SINGLE = R.string.single_answer_2
        private var TENTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private var ELEVENTH_QUESTION_ANSWER_MULTIPLE =
            setOf(R.string.multiple_answer_1, R.string.multiple_answer_2)
        private const val SINGLE_ANSWER_1_VALUE = "No alcohol"
        private const val SINGLE_ANSWER_2_VALUE = "Single answer 2"
        private const val MULTIPLE_ANSWER_1_VALUE = "Multiple answer 1"
        private const val MULTIPLE_ANSWER_2_VALUE = "Multiple answer 2"
        private const val ALCOHOL_QUESTION_ANSWER_1 = "No alcohol ingested"
        private const val ALCOHOL_QUESTION_ANSWER_2 = "Took a few beers"
        private const val ALCOHOL_QUESTION_ANSWER_3 = "Took a few wine cups"
        private const val ALCOHOL_QUESTION_ANSWER_4 = "Had some drinks"
        private const val ALCOHOL_QUESTION_ANSWER_5 = "Mixed"
        private const val TRAVEL_QUESTION_ANSWER = "Yes, I traveled"
    }

    private lateinit var resources: Resources

    @Before
    fun setup() {
        mockResources()
    }

    private fun mockResources() {
        resources = mockk()
        every { resources.getString(R.string.no_alcohol) } returns SINGLE_ANSWER_1_VALUE
        every { resources.getString(R.string.alcohol_beer) } returns SINGLE_ANSWER_1_VALUE
        every { resources.getString(R.string.alcohol_wine) } returns SINGLE_ANSWER_1_VALUE
        every { resources.getString(R.string.alcohol_distilled) } returns SINGLE_ANSWER_1_VALUE
        every { resources.getString(R.string.single_answer_2) } returns SINGLE_ANSWER_2_VALUE
        every { resources.getString(R.string.multiple_answer_1) } returns MULTIPLE_ANSWER_1_VALUE
        every { resources.getString(R.string.multiple_answer_2) } returns MULTIPLE_ANSWER_2_VALUE
        every { resources.getString(R.string.question_4_answer_1) } returns ALCOHOL_QUESTION_ANSWER_1
        every { resources.getString(R.string.question_4_answer_2) } returns ALCOHOL_QUESTION_ANSWER_2
        every { resources.getString(R.string.question_4_answer_3) } returns ALCOHOL_QUESTION_ANSWER_3
        every { resources.getString(R.string.question_4_answer_4) } returns ALCOHOL_QUESTION_ANSWER_4
        every { resources.getString(R.string.question_4_answer_5) } returns ALCOHOL_QUESTION_ANSWER_5
        every { resources.getString(R.string.question_7_answer_1) } returns TRAVEL_QUESTION_ANSWER
    }

    private fun getAnswerList(
        expandBeerQuestion: Boolean,
        expandWineQuestion: Boolean,
        expandDistilledQuestion: Boolean
    ): List<Answer<*>> {
        val answerList = mutableListOf<Answer<*>>()
        answerList.add(Answer.Slider(FIRST_QUESTION_ANSWER_SLIDER))
        answerList.add(Answer.MultipleChoice(SECOND_QUESTION_ANSWER_MULTIPLE))
        answerList.add(Answer.Slider(THIRD_QUESTION_ANSWER_SLIDER))
        val alcohol = if (expandBeerQuestion) FOURTH_QUESTION_ANSWER_SINGLE_B
        else if (expandWineQuestion) FOURTH_QUESTION_ANSWER_SINGLE_C
        else if (expandDistilledQuestion) FOURTH_QUESTION_ANSWER_SINGLE_D
        else FOURTH_QUESTION_ANSWER_SINGLE_A
        answerList.add(Answer.SingleChoice(alcohol))
        if (expandBeerQuestion) {
            answerList.add(Answer.MultipleChoice(FIFTH_QUESTION_ANSWER_MULTIPLE))
        }
        if (expandWineQuestion) {
            answerList.add(Answer.MultipleChoice(SIXTH_QUESTION_ANSWER_MULTIPLE))
        }
        if (expandDistilledQuestion) {
            answerList.add(Answer.MultipleChoice(SEVENTH_QUESTION_ANSWER_MULTIPLE))
        }
        answerList.add(
            Answer.DoubleSlider(
                EIGHT_QUESTION_ANSWER_SLIDER_1, EIGHT_QUESTION_ANSWER_SLIDER_2
            )
        )
        answerList.add(
            Answer.SingleTextInputSingleChoice(
                NINTH_QUESTION_ANSWER_INPUT,
                NINTH_QUESTION_ANSWER_SINGLE
            )
        )
        answerList.add(Answer.MultipleChoice(TENTH_QUESTION_ANSWER_MULTIPLE))
        answerList.add(Answer.MultipleChoice(ELEVENTH_QUESTION_ANSWER_MULTIPLE))
        return answerList
    }

    private fun getDailyLog(
        expandBeerQuestion: Boolean,
        expandWineQuestion: Boolean,
        expandDistilledQuestion: Boolean,
        alcoholLevel: AlcoholLevel
    ) =
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
                weather = WeatherBO(
                    humidity = EIGHT_QUESTION_ANSWER_SLIDER_1.toInt(),
                    temperature = EIGHT_QUESTION_ANSWER_SLIDER_2.toInt()
                ),
                travel = TravelBO(
                    traveled = false,
                    city = NINTH_QUESTION_ANSWER_INPUT_LOWER_CASE
                ),
                alcohol = AlcoholBO(
                    level = alcoholLevel,
                    beers = if (expandBeerQuestion) listOf(
                        MULTIPLE_ANSWER_1_VALUE,
                        MULTIPLE_ANSWER_2_VALUE
                    ) else emptyList(),
                    wines = if (expandWineQuestion) listOf(
                        MULTIPLE_ANSWER_1_VALUE,
                        MULTIPLE_ANSWER_2_VALUE
                    ) else emptyList(),
                    distilledDrinks = if (expandDistilledQuestion) listOf(
                        MULTIPLE_ANSWER_1_VALUE,
                        MULTIPLE_ANSWER_2_VALUE
                    ) else emptyList()
                )
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
        val answerList = getAnswerList(
            expandBeerQuestion = true,
            expandWineQuestion = false,
            expandDistilledQuestion = false
        )

        val surveyLog = createLogFromSurvey(
            date = DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(
            expandBeerQuestion = true,
            expandWineQuestion = false,
            expandDistilledQuestion = false,
            AlcoholLevel.Beer
        )
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `test create log from survey with wine question`() {
        val answerList = getAnswerList(
            expandBeerQuestion = false,
            expandWineQuestion = true,
            expandDistilledQuestion = false
        )

        val surveyLog = createLogFromSurvey(
            date = DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(
            expandBeerQuestion = false,
            expandWineQuestion = true,
            expandDistilledQuestion = false,
            AlcoholLevel.Wine
        )
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `test create log from survey with distilled question`() {
        val answerList = getAnswerList(
            expandBeerQuestion = false,
            expandWineQuestion = false,
            expandDistilledQuestion = true
        )

        val surveyLog = createLogFromSurvey(
            date = DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(
            expandBeerQuestion = false,
            expandWineQuestion = false,
            expandDistilledQuestion = true,
            AlcoholLevel.Distilled
        )
        Assert.assertEquals(expectedLog, surveyLog)
    }

    @Test
    fun `test create log from survey without custom alcohol question`() {
        val answerList = getAnswerList(
            expandBeerQuestion = false,
            expandWineQuestion = false,
            expandDistilledQuestion = false
        )

        val surveyLog = createLogFromSurvey(
            DataParser.getCurrentFormattedDate(),
            answers = answerList,
            resources = resources
        )

        val expectedLog = getDailyLog(
            expandBeerQuestion = false,
            expandWineQuestion = false,
            expandDistilledQuestion = false,
            alcoholLevel = AlcoholLevel.None
        )
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