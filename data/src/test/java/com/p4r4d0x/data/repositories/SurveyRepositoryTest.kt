package com.p4r4d0x.data.repositories

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.data.R
import com.p4r4d0x.data.datasources.SurveyDataSource
import com.p4r4d0x.data.testDatasourcesModule
import com.p4r4d0x.data.testRepositoriesModule
import com.p4r4d0x.domain.Constants
import com.p4r4d0x.domain.bo.PossibleAnswer
import com.p4r4d0x.domain.bo.Question
import com.p4r4d0x.domain.bo.Survey
import com.p4r4d0x.domain.repository.SurveyRepository
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.every
import io.mockk.verify
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class SurveyRepositoryTest : KoinBaseTest(testRepositoriesModule, testDatasourcesModule) {

    private val survey = Survey(
        listOf(
            Question(
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
        )
    )

    private val surveyDataSource: SurveyDataSource by inject()

    private lateinit var repository: SurveyRepository

    @Before
    fun setUp() {
        repository = SurveyRepositoryImpl(surveyDataSource)
    }

    @Test
    fun `test get survey`() {
        every {
            surveyDataSource.getSurvey()
        } returns survey

        val obtainedSurvey = repository.getSurvey()

        verify { surveyDataSource.getSurvey() }
        Assertions.assertEquals(survey, obtainedSurvey)
    }
}