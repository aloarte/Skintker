package com.p4r4d0x.domain.usecases

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.domain.CoroutinesTestRule
import com.p4r4d0x.domain.TestData.USER_ID
import com.p4r4d0x.domain.di.testRepositoriesModule
import com.p4r4d0x.domain.repository.UserRepository
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
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

@kotlin.time.ExperimentalTime
@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class LoginUseCaseTest : KoinBaseTest(testRepositoriesModule) {

    @ExperimentalCoroutinesApi
    @get:Rule
    val coroutinesTestRule = CoroutinesTestRule()

    private val userRepository: UserRepository by inject()

    private lateinit var useCase: LoginUserUseCase

    @Before
    fun setUp() {
        useCase = LoginUserUseCase(userRepository)
    }

    @Test
    fun `test login user`() {
        coEvery { userRepository.loginUser(USER_ID) } returns true

        val userLogged = runBlocking { useCase.run(LoginUserUseCase.Params(USER_ID)) }

        coVerify { userRepository.loginUser(USER_ID) }
        Assertions.assertTrue(userLogged)
    }
}