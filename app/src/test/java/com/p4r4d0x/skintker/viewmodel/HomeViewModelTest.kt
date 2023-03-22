package com.p4r4d0x.skintker.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.usecases.GetLogsUseCase
import com.p4r4d0x.domain.usecases.GetStatsUseCase
import com.p4r4d0x.domain.usecases.RemoveLogUseCase
import com.p4r4d0x.skintker.CoroutinesTestRule
import com.p4r4d0x.skintker.TestData.dailyLog
import com.p4r4d0x.skintker.di.testUseCasesModule
import com.p4r4d0x.skintker.getOrAwaitValue
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config
import java.util.*

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class HomeViewModelTest : KoinBaseTest(testUseCasesModule) {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getLogsUseCase: GetLogsUseCase by inject()
    private val getStatsUseCase: GetStatsUseCase by inject()
    private val removeLogUseCase: RemoveLogUseCase by inject()

    private lateinit var viewModelSUT: HomeViewModel

    companion object {
        const val USER_ID = "user_id"
        val logDate = Date()
        val logs = listOf(dailyLog)
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = HomeViewModel(getLogsUseCase, getStatsUseCase, removeLogUseCase)
    }

    @Test
    fun `test home view model get logs`() =
        coroutinesTestRule.runBlockingTest {
            mockGetAllLogs()

            viewModelSUT.getLogs(USER_ID)

            val logsLoaded = viewModelSUT.logList.getOrAwaitValue()
            Assert.assertEquals(logs, logsLoaded)

        }

    @Test
    fun `test home view model get user stats`() =
        coroutinesTestRule.runBlockingTest {
            val causes = generateCauses()
            val causesResult = slot<(PossibleCausesBO?) -> Unit>()
            val useCaseParams = GetStatsUseCase.Params(USER_ID)
            every {
                getStatsUseCase.invoke(
                    scope = any(),
                    resultCallback = capture(causesResult),
                    params = useCaseParams
                )
            } answers {
                causesResult.captured(causes)
            }

            viewModelSUT.getUserStats(USER_ID)

            val possibleCauses = viewModelSUT.possibleCauses.getOrAwaitValue()
            Assert.assertEquals(causes, possibleCauses)
        }

    @Ignore
    @Test
    fun `test home view model delete logs`() =
        coroutinesTestRule.runBlockingTest {
            val removeResult = slot<(Boolean?) -> Unit>()
            val useCaseParams = RemoveLogUseCase.Params(USER_ID, logDate)
            every {
                removeLogUseCase.invoke(
                    scope = any(),
                    resultCallback = capture(removeResult),
                    params = useCaseParams
                )
            } answers {
                removeResult.captured(true)
            }
            mockGetAllLogs()
            viewModelSUT.removeLog(USER_ID, logDate)
        }

    private fun mockGetAllLogs() {
        val logsResult = slot<(List<DailyLogBO>?) -> Unit>()
        every {
            getLogsUseCase.invoke(
                scope = any(),
                resultCallback = capture(logsResult),
                params = GetLogsUseCase.Params(USER_ID)
            )
        } answers {
            logsResult.captured(logs)
        }
    }

    private fun generateCauses() = PossibleCausesBO(
        true, emptyList(), true, emptyList(),
        PossibleCausesBO.StressCauseBO(true, 5),
        PossibleCausesBO.TravelCauseBO(true, ""),
        Pair(
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE,
                true,
                2
            ),
            PossibleCausesBO.WeatherCauseBO(
                PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY,
                true,
                2
            )
        )
    )

}