package com.p4r4d0x.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.bo.DailyLogBO
import com.p4r4d0x.domain.bo.IrritationBO
import com.p4r4d0x.domain.bo.PossibleCausesBO
import com.p4r4d0x.domain.repository.LogsManagementRepository
import com.p4r4d0x.domain.testRepositoriesModule
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
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class GetQueriedLogsUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    companion object {
        private const val IRRITATION_LEVEL = 7
    }

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val logsRepository: LogsManagementRepository by inject()

    private lateinit var useCase: GetQueriedLogsUseCase

    private val params = GetQueriedLogsUseCase.Params(
        irritationLevel = IRRITATION_LEVEL,
        minLogs = 3,
        foodThreshold = 1f,
        zonesThreshold = 1f,
        alcoholThreshold = 1f,
        travelThreshold = 1f,
        stressThresholds = Pair(1, 1f),
        weatherThresholds = Pair(1f, 1f)
    )

    @Before
    fun setUp() {
        useCase = GetQueriedLogsUseCase(logsRepository)
    }

    @Test
    fun `test get queried logs by irritation intensity`() {
        val logList = listOf(
           DailyLogBO(
                Date(),
                IrritationBO(8, emptyList()),
                foodList = emptyList()
            ),
            DailyLogBO(
                Date(),
                IrritationBO(9, emptyList()),
                foodList = emptyList()
            )
        )

        coEvery {
            logsRepository.getLogsByIrritationLevel(IRRITATION_LEVEL)
        } returns logList

        val possibleCauses = runBlocking { useCase.run(params) }

        val expectedPossibleCauses = PossibleCausesBO(
            false,
            alcoholCause = false,
            dietaryCauses = emptyList(),
            mostAffectedZones = emptyList(),
            stressCause = PossibleCausesBO.StressCauseBO(
                averageLevel = -1,
                possibleCause = false
            ),
            travelCause = PossibleCausesBO.TravelCauseBO(
                city = null,
                possibleCause = false
            ),
            weatherCause = Pair(
                first = PossibleCausesBO.WeatherCauseBO(
                    possibleCause = false,
                    averageValue = -1,
                    weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.HUMIDITY
                ),
                second = PossibleCausesBO.WeatherCauseBO(
                    possibleCause = false,
                    averageValue = -1,
                    weatherType = PossibleCausesBO.WeatherCauseBO.WeatherType.TEMPERATURE
                )
            )
        )
        coVerify { logsRepository.getLogsByIrritationLevel(IRRITATION_LEVEL) }
        Assertions.assertEquals(expectedPossibleCauses, possibleCauses)
    }
}