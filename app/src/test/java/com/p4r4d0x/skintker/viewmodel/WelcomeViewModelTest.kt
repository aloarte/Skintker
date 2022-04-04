package com.p4r4d0x.skintker.viewmodel

import android.content.SharedPreferences
import android.os.Build
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.p4r4d0x.skintker.data.Event
import com.p4r4d0x.skintker.di.*
import com.p4r4d0x.skintker.domain.bo.DailyLogBO
import com.p4r4d0x.skintker.domain.parsers.DataParser.getCurrentFormattedDate
import com.p4r4d0x.skintker.domain.usecases.GetLogUseCase
import com.p4r4d0x.skintker.presenter.main.FragmentScreen
import com.p4r4d0x.skintker.presenter.welcome.viewmodel.WelcomeViewModel
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
class WelcomeViewModelTest : KoinBaseTest(testRepositoriesModule, testUseCasesModule) {

    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    @get:Rule
    val instantTaskExecutorRule = InstantTaskExecutorRule()

    private val getLogUseCase: GetLogUseCase by inject()

    private lateinit var viewModelSUT: WelcomeViewModel

    private val date = getCurrentFormattedDate()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        viewModelSUT = WelcomeViewModel(getLogUseCase)
    }

    @Test
    fun `test home view model check log reported today`() =
        coroutinesTestRule.runBlockingTest {
            val logResult = slot<(DailyLogBO?) -> Unit>()
            val log = DailyLogBO(date = date, foodList = emptyList())

            every {
                getLogUseCase.invoke(
                    scope = any(),
                    params = GetLogUseCase.Params(date),
                    resultCallback = capture(logResult)
                )
            } answers {
            logResult.captured(log)
        }

        viewModelSUT.checkLogReportedToday()

        val logReported = viewModelSUT.logReported.getOrAwaitValue()
        Assert.assertTrue(logReported)
    }

    @Test
    fun `test home view model check log not reported today`() =
        coroutinesTestRule.runBlockingTest {
            val logResult = slot<(DailyLogBO?) -> Unit>()

            every {
                getLogUseCase.invoke(
                    scope = any(),
                    params = GetLogUseCase.Params(date),
                    resultCallback = capture(logResult)
                )
            } answers {
                logResult.captured(null)
            }

            viewModelSUT.checkLogReportedToday()

            val logReported = viewModelSUT.logReported.getOrAwaitValue()
            Assert.assertFalse(logReported)
        }

    @Test
    fun `test home view model checkuser login user authenticated`() =
        coroutinesTestRule.runBlockingTest {
            val googleSignIn: GoogleSignInAccount = mockk()
            val preferences: SharedPreferences = mockk()
            every { preferences.edit() } returns null

            viewModelSUT.checkUserLogin(googleSignIn, preferences)

            val userAuthenticated = viewModelSUT.userAuthenticated.getOrAwaitValue()
            Assert.assertTrue(userAuthenticated)
        }

    @Test
    fun `test home view model checkuser login user not authenticated`() =
        coroutinesTestRule.runBlockingTest {
            val preferences: SharedPreferences = mockk()

            viewModelSUT.checkUserLogin(null, preferences)

            val userAuthenticated = viewModelSUT.userAuthenticated.getOrAwaitValue()
            Assert.assertFalse(userAuthenticated)
        }

    @Test
    fun `test home view model handle continue home log not yet reported`() =
        coroutinesTestRule.runBlockingTest {
            viewModelSUT.handleContinueHome(logAlreadyReported = false)

            val navigateToValue = viewModelSUT.navigateTo.getOrAwaitValue()
            Assert.assertEquals(Event(FragmentScreen.Survey), navigateToValue)
        }

    @Test
    fun `test home view model handle continue home log already reported`() =
        coroutinesTestRule.runBlockingTest {
            viewModelSUT.handleContinueHome(logAlreadyReported = true)

            val navigateToValue = viewModelSUT.navigateTo.getOrAwaitValue()
            Assert.assertEquals(Event(FragmentScreen.Home), navigateToValue)
        }

    @Test
    fun `test home view model handle continue login user authenticated`() =
        coroutinesTestRule.runBlockingTest {
            val logResult = slot<(DailyLogBO?) -> Unit>()
            every {
                getLogUseCase.invoke(
                    scope = any(),
                    params = GetLogUseCase.Params(date),
                    resultCallback = capture(logResult)
                )
            } answers {
                logResult.captured(null)
            }

            viewModelSUT.handleContinueLogin(userAuthenticated = true)

            val logReported = viewModelSUT.logReported.getOrAwaitValue()
            Assert.assertFalse(logReported)
        }

    @Test
    fun `test home view model handle continue login user not authenticated`() =
        coroutinesTestRule.runBlockingTest {
            viewModelSUT.handleContinueLogin(userAuthenticated = false)

            val navigateToValue = viewModelSUT.navigateTo.getOrAwaitValue()
            Assert.assertEquals(Event(FragmentScreen.Login), navigateToValue)
        }

}