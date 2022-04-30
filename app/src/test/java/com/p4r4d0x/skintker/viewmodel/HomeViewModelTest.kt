package com.p4r4d0x.skintker.viewmodel

import android.content.SharedPreferences
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.usecases.GetLogsUseCase
import com.p4r4d0x.domain.usecases.GetQueriedLogsUseCase
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.skintker.CoroutinesTestRule
import com.p4r4d0x.skintker.di.testUseCasesModule
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
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
    private val getQueriedLogsUseCase: GetQueriedLogsUseCase by inject()

    private lateinit var viewModelSUT: HomeViewModel

    companion object {
        const val USER_ID = "user_id"
        const val PREFERENCE_IRRITATION = 1
        const val PREFERENCE_LOGS = 2
        const val PREFERENCE_FOOD = 3f
        const val PREFERENCE_ZONES = 4f
        const val PREFERENCE_TRAVEL = 5f
        const val PREFERENCE_ALCOHOL = 6f
        const val PREFERENCE_STRESS_VALUE = 7
        const val PREFERENCE_STRESS_TH = 8f
        const val PREFERENCE_TEMPERATURE_TH = 9f
        const val PREFERENCE_HUMIDITY_TH = 10f
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = HomeViewModel(getLogsUseCase, getQueriedLogsUseCase)
    }

    @Test
    fun `test home view model get logs`() =
        coroutinesTestRule.runBlockingTest {
            val logsResult = slot<(List<DailyLogBO>?) -> Unit>()
            val logs = listOf(
                DailyLogBO(
                    date = Date(),
                    foodList = emptyList()
                )
            )
            every {
                getLogsUseCase.invoke(
                    scope = any(),
                    resultCallback = capture(logsResult),
                    params = GetLogsUseCase.Params(USER_ID)
                )
            } answers {
                logsResult.captured(logs)
            }

            viewModelSUT.getLogs(USER_ID)

            val logsLoaded = viewModelSUT.logList.getOrAwaitValue()
            Assert.assertEquals(logs, logsLoaded)

        }

    @Test
    fun `test home view model get logs by intensity level`() =
        coroutinesTestRule.runBlockingTest {
            val causes = generateCauses()
            val preferences = mockPreferences()
            val causesResult = slot<(PossibleCausesBO) -> Unit>()
            val useCaseParams = GetQueriedLogsUseCase.Params(
                PREFERENCE_IRRITATION,
                PREFERENCE_LOGS,
                PREFERENCE_FOOD,
                PREFERENCE_ZONES,
                PREFERENCE_ALCOHOL,
                PREFERENCE_TRAVEL,
                Pair(PREFERENCE_STRESS_VALUE, PREFERENCE_STRESS_TH),
                Pair(PREFERENCE_TEMPERATURE_TH, PREFERENCE_HUMIDITY_TH)
            )
            every {
                getQueriedLogsUseCase.invoke(
                    scope = any(),
                    resultCallback = capture(causesResult),
                    params = useCaseParams
                )
            } answers {
                causesResult.captured(causes)
            }

            viewModelSUT.getLogsByIntensityLevel(preferences)

            val possibleCauses = viewModelSUT.possibleCauses.getOrAwaitValue()
            Assert.assertEquals(causes, possibleCauses)
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

    private fun mockPreferences(): SharedPreferences {
        val preferences: SharedPreferences = mockk()

        every {
            preferences.getInt(
                Constants.PREFERENCES_IRRITATION_NUMBER,
                Constants.DEFAULT_IRRITATION_LEVEL_THRESHOLD
            )
        } returns PREFERENCE_IRRITATION
        every {
            preferences.getInt(
                Constants.PREFERENCES_MIN_LOGS,
                Constants.DEFAULT_MIN_LOGS
            )
        } returns PREFERENCE_LOGS
        every {
            preferences.getFloat(
                Constants.PREFERENCES_FOOD_THRESHOLD,
                Constants.DEFAULT_FOOD_THRESHOLD
            )
        } returns PREFERENCE_FOOD
        every {
            preferences.getFloat(
                Constants.PREFERENCES_ZONES_THRESHOLD,
                Constants.DEFAULT_ZONES_THRESHOLD
            )
        } returns PREFERENCE_ZONES
        every {
            preferences.getFloat(
                Constants.PREFERENCES_TRAVEL_THRESHOLD,
                Constants.DEFAULT_TRAVEL_THRESHOLD
            )
        } returns PREFERENCE_TRAVEL
        every {
            preferences.getFloat(
                Constants.PREFERENCES_ALCOHOL_THRESHOLD,
                Constants.DEFAULT_ALCOHOL_THRESHOLD
            )
        } returns PREFERENCE_ALCOHOL
        every {
            preferences.getInt(
                Constants.PREFERENCES_STRESS_VALUE,
                Constants.DEFAULT_STRESS_VALUE
            )
        } returns PREFERENCE_STRESS_VALUE
        every {
            preferences.getFloat(
                Constants.PREFERENCES_STRESS_THRESHOLD,
                Constants.DEFAULT_STRESS_THRESHOLD
            )
        } returns PREFERENCE_STRESS_TH
        every {
            preferences.getFloat(
                Constants.PREFERENCES_WEATHER_TEMPERATURE_THRESHOLD,
                Constants.DEFAULT_WEATHER_TEMPERATURE_THRESHOLD
            )
        } returns PREFERENCE_TEMPERATURE_TH
        every {
            preferences.getFloat(
                Constants.PREFERENCES_WEATHER_HUMIDITY_THRESHOLD,
                Constants.DEFAULT_WEATHER_HUMIDITY_THRESHOLD
            )
        } returns PREFERENCE_HUMIDITY_TH

        return preferences
    }
}