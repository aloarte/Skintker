package com.p4r4d0x.skintker.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.di.CoroutinesTestRule
import com.p4r4d0x.skintker.di.KoinBaseTest
import com.p4r4d0x.skintker.di.KoinTestApplication
import com.p4r4d0x.skintker.di.testRepositoriesModule
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
import com.p4r4d0x.skintker.domain.usecases.GetLogUseCase
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
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class GetLogUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val logsRepository: LogsManagementRepository by inject()

    private lateinit var useCase: GetLogUseCase

    @Before
    fun setUp() {
        useCase = GetLogUseCase(logsRepository)
    }

    @Test
    fun `test get log`() {
        val date = Date()
        val log = DailyLogBO(date, IrritationBO(6, emptyList()), foodList = emptyList())
        val params = GetLogUseCase.Params(date)

        coEvery {
            logsRepository.getLogByDate(date.time)
        } returns log

        val logObtained = runBlocking { useCase.run(params) }

        coVerify { logsRepository.getLogByDate(date.time) }
        Assertions.assertEquals(log, logObtained)
    }
}