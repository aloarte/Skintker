package com.p4r4d0x.skintker.viewmodel

import android.content.Context
import android.content.SharedPreferences
import android.content.res.Resources
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.firebase.auth.FirebaseAuth
import com.p4r4d0x.domain.bo.ProfileBO
import com.p4r4d0x.domain.usecases.ExportLogsDBUseCase
import com.p4r4d0x.domain.usecases.RemoveLocalLogsUseCase
import com.p4r4d0x.domain.usecases.RemoveLogsUseCase
import com.p4r4d0x.domain.utils.Constants
import com.p4r4d0x.skintker.CoroutinesTestRule
import com.p4r4d0x.skintker.TestData.USER_ID
import com.p4r4d0x.skintker.di.testUseCasesModule
import com.p4r4d0x.skintker.getOrAwaitValue
import com.p4r4d0x.skintker.presenter.settings.viewmodel.SettingsViewModel
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert
import org.junit.Before
import org.junit.Ignore
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config

@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class SettingsViewModelTest : KoinBaseTest(testUseCasesModule) {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val exportLogsDBUseCase: ExportLogsDBUseCase by inject()
    private val removeLogsUseCase: RemoveLogsUseCase by inject()
    private val removeLocalLogsUseCase: RemoveLocalLogsUseCase by inject()


    private lateinit var viewModelSUT: SettingsViewModel

    companion object {
        const val USER_EMAIL = "user_email"
        const val USER_NAME = "user_name"
    }

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = SettingsViewModel(exportLogsDBUseCase, removeLocalLogsUseCase,removeLogsUseCase)
    }

    @Test
    fun `test settings view model launch export process`() = coroutinesTestRule.runBlockingTest {
        val exportResult = slot<(Boolean?) -> Unit>()
        val resources: Resources = mockk()
        val context: Context = mockk()
        every {
            exportLogsDBUseCase.invoke(
                scope = any(),
                params = ExportLogsDBUseCase.Params(resources, context, USER_ID),
                resultCallback = capture(exportResult)
            )
        } answers {
            exportResult.captured(true)
        }

        viewModelSUT.launchExportUseCase(resources, context, USER_ID)

        val exportProcess = viewModelSUT.exportStatus.getOrAwaitValue()
        Assert.assertTrue(exportProcess)
    }

    @Ignore("Issues mocking firebase auth")
    @Test
    fun `test settings view model get logged user info`() = coroutinesTestRule.runBlockingTest {
        val lastSignedInAccount: GoogleSignInAccount = mockk()
        val firebaseAuth: FirebaseAuth = mockk()
        every { firebaseAuth.currentUser?.isAnonymous } returns true
        every { lastSignedInAccount.email } returns USER_EMAIL
        every { lastSignedInAccount.displayName } returns USER_NAME
        every { lastSignedInAccount.id } returns USER_ID

        val expectedProfile = ProfileBO.AuthenticatedProfileBO(USER_EMAIL, USER_NAME, USER_ID)

        viewModelSUT.getLoggedUserInfo(lastSignedInAccount)

        val profile = viewModelSUT.profile.getOrAwaitValue()
        Assert.assertEquals(expectedProfile, profile)
    }

    @Test
    fun `test settings view model update reminder time less than 10`() =
        coroutinesTestRule.runBlockingTest {
            val preferences: SharedPreferences = mockk()
            every { preferences.getInt(Constants.PREFERENCES_ALARM_HOUR, -1) } returns 1
            every { preferences.getInt(Constants.PREFERENCES_ALARM_MINUTES, -1) } returns 1

            viewModelSUT.updateReminderTime(preferences)

            Assert.assertEquals("01:01", viewModelSUT.reminderTime.first())
        }

    @Test
    fun `test settings view model update reminder time more than 10`() =
        coroutinesTestRule.runBlockingTest {
            val preferences: SharedPreferences = mockk()
            every { preferences.getInt(Constants.PREFERENCES_ALARM_HOUR, -1) } returns 23
            every { preferences.getInt(Constants.PREFERENCES_ALARM_MINUTES, -1) } returns 59

            viewModelSUT.updateReminderTime(preferences)

            Assert.assertEquals("23:59", viewModelSUT.reminderTime.first())
        }

    @Test
    fun `test settings view model update reminder no time`() =
        coroutinesTestRule.runBlockingTest {
            val preferences: SharedPreferences = mockk()
            every { preferences.getInt(Constants.PREFERENCES_ALARM_HOUR, -1) } returns -1
            every { preferences.getInt(Constants.PREFERENCES_ALARM_MINUTES, -1) } returns -1

            viewModelSUT.updateReminderTime(preferences)

            Assert.assertEquals("", viewModelSUT.reminderTime.first())
        }

    @Test
    fun `test settings view model remove logs`() =
        coroutinesTestRule.runBlockingTest {
            val removeResult = slot<(Boolean?) -> Unit>()
            every {
                removeLogsUseCase.invoke(
                    scope = any(),
                    params = RemoveLogsUseCase.Params(userId = USER_ID),
                    resultCallback = capture(removeResult)
                )
            } answers {
                removeResult.captured(true)
            }

            viewModelSUT.removeUserData(USER_ID)

            Assert.assertTrue(viewModelSUT.removeStatus.getOrAwaitValue())
        }
}