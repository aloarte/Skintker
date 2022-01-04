package com.p4r4d0x.skintker.viewmodel

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.skintker.di.KoinBaseTest
import com.p4r4d0x.skintker.di.KoinTestApplication
import com.p4r4d0x.skintker.di.testUseCasesModule
import com.p4r4d0x.skintker.di.testViewmodelModule
import com.p4r4d0x.skintker.domain.usecases.AddLogUseCase
import com.p4r4d0x.skintker.presenter.survey.viewmodel.SurveyViewModel
import io.mockk.MockKAnnotations
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.jupiter.api.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config


@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class SurveyViewModelTest : KoinBaseTest(testViewmodelModule, testUseCasesModule) {

//    @get:Rule
//    val coroutinesTestRule = CoroutinesTestRule()
//    @get:Rule
//    val coroutinesTestRule = InstantTaskExecutorRule()

    private val addLogsUseCase: AddLogUseCase by inject()

    private lateinit var viewModelSUT: SurveyViewModel

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = SurveyViewModel(addLogsUseCase)
    }

    @Test
    fun `add logs`() {

    }
}