package com.p4r4d0x.data.repositories

import android.os.Build
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.p4r4d0x.data.datasources.UserDataSource
import com.p4r4d0x.data.dto.ApiResult
import com.p4r4d0x.data.dto.user.UserResultEnum
import com.p4r4d0x.data.testutils.TestData.USER_ID
import com.p4r4d0x.data.testutils.testDatasourcesModule
import com.p4r4d0x.data.testutils.testRepositoriesModule
import com.p4r4d0x.domain.repository.UserRepository
import com.p4r4d0x.test.KoinBaseTest
import com.p4r4d0x.test.KoinTestApplication
import io.mockk.coEvery
import io.mockk.coVerify
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.jupiter.api.Assertions
import org.junit.runner.RunWith
import org.koin.test.inject
import org.robolectric.annotation.Config

@RunWith(AndroidJUnit4::class)
@Config(application = KoinTestApplication::class, sdk = [Build.VERSION_CODES.P])
class UserRepositoryTest : KoinBaseTest(testRepositoriesModule, testDatasourcesModule) {

    private val userDataSource: UserDataSource by inject()

    private lateinit var repository: UserRepository

    @Before
    fun setUp() {
        repository = UserRepositoryImpl(userDataSource)
    }

    @Test
    fun `test login user success user inserted`() {
        coEvery {
            userDataSource.loginUser(userId = USER_ID)
        } returns ApiResult.Success(UserResultEnum.UserInserted)

        val loginResult = runBlocking {  repository.loginUser(USER_ID) }

        coVerify { userDataSource.loginUser(USER_ID) }
        Assertions.assertTrue(loginResult)
    }

    @Test
    fun `test login user success user exist`() {
        coEvery {
            userDataSource.loginUser(userId = USER_ID)
        } returns ApiResult.Success(UserResultEnum.UserExist)

        val loginResult = runBlocking {  repository.loginUser(USER_ID) }

        coVerify { userDataSource.loginUser(USER_ID) }
        Assertions.assertTrue(loginResult)
    }

    @Test
    fun `test login user error `() {
        coEvery {
            userDataSource.loginUser(userId = USER_ID)
        } returns ApiResult.Success(UserResultEnum.InvalidToken)

        val loginResult = runBlocking {  repository.loginUser(USER_ID) }

        coVerify { userDataSource.loginUser(USER_ID) }
        Assertions.assertFalse(loginResult)
    }
}