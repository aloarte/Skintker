package com.p4r4d0x.skintker.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.di.*
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.parsers.DataParser
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import com.p4r4d0x.skintker.domain.usecases.GetLogUseCase
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
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

    private lateinit var viewModelSUT: SurveyViewModel

    private val date = DataParser.getCurrentFormattedDate()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = SurveyViewModel(addLogsUseCase, getLogUseCase)
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
}