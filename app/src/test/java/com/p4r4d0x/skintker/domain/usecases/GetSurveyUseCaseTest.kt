package com.p4r4d0x.skintker.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.Constants
import com.p4r4d0x.skintker.data.repository.SurveyRepository
import com.p4r4d0x.skintker.di.CoroutinesTestRule
import com.p4r4d0x.skintker.di.KoinBaseTest
import com.p4r4d0x.skintker.di.KoinTestApplication
import com.p4r4d0x.skintker.di.testRepositoriesModule
import com.p4r4d0x.skintker.domain.log.PossibleAnswer
import com.p4r4d0x.skintker.domain.log.Question
import com.p4r4d0x.skintker.domain.log.Survey
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class GetSurveyUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val surveyRepository: SurveyRepository by inject()

    private lateinit var useCase: GetSurveyUseCase

    private val question = Question(
        id = Constants.FOURTH_QUESTION_NUMBER,
        questionText = R.string.question_4_title,
        answer = PossibleAnswer.SingleChoice(
            listOf(
                R.string.question_4_answer_1,
                R.string.question_4_answer_2,
                R.string.question_4_answer_3
            )
        )
    )

    @Before
    fun setUp() {
        useCase = GetSurveyUseCase(surveyRepository)
    }

    @Test
    fun `test get survey`() {
        val survey = Survey(listOf(question))
        coEvery {
            surveyRepository.getSurvey()
        } returns survey

        val surveyObtained = runBlocking { useCase.run() }

        coVerify { surveyRepository.getSurvey() }
        Assertions.assertEquals(survey, surveyObtained)
    }
}