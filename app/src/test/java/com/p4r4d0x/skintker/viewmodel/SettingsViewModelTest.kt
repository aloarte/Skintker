package com.p4r4d0x.skintker.viewmodel

import android.content.Context
import android.content.res.Resources
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.p4r4d0x.skintker.di.*
import com.p4r4d0x.skintker.domain.bo.ProfileBO
import com.p4r4d0x.skintker.domain.usecases.ExportLogsDBUseCase
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
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

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class SettingsViewModelTest : KoinBaseTest(testRepositoriesModule, testUseCasesModule) {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val exportLogsDBUseCase: ExportLogsDBUseCase by inject()

    private lateinit var viewModelSUT: SettingsViewModel

    companion object {
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
        const val USER_ID = "user_id"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = SettingsViewModel(exportLogsDBUseCase)
    }

    @Test
    fun `test settings view model launch export process`() = coroutinesTestRule.runBlockingTest {
        val exportResult = slot<(Boolean?) -> Unit>()
        val resources: Resources = mockk()
        val context: Context = mockk()
        every {
            exportLogsDBUseCase.invoke(
                scope = any(),
                params = ExportLogsDBUseCase.Params(resources, context),
                resultCallback = capture(exportResult)
            )
        } answers {
            exportResult.captured(true)
        }

        viewModelSUT.launchExportUseCase(resources, context)

        val exportProcess = viewModelSUT.exportStatus.getOrAwaitValue()
        Assert.assertTrue(exportProcess)
    }

    @Test
    fun `test settings view model get logged user info`() = coroutinesTestRule.runBlockingTest {
        val lastSignedInAccount: GoogleSignInAccount = mockk()
        every { lastSignedInAccount.email } returns USER_EMAIL
        every { lastSignedInAccount.displayName } returns USER_NAME
        every { lastSignedInAccount.id } returns USER_ID

        val expectedProfile = ProfileBO(USER_EMAIL, USER_NAME, USER_ID)

        viewModelSUT.getLoggedUserInfo(lastSignedInAccount)

        val profile = viewModelSUT.profile.getOrAwaitValue()
        Assert.assertEquals(expectedProfile, profile)
    }
}