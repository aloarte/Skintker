package com.p4r4d0x.skintker

import android.os.Build
import androidx.test.runner.AndroidJUnit4
import com.p4r4d0x.skintker.domain.AddLogUseCase
import com.p4r4d0x.skintker.domain.GetLogsUseCase
import com.p4r4d0x.skintker.presenter.viewmodel.MainViewModel
import io.mockk.MockKAnnotations
import io.mockk.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.java.KoinJavaComponent.inject
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class ExampleUnitTest : KoinBaseTest(viewModelModule) {


    private val getLogsUseCase by inject<GetLogsUseCase>()
    private val addLogsUseCase by inject<AddLogUseCase>()

    private lateinit var viewModelSUT: MainViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = MainViewModel(getLogsUseCase, addLogsUseCase)
    }

    @Test
    fun `dfdfsdf`() {
        viewModelSUT.getLogs()
        verify { viewModelSUT.getLogs() }

    }
}