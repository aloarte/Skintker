package com.p4r4d0x.skintker.viewmodel

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.di.KoinBaseTest
import com.p4r4d0x.skintker.di.KoinTestApplication
import com.p4r4d0x.skintker.di.testUseCasesModule
import com.p4r4d0x.skintker.di.testViewmodelModule
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import com.p4r4d0x.skintker.domain.usecases.GetLogsUseCase
import com.p4r4d0x.skintker.presenter.home.viewmodel.HomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config
import java.util.*


//@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(
    manifest = Config.NONE,
    application = KoinTestApplication::class,
    sdk = [Build.VERSION_CODES.O]
)
class HomeViewModelTest : KoinBaseTest(testViewmodelModule, testUseCasesModule) {

//    @get:Rule
//    val coroutinesTestRule = CoroutinesTestRule()
//    @get:Rule
//    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getLogsUseCase: GetLogsUseCase by inject()
    private val addLogsUseCase: AddLogUseCase by inject()

    private lateinit var viewModelSUT: HomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = HomeViewModel(getLogsUseCase)
    }

    @Test
    fun getLogs() {
        val logsResult = slot<(List<DailyLogBO>?) -> Unit>()
        val logs = listOf(DailyLogBO(date = Date(), foodList = emptyList()))
        every {
            getLogsUseCase.invoke(scope = any(), resultCallback = capture(logsResult))
        } answers {
            logsResult.captured(logs)
        }

        viewModelSUT.getLogs()

        val logsLoaded = viewModelSUT.logList.getOrAwaitValue()
        verify { viewModelSUT.getLogs() }
        Assert.assertEquals(logs, logsLoaded)

    }
}