package com.p4r4d0x.skintker.data

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.R
import com.p4r4d0x.skintker.data.datasources.SurveyDataSource
import com.p4r4d0x.skintker.data.repository.SurveyRepository
import com.p4r4d0x.skintker.data.repository.SurveyRepositoryImpl
import com.p4r4d0x.skintker.di.KoinBaseTest
import com.p4r4d0x.skintker.di.KoinTestApplication
import com.p4r4d0x.skintker.di.testDatasourcesModule
import com.p4r4d0x.skintker.di.testRepositoriesModule
import com.p4r4d0x.skintker.domain.log.PossibleAnswer
import com.p4r4d0x.skintker.domain.log.Question
import com.p4r4d0x.skintker.domain.log.Survey
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