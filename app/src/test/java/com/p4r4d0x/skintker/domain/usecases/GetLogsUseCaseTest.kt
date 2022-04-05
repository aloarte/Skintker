package com.p4r4d0x.skintker.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.data.repository.LogsManagementRepository
import com.p4r4d0x.skintker.di.CoroutinesTestRule
import com.p4r4d0x.skintker.di.KoinBaseTest
import com.p4r4d0x.skintker.di.KoinTestApplication
import com.p4r4d0x.skintker.di.testRepositoriesModule
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.bo.IrritationBO
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
class GetLogsUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    companion object {
        const val USER_ID = "user_id"
    }

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val logsRepository: LogsManagementRepository by inject()

    private lateinit var useCase: GetLogsUseCase

    @Before
    fun setUp() {
        useCase = GetLogsUseCase(logsRepository)
    }

    @Test
    fun `test get logs`() {
        val logList = listOf(
            DailyLogBO(Date(), IrritationBO(1, emptyList()), foodList = emptyList()),
            DailyLogBO(Date(), IrritationBO(2, emptyList()), foodList = emptyList())
        )

        coEvery {
            logsRepository.getAllLogsWithFirebase(USER_ID)
        } returns logList

        val logsObtained = runBlocking { useCase.run(GetLogsUseCase.Params(USER_ID)) }

        coVerify { logsRepository.getAllLogsWithFirebase(USER_ID) }
        Assertions.assertEquals(logList, logsObtained)
    }
}