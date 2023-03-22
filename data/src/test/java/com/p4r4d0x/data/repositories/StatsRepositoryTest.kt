package com.p4r4d0x.data.repositories

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.datasources.StatsDatasource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.room.*
import com.p4r4d0x.data.testutils.TestData.USER_ID
import com.p4r4d0x.data.testutils.TestData.stats
import com.p4r4d0x.data.testutils.testDatasourcesModule
import com.p4r4d0x.data.testutils.testRepositoriesModule
import com.p4r4d0x.domain.bo.*
import com.p4r4d0x.domain.repository.StatsRepository
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.core.component.inject
import org.robolectric.annotation.Config
import java.util.*

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class StatsRepositoryTest :
    KoinBaseTest(testRepositoriesModule, testDatasourcesModule) {

    private val datasource: StatsDatasource by inject()

    private lateinit var repository: StatsRepository

    @Before
    fun setUp() {
        repository = StatsRepositoryImpl(datasource)
    }

    @Test
    fun `test get user stats success`() {
        coEvery { datasource.getUserStats(USER_ID) } returns ApiResult.Success(stats)

        val userStats = runBlocking { repository.getStats(USER_ID) }

        coVerify { datasource.getUserStats(USER_ID) }
        Assertions.assertEquals(stats, userStats)
    }

    @Test
    fun `test get user stats error`() {
        coEvery { datasource.getUserStats(USER_ID) } returns ApiResult.Error(
            -1,
            "Error retrieving stats contents"
        )

        val userStats = runBlocking { repository.getStats(USER_ID) }

        coVerify { datasource.getUserStats(USER_ID) }
        Assertions.assertNull(userStats)
    }

}