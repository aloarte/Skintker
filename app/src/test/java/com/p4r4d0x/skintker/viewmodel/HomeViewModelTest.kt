package com.p4r4d0x.skintker.viewmodel

import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.di.*
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase
import com.p4r4d0x.skintker.domain.usecases.GetQueriedLogsUseCase
import com.p4r4d0x.skintker.domain.usecases.UpdateLogsUseCase
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
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
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class HomeViewModelTest : KoinBaseTest(testRepositoriesModule, testUseCasesModule) {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getLogsUseCase: GetLogsUseCase by inject()
    private val getQueriedLogsUseCase: GetQueriedLogsUseCase by inject()
    private val updateLogsUseCase: UpdateLogsUseCase by inject()


    private lateinit var viewModelSUT: HomeViewModel

    companion object {
        const val USER_ID = "user_id"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = HomeViewModel(getLogsUseCase, getQueriedLogsUseCase, updateLogsUseCase)
    }

    @Test
    fun `test home view model get logs`() = coroutinesTestRule.runBlockingTest {
        val logsResult = slot<(List<DailyLogBO>?) -> Unit>()
        val logs = listOf(DailyLogBO(date = Date(), foodList = emptyList()))

        every {
            getLogsUseCase.invoke(scope = any(), resultCallback = capture(logsResult))
        } answers {
            logsResult.captured(logs)
        }

        every {
            updateLogsUseCase.invoke(
                scope = any(),
                params = UpdateLogsUseCase.Params(user = USER_ID, any()),
                resultCallback = any()
            )
        } returns Unit

        viewModelSUT.getLogs(USER_ID)

        val logsLoaded = viewModelSUT.logList.getOrAwaitValue()
        Assert.assertEquals(logs, logsLoaded)

    }
}