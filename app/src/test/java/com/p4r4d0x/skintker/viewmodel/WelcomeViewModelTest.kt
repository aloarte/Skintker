package com.p4r4d0x.skintker.viewmodel

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.di.KoinBaseTest
import com.p4r4d0x.skintker.di.KoinTestApplication
import com.p4r4d0x.skintker.di.testUseCasesModule
import com.p4r4d0x.skintker.di.testViewmodelModule
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.usecases.GetLogUseCase
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.slot
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Assert
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config
import java.util.*


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class WelcomeViewModelTest : KoinBaseTest(testViewmodelModule, testUseCasesModule) {

//    @get:Rule
//    val coroutinesTestRule = CoroutinesTestRule()
//    @get:Rule
//    val coroutinesTestRule = InstantTaskExecutorRule()

    private val getLogUseCase: GetLogUseCase by inject()

    private lateinit var viewModelSUT: WelcomeViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = WelcomeViewModel(getLogUseCase)
    }

    @Test
    fun `get logs`() {
        val logResult = slot<(DailyLogBO?) -> Unit>()
        val log = DailyLogBO(date = Date())
        every {
            getLogUseCase.invoke(
                scope = any(),
                params = GetLogUseCase.Params(Date()),
                resultCallback = capture(logResult)
            )
        } answers {
            logResult.captured(log)
        }

        viewModelSUT.checkLogReportedToday()

        val logsLoaded = viewModelSUT.logReported.getOrAwaitValue()
        verify { viewModelSUT.checkLogReportedToday() }
        Assert.assertEquals(log, logsLoaded)

    }
}