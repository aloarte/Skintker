package com.p4r4d0x.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.bo.PossibleAnswer
import com.p4r4d0x.domain.bo.Question
import com.p4r4d0x.domain.bo.Survey
import com.p4r4d0x.domain.di.testRepositoriesModule
import com.p4r4d0x.domain.repository.SurveyRepository
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.test.CoroutinesTestRule
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.core.component.inject
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
        questionText = 2,
        answer = PossibleAnswer.SingleChoice(
            listOf(
               1
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