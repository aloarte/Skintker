package com.p4r4d0x.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.CoroutinesTestRule
import com.p4r4d0x.domain.TestData.date
import com.p4r4d0x.domain.TestData.log
import com.p4r4d0x.domain.di.testRepositoriesModule
import com.p4r4d0x.domain.repository.ReportsManagementRepository
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
class GetLogUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val reportsRepository: ReportsManagementRepository by inject()

    private lateinit var useCase: GetLogUseCase

    @Before
    fun setUp() {
        useCase = GetLogUseCase(reportsRepository)
    }

    @Test
    fun `test get log success`() {
        val params = GetLogUseCase.Params(date)
        coEvery { reportsRepository.getReport(date) } returns log

        val logObtained = runBlocking { useCase.run(params) }

        coVerify { reportsRepository.getReport(date) }
        Assertions.assertEquals(log, logObtained)
    }

    @Test
    fun `test get log not found`() {
        val params = GetLogUseCase.Params(date)
        coEvery { reportsRepository.getReport(date) } returns null

        val logObtained = runBlocking { useCase.run(params) }

        coVerify { reportsRepository.getReport(date) }
        Assertions.assertNull(logObtained)
    }
}