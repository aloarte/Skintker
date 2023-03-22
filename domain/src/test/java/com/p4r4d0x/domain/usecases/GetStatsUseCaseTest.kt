package com.p4r4d0x.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.CoroutinesTestRule
import com.p4r4d0x.domain.TestData.USER_ID
import com.p4r4d0x.domain.TestData.stats
import com.p4r4d0x.domain.di.testRepositoriesModule
import com.p4r4d0x.domain.repository.StatsRepository
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
class GetStatsUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val statsRepository: StatsRepository by inject()

    private lateinit var useCase: GetStatsUseCase

    @Before
    fun setUp() {
        useCase = GetStatsUseCase(statsRepository)
    }

    @Test
    fun `test get user stats success`() {
        val params = GetStatsUseCase.Params(USER_ID)
        coEvery { statsRepository.getStats(USER_ID) } returns stats

        val statsObtained = runBlocking { useCase.run(params) }

        coVerify { statsRepository.getStats(USER_ID) }
        Assertions.assertEquals(stats, statsObtained)
    }

    @Test
    fun `test get user stats not found`() {
        val params = GetStatsUseCase.Params(USER_ID)
        coEvery { statsRepository.getStats(USER_ID) } returns null

        val statsObtained = runBlocking { useCase.run(params) }

        coVerify { statsRepository.getStats(USER_ID) }
        Assertions.assertNull(statsObtained)
    }
}