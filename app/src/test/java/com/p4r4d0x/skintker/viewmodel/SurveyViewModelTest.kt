package com.p4r4d0x.skintker.viewmodel

import android.content.res.Resources
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.parsers.DataParser
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.domain.usecases.AddLogUseCase
import com.p4r4d0x.domain.usecases.GetLogUseCase
import com.p4r4d0x.domain.usecases.GetSurveyUseCase
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.skintker.CoroutinesTestRule
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.di.testUseCasesModule
import com.p4r4d0x.skintker.di.testViewModelModule
import com.p4r4d0x.skintker.presenter.survey.LogState
import com.p4r4d0x.skintker.presenter.survey.SurveyState
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class SurveyViewModelTest : KoinBaseTest(testViewModelModule, testUseCasesModule) {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val addLogsUseCase: AddLogUseCase by inject()

    private val getLogUseCase: GetLogUseCase by inject()

    private val getSurveyUseCase: GetSurveyUseCase by inject()

    private lateinit var viewModelSUT: SurveyViewModel

    lateinit var resources: Resources

    companion object {
        const val USER_ID = "user_id"
        private const val ALCOHOL_QUESTION_ANSWER_1 = "No alcohol ingested"
        private const val ALCOHOL_QUESTION_ANSWER_2 = "Took a few beers"
        private const val ALCOHOL_QUESTION_ANSWER_3 = "Took a few wine cups"
        private const val ALCOHOL_QUESTION_ANSWER_4 = "Had some drinks"
        private const val TRAVEL_QUESTION_ANSWER = "Yes, I traveled"
    }

    private val date = DataParser.getCurrentFormattedDate()

    private val question = Question(
        id = Constants.FOURTH_QUESTION_NUMBER,
        questionText = R.string.question_4_title,
        answer = PossibleAnswer.SingleChoice(
            listOf(
                R.string.question_4_answer_1,
                R.string.question_4_answer_2,
                R.string.question_4_answer_3,
                R.string.question_4_answer_4
            )
        )
    )
    private val questionsSurveyState = SurveyState.Questions(
        listOf(
            LogState(
                question = question,
                index = 0,
                totalCount = 1,
                showPrevious = false,
                showDone = true
            )
        ), date
    )

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = SurveyViewModel(addLogsUseCase, getLogUseCase, getSurveyUseCase)
    }

    @Test
    fun `test survey view model check log reported today`() = coroutinesTestRule.runBlockingTest {
        val logResult = slot<(DailyLogBO?) -> Unit>()
        val log = DailyLogBO(date = date, foodList = emptyList())
        every {
            getLogUseCase.invoke(
                scope = any(),
                params = GetLogUseCase.Params(date),
                resultCallback = capture(logResult)
            )
        } answers {
            logResult.captured(log)
        }

        viewModelSUT.checkIfLogIsAlreadyInserted()

        val logReported = viewModelSUT.logReported.getOrAwaitValue()
        Assert.assertTrue(logReported)
    }

    @Test
    fun `test survey view model check log not reported today`() =
        coroutinesTestRule.runBlockingTest {
            val logResult = slot<(DailyLogBO?) -> Unit>()
            every {
                getLogUseCase.invoke(
                    scope = any(),
                    params = GetLogUseCase.Params(date),
                    resultCallback = capture(logResult)
                )
            } answers {
                logResult.captured(null)
            }

            viewModelSUT.checkIfLogIsAlreadyInserted()

            val logReported = viewModelSUT.logReported.getOrAwaitValue()
            Assert.assertFalse(logReported)
        }

    @Test
    fun `test survey view model compute result`() =
        coroutinesTestRule.runBlockingTest {
            val dailyLog = DailyLogBO(
                date, IrritationBO(0, emptyList()),
                AdditionalDataBO(
                    0,
                    AdditionalDataBO.WeatherBO(0, 0),
                    AdditionalDataBO.TravelBO(false, ""),
                    AlcoholLevel.None,
                    emptyList()
                ), emptyList()
            )
            resources = mockk()
            every { resources.getString(R.string.question_4_answer_1) } returns ALCOHOL_QUESTION_ANSWER_1
            every { resources.getString(R.string.question_4_answer_2) } returns ALCOHOL_QUESTION_ANSWER_2
            every { resources.getString(R.string.question_4_answer_3) } returns ALCOHOL_QUESTION_ANSWER_3
            every { resources.getString(R.string.question_4_answer_4) } returns ALCOHOL_QUESTION_ANSWER_4
            every { resources.getString(R.string.question_7_answer_1) } returns TRAVEL_QUESTION_ANSWER
            val addLogResult = slot<(Boolean?) -> Unit>()
            every {
                addLogsUseCase.invoke(
                    scope = any(),
                    params = AddLogUseCase.Params(userId = USER_ID, dailyLog),
                    resultCallback = capture(addLogResult)
                )
            } answers {
                addLogResult.captured(true)
            }

            viewModelSUT.computeResult(USER_ID, questionsSurveyState, resources)

            val logReported = viewModelSUT.uiState.getOrAwaitValue()
            Assert.assertEquals(SurveyState.Result, logReported)
        }

    @Test
    fun `test survey view model load date  pick date`() =
        coroutinesTestRule.runBlockingTest {
            viewModelSUT.loadDate(true)

            val surveyState = viewModelSUT.uiState.getOrAwaitValue()

            Assert.assertEquals(SurveyState.PickDate, surveyState)
        }

    @Test
    fun `test survey view model load date dont pick date`() =
        coroutinesTestRule.runBlockingTest {
            loadQuestionsArrange()

            viewModelSUT.loadDate(false)

            loadQuestionsAssert()
        }

    @Test
    fun `test survey view model load questions`() =
        coroutinesTestRule.runBlockingTest {
            loadQuestionsArrange()

            viewModelSUT.loadQuestions(date)

            loadQuestionsAssert()
        }

    private fun loadQuestionsArrange() {
        val survey = Survey(mutableListOf(question))
        val surveyResult = slot<(Survey?) -> Unit>()
        every {
            getSurveyUseCase.invoke(
                scope = any(),
                resultCallback = capture(surveyResult)
            )
        } answers {
            surveyResult.captured(survey)
        }
    }

    private fun loadQuestionsAssert() {
        val surveyState = viewModelSUT.uiState.getOrAwaitValue()
        Assert.assertEquals(questionsSurveyState, surveyState)
    }
}